package dev.rage.argon.config;

import dev.rage.argon.util.config.creator.ConfigCreator;
import dev.rage.argon.util.config.creator.annotation.Path;
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

    // Loot box settings
    @Path(id = "loot-box.drop-amount")
    public static int LOOT_BOX_DROPS = 3;

    @Path(id = "loot-box.launch-firework")
    public static boolean LOOT_BOX_FIREWORK = true;

    @Path(id = "loot-box.name")
    public static String LOOT_BOX_NAME = "&d&lLoot Box";

    @Path(id = "loot-box.lore")
    public static List<String> LOOT_BOX_LORE = Arrays.asList(
            " ",
            " &ePossible wins&7:",
            " &fx32 Iron Block",
            " &fx32 Diamond Block",
            " &fx32 Gold Block",
            " &fx32 Emerald Block",
            " ",
            " &aClick to open loot box",
            " "
    );

    // Lucky block settings
    @Path(id = "lucky-block.drop-amount")
    public static int LUCKY_BLOCK_DROPS = 3;

    @Path(id = "lucky-block.name")
    public static String LUCKY_BLOCK_NAME = "&6&lLucky Block";

    @Path(id = "lucky-block.lore")
    public static List<String> LUCKY_BLOCK_LORE = Arrays.asList(
            " ",
            " &ePossible wins&7:",
            " &fx32 Iron Block",
            " &fx32 Diamond Block",
            " &fx32 Gold Block",
            " &fx32 Emerald Block",
            " &fx16 Dirt",
            " &fx16 Cobblestone",
            " &fx5 Bowl",
            " &fx64 Stick",
            " &fx16 Bone",
            " ",
            " &aClick to place the lucky block",
            " "
    );

    // Mystery box settings
    @Path(id = "mystery-box.name")
    public static String MYSTERY_BOX_NAME = "&5&lMystery Box";

    @Path(id = "mystery-box.lore")
    public static List<String> MYSTERY_BOX_LORE = Arrays.asList(
            " ",
            " &ePossible wins&7:",
            " &fx32 Iron Block",
            " &fx32 Diamond Block",
            " &fx32 Gold Block",
            " &fx32 Emerald Block",
            " ",
            " &ePossible op wins&7:",
            " &fx64 Iron Block",
            " &fx64 Diamond Block",
            " &fx64 Gold Block",
            " &fx64 Emerald Block",
            " ",
            " &aClick to open mystery box",
            " "
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
