package dev.astro.argon.crate.command;

import dev.astro.argon.crate.menu.CrateViewAllMenu;
import dev.astro.argon.crate.menu.editor.CrateEditorMenu;
import dev.astro.argon.ArgonPlugin;
import dev.astro.argon.config.LanguageConfig;
import dev.astro.argon.crate.Crate;
import dev.astro.argon.npc.NPCProfile;
import dev.astro.argon.util.BukkitUtil;
import dev.astro.argon.util.ColorUtil;
import dev.astro.argon.util.command.annotation.Command;
import dev.astro.argon.util.command.annotation.Param;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrateCommands {

    @Command(
            names = {"crate create", "cr create"},
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
            names = {"crate delete", "cr delete"},
            description = "Delete a crate",
            permission = "argon.command.crate.delete"
    )
    public void delete(Player player, @Param(name = "crate name") Crate crate) {
        ArgonPlugin.get().getCrateHandler().deleteCrate(crate);
        player.sendMessage(ColorUtil.colorize(LanguageConfig.CRATE_DELETED.replace("<prefix>", LanguageConfig.PREFIX).replace("<crate>", crate.getName())));
    }

    @Command(
            names = {"crate crate", "cr crate"},
            description = "Get the crate",
            permission = "argon.command.crate.crate"
    )
    public void crate(Player player, @Param(name = "crate name") Crate crate) {
        ArgonPlugin.get().getCrateHandler().giveCrate(player, crate);
        player.sendMessage(ColorUtil.colorize(LanguageConfig.CRATE_GIVEN_CRATE.replace("<prefix>", LanguageConfig.PREFIX).replace("<crate>", crate.getName())));
    }

    @Command(
            names = {"crate list", "cr list"},
            description = "Show the list of crates",
            permission = "argon.command.crate.list"
    )
    public void list(Player player) {
        new CrateViewAllMenu().openMenu(player);
    }

    @Command(
            names = {"crate edit", "cr edit"},
            description = "Edit a crate",
            permission = "argon.command.crate.edit"
    )
    public void edit(Player player, @Param(name = "crate name") Crate crate) {
        new CrateEditorMenu(crate).openMenu(player);
    }

    @Command(
            names = {"crate givekey", "cr givekey"},
            description = "Give the crate key",
            permission = "argon.command.crate.givekey"
    )
    public void givekey(CommandSender sender, @Param(name = "crate name") Crate crate, @Param(name = "target") Player target, @Param(name = "amount") int amount) {
        ArgonPlugin.get().getCrateHandler().giveKey(target, crate, amount);
    }

    @Command(
            names = {"crate givekeyall", "cr givekeyall"},
            description = "Give the crate key to all players",
            permission = "argon.command.crate.givekeyall"
    )
    public void givekeyall(CommandSender sender, @Param(name = "crate name") Crate crate, @Param(name = "amount") int amount) {
        BukkitUtil.getOnlinePlayers().forEach(online -> ArgonPlugin.get().getCrateHandler().giveKey(online, crate, amount));
    }

    @Command(
            names = {"crate givevirtualkey", "cr givevirtualkey"},
            description = "Give the crate virtual key",
            permission = "argon.command.crate.givevirtualkey"
    )
    public void givevirtualkey(CommandSender sender, @Param(name = "crate name") Crate crate, @Param(name = "target") Player target, @Param(name = "amount") int amount) {
        ArgonPlugin.get().getCrateHandler().giveVirtualKey(target, crate, amount);
    }

    @Command(
            names = {"crate givevirtualkeyall", "cr givevirtualkeyall"},
            description = "Give the crate virtual key to all players",
            permission = "argon.command.crate.givevirtualkeyall"
    )
    public void givevirtualkeyall(CommandSender sender, @Param(name = "crate name") Crate crate, @Param(name = "amount") int amount) {
        BukkitUtil.getOnlinePlayers().forEach(online -> ArgonPlugin.get().getCrateHandler().giveVirtualKey(online, crate, amount));
    }

    @Command(
            names = {"crate-npc create", "cr-npc create"}
    )
    public void npc$create(Player player, @Param(name = "npc name") String npcName, @Param(name = "crate name") Crate crate) {
        ArgonPlugin.get().getNpcRegistrar().createNPC(npcName, crate.getName(), player.getLocation());
    }

    @Command(
            names = {"crate-npc delete", "cr-npc delete"}
    )
    public void npc$delete(Player player, @Param(name = "npc name") String npcName) {
        ArgonPlugin.get().getNpcRegistrar().deleteNPC(npcName);
    }

    @Command(
            names = {"crate-npc skin", "cr-npc skin"}
    )
    public void npc$skin(Player player, @Param(name = "npc name") String npcName, @Param(name = "skin name") String skinName) {
        NPCProfile npcProfile = ArgonPlugin.get().getNpcRegistrar().getProfiles().get(npcName);
        if (npcProfile == null) {
            player.sendMessage(ColorUtil.colorize(LanguageConfig.NPC_NOT_FOUND.replace("<prefix>", LanguageConfig.PREFIX).replace("<npc>", npcName)));
            return;
        }
        npcProfile.setSkinName(skinName);
        npcProfile.flushToConfig();
        npcProfile.createNPC();
    }

    @Command(
            names = {"crate-npc hidename", "cr-npc hidename"}
    )
    public void npc$hidename(Player player, @Param(name = "npc name") String npcName) {
        NPCProfile npcProfile = ArgonPlugin.get().getNpcRegistrar().getProfiles().get(npcName);
        if (npcProfile == null) {
            player.sendMessage(ColorUtil.colorize(LanguageConfig.NPC_NOT_FOUND.replace("<prefix>", LanguageConfig.PREFIX).replace("<npc>", npcName)));
            return;
        }
        npcProfile.setHideName(!npcProfile.isHideName());
        npcProfile.flushToConfig();
        npcProfile.createNPC();
    }
    @Command(
            names = {"crate-npc animation", "cr-npc animation"}
    )
    public void npc$animation(Player player, @Param(name = "npc name") String npcName, @Param(name = "animation") String animation) {
        NPCProfile npcProfile = ArgonPlugin.get().getNpcRegistrar().getProfiles().get(npcName);
        if (npcProfile == null) {
            player.sendMessage(ColorUtil.colorize(LanguageConfig.NPC_NOT_FOUND.replace("<prefix>", LanguageConfig.PREFIX).replace("<npc>", npcName)));
            return;
        }
        npcProfile.setAnimation(animation);
        npcProfile.flushToConfig();
    }
}
