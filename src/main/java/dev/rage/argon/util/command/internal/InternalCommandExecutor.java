package dev.rage.argon.util.command.internal;

import dev.rage.argon.config.LanguageConfig;
import dev.rage.argon.util.ColorUtil;
import dev.rage.argon.util.command.CommandContext;
import dev.rage.argon.util.command.annotation.Flag;
import dev.rage.argon.util.command.data.type.FlagData;
import dev.rage.argon.util.command.data.type.ProviderData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.SpigotConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@RequiredArgsConstructor
public class InternalCommandExecutor implements CommandExecutor, TabCompleter {
	
    @Getter
    private final CommandContext context;
    private final JavaPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        label = label.replace(plugin.getName().toLowerCase() + ":", "");

        List<String> arguments = new ArrayList<>();
        List<String> flags = new ArrayList<>();

        for (String s : args) {
            if (!s.isEmpty()) {
                if ((s.charAt(0) == '-')
                        && (!s.equals("-")
                        && !s.equals("--"))
                        && (Flag.FLAG_PATTERN.matcher(s).matches())) {
                    String flag = s.replaceFirst("-", "");
                    flags.add(flag);
                } else arguments.add(s);
            }
        }

        CommandContext executionNode = context.findContext(arguments);
        String realLabel = (sender instanceof Player ? "/" : "") + executionNode.getFullLabel();

        flags.removeIf(flag -> !executionNode.getFlags().contains(flag));

        if (executionNode.getSenderClass().equals(Player.class) && sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ColorUtil.colorize(LanguageConfig.COMMAND_ONLY_PLAYERS));
            return true;
        }

        if (executionNode.getSenderClass().equals(ConsoleCommandSender.class) && sender instanceof Player) {
            sender.sendMessage(ColorUtil.colorize(LanguageConfig.COMMAND_ONLY_CONSOLE));
            return true;
        }

        if (!executionNode.canUse(sender)) {
            if (executionNode.isHidden()) {
                sender.sendMessage(ColorUtil.colorize(LanguageConfig.COMMAND_HELP));
                return true;
            }

            sender.sendMessage(getContext().getCommandHandler().getConfig().getNoPermissionsMessage());
            return true;
        }

        if (executionNode.isAsync()) {
        	ForkJoinPool.commonPool().execute(() -> {
                try {
                    if (!executionNode.execute(sender, arguments, flags)) {
                        sender.sendMessage(ChatColor.RED + executionNode.getUsage(realLabel));
                    }
                } catch (Exception e) {
                    if (sender.isOp()) {
                        sender.sendMessage(ChatColor.RED + "An error (" + e.getClass().getSimpleName() + ": " + e.getMessage() + ")"
                                + " occurred while executing your command. ");
                    } else {
                        sender.sendMessage(ChatColor.RED + "An error occurred while executing your command. " +
                                "Please contact the server administration if this continues to happen.");
                    }
                    e.printStackTrace();
                }
            });
        } else {
            try {
                if (!executionNode.execute(sender, arguments, flags)) {
                    sender.sendMessage(ChatColor.RED + executionNode.getUsage(realLabel));
                }
            } catch (Exception e) {
                if (sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + "An error (" + e.getClass().getSimpleName() + ": " + e.getMessage() + ")"
                            + " occurred while executing your command. ");
                } else {
                    sender.sendMessage(ChatColor.RED + "An error occurred while executing your command. " +
                            "Please contact the server administration if this continues to happen.");
                }
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!context.canUse(sender)) {
            return new ArrayList<>();
        }
        List<String> arguments = new ArrayList<>();

        for (String s : args) {
            if (!s.isEmpty()) {
                if ((s.charAt(0) == '-')
                        && (!s.equals("-")
                        && !s.equals("--"))
                        && (Flag.FLAG_PATTERN.matcher(s).matches())) {
                    String flag = s.replaceFirst("-", "");
                    if (!context.getFlags().contains(flag))
                        arguments.add(s);
                } else {
                    arguments.add(s);
                }
            }
        }

        List<String> completions = new ArrayList<>();
        CommandContext tabbingNode = context.findContext(arguments);
        int offset = tabbingNode.getFullLabel().split(" ").length - 1;

        if (!tabbingNode.getChilds().isEmpty()) {
            tabbingNode.getChilds().forEach(child -> {
                if (child.canUse(sender) && !child.isHidden()) {
                    completions.add(child.getLabel());
                    completions.addAll(child.getAliases());
                }
            });
        }

        List<ProviderData> parameters = new ArrayList<>();
        tabbingNode.getParameters().forEach(data -> {
            if (data instanceof ProviderData) {
                parameters.add((ProviderData) data);
            }
        });

        List<FlagData> flagList = new ArrayList<>();
        tabbingNode.getParameters().forEach(data -> {
            if (data instanceof FlagData)
                flagList.add((FlagData) data);
        });

        List<String> unusedArguments = new ArrayList<>();
        for (String arg : args) {
            if (!tabbingNode.getFullLabel().contains(arg)) {
                unusedArguments.add(arg);
            }
        }

        if (!parameters.isEmpty()) {
            int index = Math.max(0, args.length - 1) - offset;
            if (index >= 0 && index < parameters.size()) {
                ProviderData providerData = parameters.get(index);
                completions.addAll(providerData.getProvider().suggest(sender,
                        providerData.getCompletionFlags()));
            }
        }

        if (!flagList.isEmpty())
            flagList.forEach(flag -> flag.getNames().forEach(s -> completions.add("-" + s)));

        return getCompletions(args, completions);
    }

    private List<String> getCompletions(String[] args, List<String> input) {
        List<String> result = new ArrayList<>();
        String argument = args[args.length - 1];
        for (String string : input)
            if (string.regionMatches(true, 0, argument,
                    0, argument.length()) && result.size() < 80)
                result.add(string);

        return result;
    }
}
