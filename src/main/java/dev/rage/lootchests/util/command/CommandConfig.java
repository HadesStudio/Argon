package dev.rage.lootchests.util.command;

import org.bukkit.ChatColor;

public interface CommandConfig {

	String getNoPermissionMessage();
	
	String getUnknownCommandMessage();
	
	String getUsageFormat();
	
	String getOnlyConsoleMessage();
	
	String getOnlyPlayersMessage();
	
	ChatColor getLineColor();
	
	ChatColor getNoCommandsColor();
	
	ChatColor getCommandColor();
	
	ChatColor getCommaColor();
	
	ChatColor getDescriptionColor();
}