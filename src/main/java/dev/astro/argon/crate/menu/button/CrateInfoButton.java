package dev.astro.argon.crate.menu.button;

import dev.astro.argon.config.LanguageConfig;
import dev.astro.argon.config.SettingsConfig;
import dev.astro.argon.crate.Crate;
import dev.astro.argon.crate.menu.editor.CrateEditorMenu;
import dev.astro.argon.crate.menu.loot.CrateLootMenu;
import dev.astro.argon.util.ItemBuilder;
import dev.astro.argon.util.TextUtil;
import dev.astro.argon.util.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CrateInfoButton extends Button {

    private final Crate crate;
    private final boolean editable;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(crate.getOpener());

        builder.setName(crate.getColor() + crate.getName());

        List<String> lore = new ArrayList<>();

        lore.add(" ");
        lore.add(" " + SettingsConfig.SECONDARY_COLOR + "Color&7: &f" + TextUtil.formatPretty(crate.getColor().name()));
        lore.add(" " + SettingsConfig.SECONDARY_COLOR + "Opener&7: &f" + TextUtil.formatPretty(crate.getOpener().getType().name()));
        lore.add(" " + SettingsConfig.SECONDARY_COLOR + "Key&7: &f" + TextUtil.formatPretty(crate.getKey().getType().name()));
        lore.add(" ");
        lore.add(" " + SettingsConfig.SECONDARY_COLOR + "Virtual Keys&7: &f" + (crate.isVirtualKeys() ? LanguageConfig.BOOLEAN_TRUE_FORMAT : LanguageConfig.BOOLEAN_FALSE_FORMAT));
        lore.add(" " + SettingsConfig.SECONDARY_COLOR + "Open from Key&7: &f" + (crate.isOpenFromKey() ? LanguageConfig.BOOLEAN_TRUE_FORMAT : LanguageConfig.BOOLEAN_FALSE_FORMAT));
        lore.add(" ");
        lore.add(" " + SettingsConfig.SECONDARY_COLOR + "Locations&7: &f" + crate.getLocations().size());
        lore.add(" " + SettingsConfig.SECONDARY_COLOR + "Loot&7: &f" + crate.getLootItems().size());
        if (editable) {
            lore.add(" ");
            lore.add(" &eLeft click here to view loot");
            lore.add(" &eRight click here to edit");
        }
        lore.add(" ");

        builder.setLore(lore);

        return builder.build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (editable) {
            if (clickType.isLeftClick()) {
                new CrateLootMenu(crate).openMenu(player);
            }

            if (clickType.isRightClick()) {
                new CrateEditorMenu(crate).openMenu(player);
            }
        }
    }
}
