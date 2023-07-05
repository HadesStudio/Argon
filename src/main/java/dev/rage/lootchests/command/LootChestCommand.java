package dev.rage.lootchests.command;

import dev.rage.lootchests.config.LanguageConfig;
import dev.rage.lootchests.config.MenusConfig;
import dev.rage.lootchests.config.SettingsConfig;
import dev.rage.lootchests.util.command.annotation.Command;
import org.bukkit.command.CommandSender;

public class LootChestCommand {

    @Command(names = "lootchests reload", permission = "lootchests.command.reload", description = "Reloads all config files")
    public static void reload(CommandSender sender) {
        LanguageConfig.INSTANCE.reload();
        MenusConfig.INSTANCE.reload();
        SettingsConfig.INSTANCE.reload();
    }
}
