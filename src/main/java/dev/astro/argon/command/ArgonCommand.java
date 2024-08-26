package dev.astro.argon.command;

import dev.astro.argon.ArgonPlugin;
import dev.astro.argon.box.editor.CommonBoxEditor;
import dev.astro.argon.box.editor.LuckyBlockEditor;
import dev.astro.argon.box.editor.MysteryBoxEditor;
import dev.astro.argon.box.editor.MysteryBoxOpEditor;
import dev.astro.argon.config.LanguageConfig;
import dev.astro.argon.config.MenusConfig;
import dev.astro.argon.config.SettingsConfig;
import dev.astro.argon.util.ColorUtil;
import dev.astro.argon.util.ReflectionUtil;
import dev.astro.argon.util.command.annotation.Command;
import dev.astro.argon.util.command.annotation.Param;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArgonCommand {

    @Command(names = "argon reload", permission = "argon.command.reload", description = "Reloads all config files")
    public void reload(CommandSender sender) {
        LanguageConfig.INSTANCE.reload();
        MenusConfig.INSTANCE.reload();
        SettingsConfig.INSTANCE.reload();
    }

    @Command(names = "argon checkversion", permission = "argon.command.checkspigot", description = "Checks spigot NMS version")
    public void checkversion(CommandSender sender) {
        sender.sendMessage(ColorUtil.colorize("&aArgon is running for: &r" + ReflectionUtil.getServerVersion().getName()));
    }

    @Command(names = "lootbox give luckyblock", permission = "argon.command.lootbox.give.luckyblock", description = "Gives a lucky block")
    public void giveluckyblock(CommandSender sender, @Param(name = "target") Player target, @Param(name = "amount", defaultValue = "1") int amount) {
        if (ArgonPlugin.get().getBoxHandler().giveLuckyBlock(target, amount)) {
            sender.sendMessage("Added a lucky block");
        }
    }

    @Command(names = "lootbox give common", permission = "argon.command.lootbox.give.common", description = "Gives a common loot box")
    public void givelootbox(CommandSender sender, @Param(name = "target") Player target, @Param(name = "amount", defaultValue = "1") int amount) {
        if (ArgonPlugin.get().getBoxHandler().giveLootBox(target, amount)) {
            sender.sendMessage("Added a common loot box");
        }
    }

    @Command(names = "lootbox give mystery", permission = "argon.command.lootbox.give.mystery", description = "Gives a mystery box")
    public void givemysterybox(CommandSender sender, @Param(name = "target") Player target, @Param(name = "amount", defaultValue = "1") int amount) {
        if (ArgonPlugin.get().getBoxHandler().giveMysteryBox(target, amount)) {
            sender.sendMessage("Added a mystery box");
        }
    }

    @Command(names = "lootbox edit luckyblock", permission = "argon.command.lootbox.edit.luckyblock", description = "Edit a lucky block")
    public void editluckyblock(Player player) {
        new LuckyBlockEditor(player, ArgonPlugin.get().getBoxHandler().getLuckyBlock());
    }

    @Command(names = "lootbox edit common", permission = "argon.command.lootbox.edit.common", description = "Edit a common box")
    public void editcommon(Player player) {
        new CommonBoxEditor(player, ArgonPlugin.get().getBoxHandler().getLootBox());
    }

    @Command(names = "lootbox edit mysterycommon", permission = "argon.command.lootbox.edit.mystery", description = "Edit a mystery box common loot")
    public void editmysterycommon(Player player) {
        new MysteryBoxEditor(player, ArgonPlugin.get().getBoxHandler().getMysteryBoxCommon());
    }

    @Command(names = "lootbox edit mysteryop", permission = "argon.command.lootbox.edit.mystery", description = "Edit a mystery box op loot")
    public void editmysteryop(Player player) {
        new MysteryBoxOpEditor(player, ArgonPlugin.get().getBoxHandler().getMysteryBoxOp());
    }
}
