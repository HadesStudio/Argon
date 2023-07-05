package dev.rage.lootchests.config;

import dev.rage.lootchests.util.config.creator.ConfigCreator;
import dev.rage.lootchests.util.config.creator.annotation.Path;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class SettingsConfig extends ConfigCreator {

    public static SettingsConfig INSTANCE;

    // Key settings
    @Path(id = "key.name")
    public static String KEY_NAME = "<crate> &7Key";

    @Path(id = "key.lore")
    public static List<String> KEY_LORE = Arrays.asList(
            "",
            "&7Left click to view loot",
            "&7Right click to open key"
    );

    // Color settings
    @Path(id = "color.primary")
    public static ChatColor PRIMARY_COLOR = ChatColor.DARK_RED;

    @Path(id = "color.secondary")
    public static ChatColor SECONDARY_COLOR = ChatColor.RED;

    public SettingsConfig(JavaPlugin plugin) {
        super(plugin, "settings.yml");

        INSTANCE = this;
    }
}
