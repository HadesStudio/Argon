package dev.astro.argon.util.command.internal;

import dev.astro.argon.config.LanguageConfig;
import dev.astro.argon.util.ColorUtil;
import dev.astro.argon.util.command.CommandConfig;
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
