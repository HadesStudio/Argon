package dev.astro.argon.util.command.internal;

import dev.astro.argon.config.LanguageConfig;
import dev.astro.argon.util.ColorUtil;
import dev.astro.argon.util.command.CommandContext;
import dev.astro.argon.util.command.CommandHandler;
import dev.astro.argon.util.command.data.Data;
import dev.astro.argon.util.command.data.type.FlagData;
import dev.astro.argon.util.command.data.type.ProviderData;
import dev.astro.argon.util.command.provider.exception.ProviderException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.spigotmc.SpigotConfig;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class InternalCommandContext extends CommandContext {

	public InternalCommandContext(CommandHandler commandHandler, Class<?> senderClass) {
		super(commandHandler, senderClass);
	}

	@Override
	public String getUsage(String realLabel) {
        StringBuilder builder = new StringBuilder(LanguageConfig.COMMAND_USAGE_FORMAT);
        builder.append(realLabel).append(" ");

        List<FlagData> flags = new ArrayList<>();
        for (Data parameter : parameters) {
            if (!(parameter instanceof FlagData))
                continue;

            FlagData data = (FlagData) parameter;
            if (data.isHidden())
                continue;

            flags.add(data);
        }

        List<ProviderData> params = new ArrayList<>();
        for (Data parameter : parameters) {
            if (!(parameter instanceof ProviderData))
                continue;

            params.add((ProviderData) parameter);
        }

        if (!params.isEmpty()) {
            int index = 0;
            for (ProviderData param : params) {
                boolean required = param.getDefaultValue().isEmpty();
                boolean wildcard = param.isWildCard();
                builder.append(required ? "<" : "[").append(param.getName()).append(wildcard ? "..." : "").append(required ? ">" : "]").append(index != params.size() - 1 ? " " : "");
                index++;
            }
        }

        boolean firstFlag = true;

        if (!flags.isEmpty()) {

            builder.append(" (");

            for (FlagData flag : flags) {
                if (!firstFlag) {
                    builder.append(" | ");
                }
                firstFlag = false;
                builder.append("-").append(flag.getNames().get(0));
            }

            builder.append(")");
        }

        flags.clear();
        params.clear();
        return builder.toString();
	}

	@Override
	public boolean execute(CommandSender sender, List<String> args, List<String> flags) {
		if (method == null) {
            if (!childs.isEmpty()) {

                List<CommandContext> usable = new ArrayList<>();
                for (CommandContext context : childs)
                    if (context.canUse(sender) && !context.isHidden())
                        usable.add(context);

                if (usable.isEmpty()) {
                    if (isHidden()) {
                        sender.sendMessage(ColorUtil.colorize(LanguageConfig.COMMAND_HELP));
                        return true;
                    }

                    sender.sendMessage(getCommandHandler().getConfig().getNoPermissionsMessage());
                    return true;
                }

                if (getSenderClass().equals(Player.class) && sender instanceof ConsoleCommandSender) {
                    sender.sendMessage(ColorUtil.colorize(LanguageConfig.COMMAND_ONLY_PLAYERS));
                    return true;
                }

                if (getSenderClass().equals(ConsoleCommandSender.class) && sender instanceof Player) {
                    sender.sendMessage(ColorUtil.colorize(LanguageConfig.COMMAND_ONLY_CONSOLE));
                    return true;
                }

                usable.sort(Comparator.comparing(CommandContext::getFullLabel));

                sender.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "----------------------------------------------------------------");
                usable.forEach(node -> {
                    String usage = getCommandHandler().getConfig().getColor() + (sender instanceof Player ? "/" : "") + node.getUsage(node.getFullLabel()).substring(7);

                    usage = usage.trim() + "&f, &7(" + node.getDescription() + ")";

                    sender.sendMessage(ColorUtil.colorize(usage));
                });

                sender.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "----------------------------------------------------------------");
                return true;
            }
            sender.sendMessage(SpigotConfig.unknownCommandMessage);
            return true;
        }

        List<ProviderData> realParameters = new ArrayList<>();
        for (Data data : parameters) {
            if (data instanceof ProviderData) {
                realParameters.add((ProviderData) data);
            }
        }
        List<String> unusedArguments = new ArrayList<>(args);
        List<ProviderData> defaultsUsed = new ArrayList<>();
        Map<Integer, Object> objects = new HashMap<>();
        objects.put(0, sender);
        int index = 0;
        for (Data parameter : parameters) {
            if (parameter instanceof FlagData) {
                FlagData data = (FlagData) parameter;
                boolean value = data.isDefaultValue();
                for (String name : data.getNames()) {
                    if (flags.contains(name.toLowerCase())) {
                        value = !value;
                    }
                }
                objects.put(data.getIndex(), value);
            } else if (parameter instanceof ProviderData) {
                ProviderData data = (ProviderData) parameter;
                String argument;
                if ((!data.getDefaultValue().isEmpty()) && (args.size() < realParameters.size())) {
                    argument = data.getDefaultValue();
                    
                    try {
						Object parsed = data.getProvider().provide(sender, argument);
						if (parsed == null) {
	                        return true;
	                    }
						
						objects.put(data.getIndex() + 1, parsed);
	                    defaultsUsed.add(data);
	                    continue;
					} catch (ProviderException e) {
						sender.sendMessage(e.getLocalizedMessage());
						return true;
					}
                } else {
                    try {
                        argument = args.get(index);
                    } catch (Exception e) {
                        return false;
                    }
                }
                unusedArguments.remove(argument);
                if ((data.isWildCard()) && (!args.isEmpty()) /*|| (argument.equals(data.getDefaultValue()))*/) {
                    argument = StringUtils.join(args.toArray(new String[0]), " ", index, args.size());
                }
                if (data.getProvider() == null) {
                    sender.sendMessage(ChatColor.RED + "Could not find a Provider to parse " + data.getType().getName() + "." +
                            " Please contact the server administration if this continues to happen.");
                    return true;
                }
                
                try {
                	Object parsed = data.getProvider().provide(sender, argument);
                    if (parsed == null) {
                        return true;
                    }
                    objects.put(data.getIndex() + 1, parsed);
				} catch (ProviderException e) {
					sender.sendMessage(e.getLocalizedMessage());
					return true;
				}
            }
            index++;
        }

        index = 0;
        for (ProviderData data : defaultsUsed) {
            String argument;
            try {
                argument = unusedArguments.get(index);
            } catch (Exception e) {
                continue;
            }
            
            try {
            	Object parsed = data.getProvider().provide(sender, argument);
                if (parsed == null) {
                    continue;
                }
                objects.put(data.getIndex() + 1, parsed);
                index++;
			} catch (ProviderException e) {
				sender.sendMessage(e.getLocalizedMessage());
				return true;
			}
        }

        try {
        	method.invoke(object, objects.values().toArray());
            return true;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
	}

}
