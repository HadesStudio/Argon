package dev.rage.argon.util.command.bukkit;

import dev.rage.argon.util.command.CommandContext;
import dev.rage.argon.util.command.CommandHandler;
import dev.rage.argon.util.command.CommandRegistrar;
import dev.rage.argon.util.command.processor.impl.MethodProcessor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

@RequiredArgsConstructor
public class BukkitCommandRegistrar implements CommandRegistrar {

	private final CommandHandler commandHandler;
		
	@Override
	public void registerObject(JavaPlugin owner, Object object) {
		initializeClass(owner, object.getClass());
	}
	
	@Override
	public void registerClass(JavaPlugin owner, Class<?> clazz) {
		initializeClass(owner, clazz);
	}
	
	private void processMethod(JavaPlugin owner, Method method) {
        method.setAccessible(true);
        Set<CommandContext> contexts = new MethodProcessor(commandHandler).process(method);
        
        if (contexts != null) {
            contexts.forEach(context -> {
                if (context != null) {
                	BukkitCommand command = new BukkitCommand(context, owner, commandHandler);
                    addToMap(owner, command);
                    context.getChildrens().values().forEach(n -> registerHelpTopic(n, context.getAliases()));
                }
            });
        }
    }
	
	private void addToMap(JavaPlugin owner, BukkitCommand command) {
        try {
        	getCommandMap().register(owner.getName(), command);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    }
	
	private void initializeClass(JavaPlugin owner, Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            processMethod(owner, method);
        }
    }
	
	protected void registerHelpTopic(CommandContext context, Set<String> aliases) {
        if (context.getMethod() != null) {
            Bukkit.getHelpMap().addTopic((HelpTopic) new BukkitHelpTopic(commandHandler, context, aliases));
        }
        
        if (context.hasChildrens()) {
            context.getChildrens().values().forEach(n -> registerHelpTopic(n, null));
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
}
