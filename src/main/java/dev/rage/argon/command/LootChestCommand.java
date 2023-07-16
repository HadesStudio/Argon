package dev.rage.argon.command;

import dev.rage.argon.config.LanguageConfig;
import dev.rage.argon.config.MenusConfig;
import dev.rage.argon.config.SettingsConfig;
import dev.rage.argon.util.command.annotation.Command;
import org.bukkit.command.CommandSender;

public class LootChestCommand {

    @Command(names = "lootchests reload", permission = "lootchests.command.reload", description = "Reloads all config files")
    public static void reload(CommandSender sender) {
        LanguageConfig.INSTANCE.reload();
        MenusConfig.INSTANCE.reload();
        SettingsConfig.INSTANCE.reload();
    }
}
