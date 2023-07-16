package dev.rage.argon.util.command.bukkit;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import dev.rage.argon.util.command.ArgumentFormatter;
import dev.rage.argon.util.command.CommandContext;
import dev.rage.argon.util.command.CommandHandler;
import dev.rage.argon.util.command.processor.impl.ArgumentFormatterProcessor;
import dev.rage.argon.util.command.provider.Provider;
import dev.rage.argon.util.command.provider.ProviderData;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Getter
public class BukkitCommand extends Command implements PluginIdentifiableCommand {
	
	private CommandContext context;
    protected JavaPlugin owningPlugin;
    protected CommandHandler commandHandler;
	
    public BukkitCommand(CommandContext context, JavaPlugin plugin, CommandHandler commandHandler) {
        super(context.getName(), "", "/", Lists.newArrayList(context.getRealAliases()));
        
        this.context = context;
        this.owningPlugin = plugin;
        this.commandHandler = commandHandler;
    }

	@Override
	public Plugin getPlugin() {
		return owningPlugin;
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		alias = alias.replace(this.owningPlugin.getName().toLowerCase() + ":", "");
        String[] newArgs = this.concat(alias, args);
        
        ArgumentFormatter formatter = new ArgumentFormatterProcessor().process(newArgs);
        CommandContext executionContext = this.context.findChildren(formatter);
        String realLabel = this.getFullLabel(executionContext);
        
        if (executionContext.getCommandExecutor().canExecute(sender)) {
            if (executionContext.isAsync()) {
            	ForkJoinPool.commonPool().execute(() -> {
            		try {
                        if (!executionContext.getCommandExecutor().execute(sender, formatter)) {
                        	sender.sendMessage(commandHandler.getConfig().getUsageFormat() + ' ' + executionContext.getUsageGenerator().generate(realLabel));
                        }
                    } catch (CommandException ex) {
                    	sender.sendMessage(commandHandler.getConfig().getUsageFormat() + ' ' + executionContext.getUsageGenerator().generate(realLabel));
                        
                        sender.sendMessage(ChatColor.RED + "An error occurred while processing your command.");
                        
                        if (sender.isOp()) {
                            BukkitCommand.this.sendStackTrace(sender, (Exception) ex);
                        }
                    }
            	});
            } else {
                try {
                    if (!executionContext.getCommandExecutor().execute(sender, formatter)) {
                    	sender.sendMessage(this.commandHandler.getConfig().getUsageFormat() + ' ' + executionContext.getUsageGenerator().generate(realLabel));
                    }
                } catch (CommandException ex) {
                	sender.sendMessage(this.commandHandler.getConfig().getUsageFormat() + ' ' + executionContext.getUsageGenerator().generate(realLabel));
                    
                    sender.sendMessage(ChatColor.RED + "An error occurred while processing your command.");
                    
                    if (sender.isOp()) {
                        this.sendStackTrace(sender, (Exception) ex);
                    }
                }
            }
        } else if (executionContext.isHidden()) {
            sender.sendMessage(commandHandler.getConfig().getUnknownCommandMessage());
        } else {
            sender.sendMessage(commandHandler.getConfig().getNoPermissionMessage());
        }
        
        return true;
	}

    @Override
    public List<String> tabComplete(CommandSender sender, String cmdLine, String[] rawArgs) {
        cmdLine = cmdLine.replace(this.owningPlugin.getName().toLowerCase() + ":", "");
        rawArgs = this.concat(cmdLine, rawArgs);

        ArgumentFormatter formatter = new ArgumentFormatterProcessor().process(rawArgs);
        CommandContext realContext = this.context.findChildren(formatter);

        if (!realContext.getCommandExecutor().canExecute(sender)) {
            return Collections.emptyList();
        }

        List<String> realArgs = formatter.getArguments();
        int currentIndex = realArgs.size() - 1;
        if (currentIndex < 0) {
            currentIndex = 0;
        }

        if (cmdLine.endsWith(" ") && realArgs.size() >= 1) {
            ++currentIndex;
        }

        List<String> completions = Lists.newArrayList();
        if (realContext.hasChildrens()) {
            String name = (realArgs.size() == 0) ? "" : realArgs.get(realArgs.size() - 1);
            completions.addAll(realContext.getChildrens().values().stream().filter(context -> context.getCommandExecutor().canExecute(sender) && (StringUtils.startsWithIgnoreCase(context.getName(), name) || StringUtils.isEmpty(name))).map(CommandContext::getName).collect(Collectors.toList()));
            if (completions.size() > 0) {
                return completions;
            }
        }

        if (rawArgs[rawArgs.length - 1].equalsIgnoreCase(realContext.getName()) && !cmdLine.endsWith(" ")) {
            return ImmutableList.of();
        }

        try {
            Provider<?> provider = null;
            ProviderData data;

            if (realContext.getParameters() != null) {
                List<ProviderData> params =  realContext.getParameters();
                int fixed = Math.max(0, currentIndex - 1);
                data = params.get(fixed);
                provider = commandHandler.getBinder().getBinding(data.getType()).getProvider();
            }
            if (provider != null) {
                if (currentIndex < realArgs.size() && realArgs.get(currentIndex).equalsIgnoreCase(realContext.getName())) {
                    realArgs.add("");
                    ++currentIndex;
                }

                String argumentBeingCompleted = currentIndex >= realArgs.size() ? "" : realArgs.get(currentIndex);
                List<String> suggested = provider.suggest(sender, argumentBeingCompleted);
                completions.addAll(suggested.stream().filter(s -> StringUtils.startsWithIgnoreCase(s, argumentBeingCompleted)).collect(Collectors.toList()));
            }
        } catch (Exception ignored) { }

        return completions;
    }
	
	private String[] concat(String label, String[] args) {
        String[] labelAsArray = { label };
        String[] newArgs = new String[args.length + 1];
        System.arraycopy(labelAsArray, 0, newArgs, 0, 1);
        System.arraycopy(args, 0, newArgs, 1, args.length);
        
        return newArgs;
    }
    
    private String getFullLabel(CommandContext context) {
        List<String> labels = Lists.newArrayList();
        while (context != null) {
            String name = context.getName();
            if (name != null) {
                labels.add(name);
            }
            context = context.getRootContext();
        }
        Collections.reverse(labels);
        labels.remove(0);
        StringBuilder builder = new StringBuilder();
        labels.forEach(s -> builder.append(s).append(' '));
        return builder.toString();
    }
    
    private void sendStackTrace(CommandSender sender, Exception exception) {
        String rootCauseMessage = ExceptionUtils.getRootCauseMessage(exception);
        sender.sendMessage(ChatColor.RED + "Message: " + rootCauseMessage);
        String cause = ExceptionUtils.getStackTrace((Throwable)exception);
        StringTokenizer tokenizer = new StringTokenizer(cause);
        String exceptionType = "";
        String details = "";
        boolean parsingNeeded = false;
        
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.equalsIgnoreCase("Caused")) {
                tokenizer.nextToken();
                parsingNeeded = true;
                exceptionType = tokenizer.nextToken();
            } else {
                if (token.equalsIgnoreCase("at") && parsingNeeded) {
                    details = tokenizer.nextToken();
                    break;
                }
                
                continue;
            }
        }
        
        sender.sendMessage(ChatColor.RED + "Exception: " + exceptionType.replace(":", ""));
        sender.sendMessage(ChatColor.RED + "Details:");
        sender.sendMessage(ChatColor.RED + details);
    }
}
