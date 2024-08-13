package dev.rage.argon.config;

import dev.rage.argon.util.config.creator.ConfigCreator;
import dev.rage.argon.util.config.creator.annotation.Path;
import org.bukkit.plugin.java.JavaPlugin;

public class LanguageConfig extends ConfigCreator {

    public static LanguageConfig INSTANCE;

    @Path(id = "prefix")
    public static String PREFIX = "&c&lArgon";

    @Path(id = "inventory-full")
    public static String INVENTORY_FULL = "<prefix> &7» &cYour inventory is full!";

    // Command messages
    @Path(id = "crate.already-created")
    public static String CRATE_ALREADY_CREATED = "<prefix> &7» &eCrate named '&r<crate>&e' is already created.";

    @Path(id = "crate.create")
    public static String CRATE_CREATED = "<prefix> &7» &aYou have created a new crate named '&r<crate>&a'.";

    @Path(id = "crate.deleted")
    public static String CRATE_DELETED= "<prefix> &7» &cYou have deleted a crate named '&r<crate>&c'.";

    @Path(id = "crate.given-crate")
    public static String CRATE_GIVEN_CRATE= "<prefix> &7» &aA '&r<crate>&a' crate opener was added to your inventory.";

    @Path(id = "crate.placed-crate")
    public static String CRATE_PLACED = "<prefix> &7» &eYou have placed '&r<crate>&e' at '&r<location>&e'.";

    @Path(id = "crate.removed-crate")
    public static String CRATE_REMOVED = "<prefix> &7» &eYou have removed '&r<crate>&e' from '&r<location>&e'.";

    @Path(id = "crate.edited-loot")
    public static String CRATE_EDITED_LOOT = "<prefix> &7» &eYou have edited loot of '&r<crate>&e'.";

    @Path(id = "crate.no-holding-key")
    public static String CRATE_NO_KEY = "<prefix> &7» &cYou need to hold a '&r<crate>&c' key on your hand!";

    @Path(id = "crate.no-selected-item")
    public static String CRATE_NO_ITEM = "<prefix> &7» &cNo item selected!";

    @Path(id = "crate.added-keys")
    public static String CRATE_ADDED_KEYS = "<prefix> &7» &aAdded &fx<amount> &a'<crate>&r' &acrate key to your inventory!";

    @Path(id = "crate.added-virtual-keys")
    public static String CRATE_ADDED_VIRTUAL_KEYS = "<prefix> &7» &aAdded &fx<amount> &a'<crate>&r' &acrate keys to you!";

    @Path(id = "crate.no-virtual-key")
    public static String CRATE_NO_VIRTUAL_KEY= "<prefix> &7» &cYou don't have any '&r<crate>&c' virtual key!";

    // Misc messages
    @Path(id = "misc.boolean.true")
    public static String BOOLEAN_TRUE_FORMAT = "&aYes";

    @Path(id = "misc.boolean.false")
    public static String BOOLEAN_FALSE_FORMAT = "&cNo";

    @Path(id = "misc.no-filters")
    public static String NO_FILTERS = "&cThere are no filters.";

    @Path(id = "misc.command.no-permission")
    public static String COMMAND_NO_PERMISSION = "&cI'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.";

    @Path(id = "misc.command.usage")
    public static String COMMAND_USAGE_FORMAT = "Usage: ";

    @Path(id = "misc.command.help")
    public static String COMMAND_HELP = "Unknown command type '/help' to get help.";

    @Path(id = "misc.command.only-console")
    public static String COMMAND_ONLY_CONSOLE = "&cOnly console can execute that command.";

    @Path(id = "misc.command.only-players")
    public static String COMMAND_ONLY_PLAYERS = "&cOnly players can execute that command.";

    public LanguageConfig(JavaPlugin plugin) {
        super(plugin, "language.yml");

        INSTANCE = this;
    }
}
