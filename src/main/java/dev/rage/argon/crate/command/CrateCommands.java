package dev.rage.argon.crate.command;

import dev.rage.argon.ArgonPlugin;
import dev.rage.argon.config.LanguageConfig;
import dev.rage.argon.crate.Crate;
import dev.rage.argon.crate.menu.CrateViewAllMenu;
import dev.rage.argon.crate.menu.editor.CrateEditorMenu;
import dev.rage.argon.util.BukkitUtil;
import dev.rage.argon.util.ColorUtil;
import dev.rage.argon.util.command.annotation.Command;
import dev.rage.argon.util.command.annotation.Param;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrateCommands {

    @Command(names = { "crate create", "cr create"}, description = "Create a new crate", permission = "lootchests.command.crate.create")
    public static void create(Player player, @Param(name = "crate name") String name) {
        Crate crate = ArgonPlugin.get().getCrateHandler().getByName(name);
        if (crate != null) {
            player.sendMessage(ColorUtil.colorize(LanguageConfig.CRATE_ALREADY_CREATED.replace("<prefix>", LanguageConfig.PREFIX).replace("<crate>", crate.getName())));
            return;
        }

        crate = ArgonPlugin.get().getCrateHandler().createCrate(name);
        player.sendMessage(ColorUtil.colorize(LanguageConfig.CRATE_CREATED.replace("<prefix>", LanguageConfig.PREFIX).replace("<crate>", crate.getName())));
    }

    @Command(
            names = { "crate delete", "cr delete"},
            description = "Delete a crate",
            permission = "lootchests.command.crate.delete"
    )
    public static void delete(Player player, @Param(name = "crate name") Crate crate) {
        ArgonPlugin.get().getCrateHandler().deleteCrate(crate);
        player.sendMessage(ColorUtil.colorize(LanguageConfig.CRATE_DELETED.replace("<prefix>", LanguageConfig.PREFIX).replace("<crate>", crate.getName())));
    }

    @Command(
            names = { "crate crate", "cr crate"},
            description = "Get the crate",
            permission = "lootchests.command.crate.crate"
    )
    public static void crate(Player player, @Param(name = "crate name") Crate crate) {
        ArgonPlugin.get().getCrateHandler().giveCrate(player, crate);
        player.sendMessage(ColorUtil.colorize(LanguageConfig.CRATE_GIVEN_CRATE.replace("<prefix>", LanguageConfig.PREFIX).replace("<crate>", crate.getName())));
    }

    @Command(
            names = { "crate list", "cr list"},
            description = "Show the list of crates",
            permission = "lootchests.command.crate.list"
    )
    public static void list(Player player) {
        new CrateViewAllMenu().openMenu(player);
    }

    @Command(
            names = { "crate edit", "cr edit" },
            description = "Edit a crate",
            permission = "lootchests.command.crate.edit"
    )
    public static void edit(Player player, @Param(name = "crate name") Crate crate) {
        new CrateEditorMenu(crate).openMenu(player);
    }

    @Command(
            names = { "crate givekey", "cr givekey" },
            description = "Give the crate key",
            permission = "lootchests.command.crate.givekey"
    )
    public static void givekey(CommandSender sender, @Param(name = "crate name") Crate crate, @Param(name = "target") Player target, @Param(name = "amount") int amount) {
        ArgonPlugin.get().getCrateHandler().giveKey(target, crate, amount);
    }

    @Command(
            names = { "crate givekeyall", "cr givekeyall" },
            description = "Give the crate key to all players",
            permission = "lootchests.command.crate.givekeyall"
    )
    public static void givekeyall(CommandSender sender, @Param(name = "crate name") Crate crate, @Param(name = "amount") int amount) {
        BukkitUtil.getOnlinePlayers().forEach(online -> ArgonPlugin.get().getCrateHandler().giveKey(online, crate, amount));
    }

    @Command(
            names = { "crate givevirtualkey", "cr givevirtualkey" },
            description = "Give the crate virtual key",
            permission = "lootchests.command.crate.givevirtualkey"
    )
    public static void givevirtualkey(CommandSender sender, @Param(name = "crate name") Crate crate, @Param(name = "target") Player target, @Param(name = "amount") int amount) {
        ArgonPlugin.get().getCrateHandler().giveVirtualKey(target, crate, amount);
    }

    @Command(
            names = { "crate givevirtualkeyall", "cr givevirtualkeyall" },
            description = "Give the crate virtual key to all players",
            permission = "lootchests.command.crate.givevirtualkey all"
    )
    public static void givevirtualkeyall(CommandSender sender, @Param(name = "crate name") Crate crate, @Param(name = "amount") int amount) {
        BukkitUtil.getOnlinePlayers().forEach(online -> ArgonPlugin.get().getCrateHandler().giveVirtualKey(online, crate, amount));
    }
}
