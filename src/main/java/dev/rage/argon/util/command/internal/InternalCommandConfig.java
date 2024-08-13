package dev.rage.argon.util.command.internal;

import dev.rage.argon.config.LanguageConfig;
import dev.rage.argon.util.ColorUtil;
import dev.rage.argon.util.command.CommandConfig;
import org.bukkit.ChatColor;

public class InternalCommandConfig implements CommandConfig {

    @Override
    public String getNoPermissionsMessage() {
        return ColorUtil.colorize(LanguageConfig.COMMAND_NO_PERMISSION);
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.RED;
    }
}
