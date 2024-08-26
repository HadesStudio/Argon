package dev.astro.argon.crate.menu.editor.button;

import dev.astro.argon.crate.Crate;
import dev.astro.argon.crate.menu.editor.CrateEditorMenu;
import dev.astro.argon.crate.prompt.CrateEditStringPrompt;
import dev.astro.argon.util.ColorUtil;
import dev.astro.argon.util.ItemBuilder;
import dev.astro.argon.util.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CrateHologramButton extends Button {

    private final Crate crate;
    private final CrateEditorMenu menu;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(Material.BOOK);

        builder.setName("&bHologram Lines");

        List<String> lore = new ArrayList<>();

        if (crate.getHologramLines().isEmpty()) {
            lore.add(" &cEmpty");
        } else {
            lore.add(" ");
            for (int i = 0; i < crate.getHologramLines().size(); i++) {
                lore.add((menu.selectedHologramLine == i ? "&a&m->&r" : "") + " &7" + (i + 1) + ".- &r" + crate.getHologramLines().get(i));
            }
        }

        lore.add(" ");
        lore.add(" &7» &eLeft click to scroll lines &7«");
        lore.add(" &7» &eMiddle click to remove selected line &7«");
        lore.add(" &7» &eRight click to add new line &7«");
        lore.add(" ");

        builder.setLore(lore);

        return builder.build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType.isLeftClick()) {
            if (crate.getHologramLines().isEmpty()) return;

            if (crate.getHologramLines().size() == (menu.selectedHologramLine + 1)) {
                menu.selectedHologramLine = 0;
            } else {
                ++menu.selectedHologramLine;
            }
        } else if (clickType.isRightClick()) {
            player.closeInventory();

            new CrateEditStringPrompt(crate, null, (str) -> {

                crate.getHologramLines().add(str);

                player.sendMessage(ColorUtil.colorize("&aYou have added the hologram line '&r" + str + "&a' from npc '&r" + crate.getName() + "&a'!"));

            }).startPrompt(player);

            player.sendMessage(ColorUtil.colorize("&aWrite new hologram line for '&r" + crate.getName() + "&a' npc..."));
        } else if (clickType.isCreativeAction()) {
            if (crate.getHologramLines().isEmpty()) return;

            crate.getHologramLines().remove(menu.selectedHologramLine);

            menu.selectedHologramLine = 0;
        }
    }
}
