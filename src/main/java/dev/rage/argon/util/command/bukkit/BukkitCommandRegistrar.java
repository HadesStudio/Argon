package dev.rage.argon.util.command.bukkit;

import dev.rage.argon.util.BroadcastUtil;
import dev.rage.argon.util.command.CommandContext;
import dev.rage.argon.util.command.CommandHandler;
import dev.rage.argon.util.command.CommandRegistrar;
import dev.rage.argon.util.command.annotation.Command;
import dev.rage.argon.util.command.annotation.Flag;
import dev.rage.argon.util.command.annotation.Param;
import dev.rage.argon.util.command.binding.Binding;
import dev.rage.argon.util.command.data.Data;
import dev.rage.argon.util.command.data.type.FlagData;
import dev.rage.argon.util.command.data.type.ProviderData;
import dev.rage.argon.util.command.internal.InternalCommandContext;
import dev.rage.argon.util.command.internal.InternalCommandExecutor;
import dev.rage.argon.util.command.provider.Provider;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Getter
public class BukkitCommandRegistrar extends CommandRegistrar {

	private final CommandHandler commandHandler;
	
	public BukkitCommandRegistrar(CommandHandler commandHandler) {
		super(new HashMap<>());
		
		this.commandHandler = commandHandler;
	}

	@Override
	public void registerObject(JavaPlugin owner, Object object) {
		for (Method method : object.getClass().getDeclaredMethods()) {
            if ((method.getParameterCount() < 1) || (!method.isAnnotationPresent(Command.class)) || (!CommandSender.class.isAssignableFrom(method.getParameterTypes()[0]))) {
                continue;
            }

            Class<?> senderClass = method.getParameterTypes()[0];

            Command command = method.getAnnotation(Command.class);
            List<String> flags = new ArrayList<>();
            List<Data> parameters = new ArrayList<>();
            if (method.getParameterCount() > 1) {
                for (int i = 1; i < method.getParameterCount(); i++) {
                    Parameter parameter = method.getParameters()[i];
                    if (parameter.isAnnotationPresent(Param.class)) {
                        Binding<?> binding = commandHandler.getBinder().getBinding(parameter.getType());

                        if (binding == null) {
                            BroadcastUtil.sendConsole("&c[Argon] (CommandHandler) A provider '" + parameter.getType().getSimpleName() + "' class was not found for '" + command.names()[0] + "' command");
                            return;
                        }
                        Provider<?> provider = binding.getProvider();

                        Param param = parameter.getAnnotation(Param.class);
                        parameters.add(
                                new ProviderData(
                                        param.name(),
                                        param.defaultValue(),
                                        parameter.getType(),
                                        param.wildcard(),
                                        provider,
                                        Arrays.asList(param.completionFlags()),
                                        i - 1
                                )
                        );
                    } else if (parameter.isAnnotationPresent(Flag.class)) {
                        Flag flag = parameter.getAnnotation(Flag.class);
                        parameters.add(
                                new FlagData(
                                        Arrays.asList(flag.names()),
                                        flag.defaultValue(),
                                        flag.description(),
                                        flag.hidden(),
                                        i
                                )
                        );
                        flags.addAll(Arrays.asList(flag.names()));
                    } else {
                        BroadcastUtil.sendConsole("&c[" + owner.getName() + "] Failed to register command '&r" + command.names()[0] + "&c'. Every paramater (except the CommandSender) must either have the @Flat or @Param annotation (" + method.getDeclaringClass().getName() + ":" + method.getName() + ")");
                    }
                }
            }

            CommandContext context = null;
            CommandContext child = null;

            for (String name : command.names()) {
                if (!name.contains(" ")) {
                    if (context == null) {
                        if (getCommands().containsKey(name.toLowerCase())) {
                            context = getCommands().get(name.toLowerCase());
                        } else {
                            context = new InternalCommandContext(commandHandler, senderClass);
                            context.setLabel(name.toLowerCase());
                            
                            registerCommand(owner, context);
                        }
                    } else {
                        if (!context.getAliases().contains(name.toLowerCase()) && !context.getLabel().equalsIgnoreCase(name)) {
                            context.getAliases().add(name.toLowerCase());
                            
                            registerCommand(owner, context);
                        }
                    }

                    if (context.getMethod() == null) {
                        context.setMethod(method);
                        context.setObject(object);
                        context.setAsync(command.async());
                        context.setHidden(command.hidden());
                        context.setPermission(command.permission());
                        context.setDescription(command.description());
                        context.setParameters(parameters);
                        context.setFlags(flags);
                    }

                    getCommands().put(name.toLowerCase(), context);
                    continue;
                }

                String[] args = name.split(" ");

                if (context == null) {
                    if (getCommands().containsKey(args[0].toLowerCase())) {
                        context = getCommands().get(args[0].toLowerCase());
                    } else {
                    	context = new InternalCommandContext(commandHandler, senderClass);
                        context.setLabel(args[0].toLowerCase());
                        
                        registerCommand(owner, context);
                    }
                } else if (!context.getAliases().contains(args[0].toLowerCase()) && !context.getLabel().equalsIgnoreCase(args[0])) {
                    context.getAliases().add(args[0].toLowerCase());
                    
                    registerCommand(owner, context);
                }

                for (int i = 1; i < args.length; i++) {
                    String subCommand = args[i];

                    if (child == null) {
                        if (context.getChild(subCommand.toLowerCase()) != null) {
                            child = context.getChild(subCommand.toLowerCase());
                        } else {
                        	child = new InternalCommandContext(commandHandler, senderClass);
                            child.setLabel(subCommand.toLowerCase());
                            
                            context.registerChild(child);
                        }
                    } else if (!child.getAliases().contains(subCommand.toLowerCase()) && !child.getLabel().equalsIgnoreCase(subCommand))
                        child.getAliases().add(subCommand.toLowerCase());

                    if (i == args.length - 1) {
                        child.setMethod(method);
                        child.setObject(object);
                        child.setAsync(command.async());
                        child.setHidden(command.hidden());
                        child.setPermission(command.permission());
                        child.setDescription(command.description());
                        child.setParameters(parameters);
                        child.setFlags(flags);
                    } else {
                        context = child;
                        child = null;
                    }
                }

            }

        }
	}

    @Override
    public void registerCommand(JavaPlugin owner, CommandContext context) {
        InternalCommandExecutor commandExecutor = new InternalCommandExecutor(context, owner);

        getCommands().put(context.getLabel().toLowerCase(), context);
        context.getAliases().forEach(alias -> getCommands().put(alias.toLowerCase(), context));

        PluginCommand pluginCommand = getPluginCommmand(commandExecutor, owner);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(commandExecutor);
            pluginCommand.setTabCompleter(commandExecutor);
            setFieldValue(pluginCommand, "aliases", context.getAliases());
            setFieldValue(pluginCommand, "activeAliases", context.getAliases());
            setFieldValue(pluginCommand, "usageMessage", "");
            setFieldValue(pluginCommand, "description", context.getDescription());

            getCommandMap().register(owner.getName(), pluginCommand);
        } else {
            BroadcastUtil.sendConsole("Failed to register command " + context.getFullLabel());
        }
    }

    private CommandMap getCommandMap() {
        try {
            Server server = Bukkit.getServer();
            Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            return (CommandMap) field.get(server);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static PluginCommand getPluginCommmand(InternalCommandExecutor command, JavaPlugin plugin) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class,
                    Plugin.class);
            constructor.setAccessible(true);
            return constructor.newInstance(command.getContext().getLabel(), plugin);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void setFieldValue(PluginCommand pluginCommand, String fieldName, Object value) {
        try {
            Field field = pluginCommand.getClass().getSuperclass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(pluginCommand, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
