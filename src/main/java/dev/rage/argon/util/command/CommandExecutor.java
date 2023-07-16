package dev.rage.argon.util.command;

import org.bukkit.command.CommandSender;

public interface CommandExecutor {

	boolean canExecute(CommandSender sender);
	
	boolean execute(CommandSender sender, ArgumentFormatter formatter);
}
