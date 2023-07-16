package dev.rage.argon.util.command.internal;

import dev.rage.argon.config.LanguageConfig;
import dev.rage.argon.config.SettingsConfig;
import dev.rage.argon.util.ColorUtil;
import dev.rage.argon.util.command.CommandConfig;
import org.bukkit.ChatColor;

public class InternalCommandConfig implements CommandConfig {

	@Override
	public String getNoPermissionMessage() {
		return ColorUtil.colorize(LanguageConfig.COMMAND_NO_PERMISSION);
	}

	@Override
	public String getUnknownCommandMessage() {
		return ColorUtil.colorize(LanguageConfig.COMMAND_HELP);
	}
	
	@Override
	public String getUsageFormat() {
		return ColorUtil.colorize(LanguageConfig.COMMAND_USAGE_FORMAT);
	}

	@Override
	public String getOnlyConsoleMessage() {
		return ColorUtil.colorize(LanguageConfig.COMMAND_ONLY_CONSOLE);
	}

	@Override
	public String getOnlyPlayersMessage() {
		return ColorUtil.colorize(LanguageConfig.COMMAND_ONLY_PLAYERS);
	}

	@Override
	public ChatColor getLineColor() {
		return ChatColor.GRAY;
	}

	@Override
	public ChatColor getNoCommandsColor() {
		return ChatColor.RED;
	}

	@Override
	public ChatColor getCommandColor() {
		return SettingsConfig.PRIMARY_COLOR;
	}

	@Override
	public ChatColor getCommaColor() {
		return ChatColor.WHITE;
	}

	@Override
	public ChatColor getDescriptionColor() {
		return ChatColor.GRAY;
	}
}
