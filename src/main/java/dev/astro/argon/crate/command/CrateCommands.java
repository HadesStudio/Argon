package dev.astro.argon.crate.command;

import dev.astro.argon.crate.menu.CrateViewAllMenu;
import dev.astro.argon.crate.menu.editor.CrateEditorMenu;
import dev.astro.argon.ArgonPlugin;
import dev.astro.argon.config.LanguageConfig;
import dev.astro.argon.crate.Crate;
import dev.astro.argon.util.BukkitUtil;
import dev.astro.argon.util.ColorUtil;
import dev.astro.argon.util.command.annotation.Command;
import dev.astro.argon.util.command.annotation.Param;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrateCommands {

    @Command(
            names = { "crate create", "cr create"},
            description = "Create a new crate",
            permission = "argon.command.crate.create"
    )
    public void create(Player player, @Param(name = "crate name") String name) {
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
            permission = "argon.command.crate.delete"
    )
    public void delete(Player player, @Param(name = "crate name") Crate crate) {
        ArgonPlugin.get().getCrateHandler().deleteCrate(crate);
        player.sendMessage(ColorUtil.colorize(LanguageConfig.CRATE_DELETED.replace("<prefix>", LanguageConfig.PREFIX).replace("<crate>", crate.getName())));
    }

    @Command(
            names = { "crate crate", "cr crate"},
            description = "Get the crate",
            permission = "argon.command.crate.crate"
    )
    public void crate(Player player, @Param(name = "crate name") Crate crate) {
        ArgonPlugin.get().getCrateHandler().giveCrate(player, crate);
        player.sendMessage(ColorUtil.colorize(LanguageConfig.CRATE_GIVEN_CRATE.replace("<prefix>", LanguageConfig.PREFIX).replace("<crate>", crate.getName())));
    }

    @Command(
            names = { "crate list", "cr list"},
            description = "Show the list of crates",
            permission = "argon.command.crate.list"
    )
    public void list(Player player) {
        new CrateViewAllMenu().openMenu(player);
    }

    @Command(
            names = { "crate edit", "cr edit" },
            description = "Edit a crate",
            permission = "argon.command.crate.edit"
    )
    public void edit(Player player, @Param(name = "crate name") Crate crate) {
        new CrateEditorMenu(crate).openMenu(player);
    }

    @Command(
            names = { "crate givekey", "cr givekey" },
            description = "Give the crate key",
            permission = "argon.command.crate.givekey"
    )
    public void givekey(CommandSender sender, @Param(name = "crate name") Crate crate, @Param(name = "target") Player target, @Param(name = "amount") int amount) {
        ArgonPlugin.get().getCrateHandler().giveKey(target, crate, amount);
    }

    @Command(
            names = { "crate givekeyall", "cr givekeyall" },
            description = "Give the crate key to all players",
            permission = "argon.command.crate.givekeyall"
    )
    public void givekeyall(CommandSender sender, @Param(name = "crate name") Crate crate, @Param(name = "amount") int amount) {
        BukkitUtil.getOnlinePlayers().forEach(online -> ArgonPlugin.get().getCrateHandler().giveKey(online, crate, amount));
    }

    @Command(
            names = { "crate givevirtualkey", "cr givevirtualkey" },
            description = "Give the crate virtual key",
            permission = "argon.command.crate.givevirtualkey"
    )
    public void givevirtualkey(CommandSender sender, @Param(name = "crate name") Crate crate, @Param(name = "target") Player target, @Param(name = "amount") int amount) {
        ArgonPlugin.get().getCrateHandler().giveVirtualKey(target, crate, amount);
    }

    @Command(
            names = { "crate givevirtualkeyall", "cr givevirtualkeyall" },
            description = "Give the crate virtual key to all players",
            permission = "argon.command.crate.givevirtualkeyall"
    )
    public void givevirtualkeyall(CommandSender sender, @Param(name = "crate name") Crate crate, @Param(name = "amount") int amount) {
        BukkitUtil.getOnlinePlayers().forEach(online -> ArgonPlugin.get().getCrateHandler().giveVirtualKey(online, crate, amount));
    }
}
