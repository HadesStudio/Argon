package dev.rage.lootchests.util.command.internal;

import com.google.common.collect.Lists;
import dev.rage.lootchests.util.command.ArgumentFormatter;
import dev.rage.lootchests.util.command.CommandContext;
import dev.rage.lootchests.util.command.CommandExecutor;
import dev.rage.lootchests.util.command.CommandHandler;
import dev.rage.lootchests.util.command.provider.Provider;
import dev.rage.lootchests.util.command.provider.ProviderData;
import dev.rage.lootchests.util.command.provider.exception.ProviderException;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RequiredArgsConstructor
public class InternalCommandExecutor implements CommandExecutor {

	private final CommandContext context;
	private final CommandHandler commandHandler;
	
	@Override
	public boolean canExecute(CommandSender sender) {
		if(context.hasPermission()) {
			switch (context.getPermission().toLowerCase()) {
			case "console":
				return (sender instanceof ConsoleCommandSender);
			case "op":
				return sender.isOp();
			default:
				return sender.hasPermission(context.getPermission());
			}
		}
		
		return true;
	}

	@Override
	public boolean execute(CommandSender sender, ArgumentFormatter formatter) {
		if(context.getMethod() == null) {
			if(context.hasChildrens()) {
				if(canExecute(sender)) {
					context.getHelp(sender, true);
				} else {
					if(context.isHidden()) {
						sender.sendMessage(commandHandler.getConfig().getUnknownCommandMessage());
					} else {
						sender.sendMessage(commandHandler.getConfig().getNoPermissionMessage());
					}
				}
			} else {
                sender.sendMessage(commandHandler.getConfig().getUnknownCommandMessage());
            }
			
			return true;
		}
		
		if(context.getSenderClass().equals(Player.class) && sender instanceof ConsoleCommandSender) {
			sender.sendMessage(commandHandler.getConfig().getOnlyPlayersMessage());
			return true;
		}
		
		if(context.getSenderClass().equals(ConsoleCommandSender.class) && sender instanceof Player) {
			sender.sendMessage(commandHandler.getConfig().getOnlyConsoleMessage());
			return true;
		}
		
		List<Object> objects = Lists.newArrayList();
		objects.add(sender);
		
		int index = 0;
		for(ProviderData data : context.getParameters()) {
			String argument;
			
			try {
                argument = formatter.getArguments().get(index);
            } catch (Exception ex) {
                if (data.getDefaultValue().isEmpty()) {
                    return false;
                }
                
                argument = data.getDefaultValue();
            }
			
			if(data.isWildcard() && (argument.isEmpty() || !argument.equals(data.getDefaultValue()))) {
                argument = formatter.join(index);
            }
			
			if(data.getType().equals(String.class)) {
				objects.add(argument);
			} else {
				Provider<?> provider = commandHandler.getBinder().getBinding(data.getType()).getProvider();
	            if (provider == null) {
	            	sender.sendMessage(ChatColor.RED + "No provider found: " + data.getType().getSimpleName());
	                return true;
	            }
	            
	            try {
	            	Object result = provider.provide(sender, argument);
	            	if (result == null) {
		                return true;
		            }
		            
		            objects.add(result);
				} catch (ProviderException e) {
					sender.sendMessage(e.getLocalizedMessage());
					return true;
				}
			}
			
			index++;
		}
		
		try {
			
			context.getMethod().invoke(null, objects.toArray());
            
            return true;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new CommandException("An error occurred while executing the command", e);
        }
	}
}
