package dev.rage.lootchests.crate.menu.editor.button;

import dev.rage.lootchests.config.SettingsConfig;
import dev.rage.lootchests.crate.Crate;
import dev.rage.lootchests.crate.menu.editor.CrateEditItemMenu;
import dev.rage.lootchests.crate.prompt.CrateEditStringPrompt;
import dev.rage.lootchests.item.LootItem;
import dev.rage.lootchests.util.ColorUtil;
import dev.rage.lootchests.util.ItemBuilder;
import dev.rage.lootchests.util.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CrateItemCommandsButton extends Button {

    private final Crate crate;
    private final LootItem item;
    private final CrateEditItemMenu menu;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(Material.COMMAND);

        builder.setName(SettingsConfig.SECONDARY_COLOR + "Current Commands");

        List<String> lore = new ArrayList<>();

        lore.add(" ");
        lore.add(SettingsConfig.SECONDARY_COLOR + " Placeholders&7:");
        lore.add(" &7- &f<player>");
        lore.add(" &7- &f<item-amount>");
        lore.add(" &7- &f<item-name>");
        lore.add(" ");

        if (item.getCommands().isEmpty()) {
            lore.add(" &cEmpty");
        } else {
            for (int i = 0; i < item.getCommands().size(); i++) {
                lore.add((menu.selectedCommand == i ? "&a&m->&r" : "") + " &7" + (i + 1) + ".- &r/" + item.getCommands().get(i));
            }
        }

        lore.add(" ");
        lore.add(" &7» &eLeft click to scroll commands &7«");
        lore.add(" &7» &eMiddle click to remove selected command &7«");
        lore.add(" &7» &eRight click to add new command &7«");
        lore.add(" ");

        builder.setLore(lore);

        return builder.build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType.isLeftClick()) {
            if (item.getCommands().isEmpty()) return;

            if (item.getCommands().size() == (menu.selectedCommand + 1)) {
                menu.selectedCommand = 0;
            } else {
                ++menu.selectedCommand;
            }
        } else if (clickType.isRightClick()) {
            player.closeInventory();

            new CrateEditStringPrompt(crate, item, (string) -> {

                item.getCommands().add(string);

                player.sendMessage(ColorUtil.colorize("&aYou have added the command '&r/" + string + "&a' to item '&r" + item.getItemStack().getType() + "&a'!"));

            }).startPrompt(player);

            player.sendMessage(ColorUtil.colorize("&aWrite new command for '&r" + item.getItemStack().getType() + "&a' item..."));
        } else if (clickType.isCreativeAction()) {
            if (item.getCommands().isEmpty()) return;

            item.getCommands().remove(menu.selectedCommand);

            menu.selectedCommand = 0;
        }
    }
}
