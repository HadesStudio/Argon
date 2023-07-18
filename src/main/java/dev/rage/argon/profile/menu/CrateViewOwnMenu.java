package dev.rage.argon.profile.menu;

import dev.rage.argon.Argon;
import dev.rage.argon.command.ArgonCommand;
import dev.rage.argon.config.LanguageConfig;
import dev.rage.argon.config.SettingsConfig;
import dev.rage.argon.crate.Crate;
import dev.rage.argon.profile.Profile;
import dev.rage.argon.util.ColorUtil;
import dev.rage.argon.util.InventoryUtil;
import dev.rage.argon.util.ItemBuilder;
import dev.rage.argon.util.config.Replacement;
import dev.rage.argon.util.menu.Menu;
import dev.rage.argon.util.menu.button.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrateViewOwnMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "Your Crates";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Profile profile = Argon.get().getProfileHandler().getProfile(player);

        buttons.put(3, new Button() {

            @Override
            public ItemStack getButtonItem(Player player) {
                ItemBuilder builder = new ItemBuilder(Material.CHEST);

                builder.setName(SettingsConfig.SECONDARY_COLOR + "Your Stored Keys");

                List<String> lore = new ArrayList<>();

                lore.add(" ");
                if (profile.getStoredKeys().keySet().isEmpty()) {
                    lore.add(" &cEmpty");
                } else {
                    profile.getStoredKeys().keySet().stream().filter(id -> Argon.get().getCrateHandler().getById(id) != null).forEach(id -> {
                        Crate crate = Argon.get().getCrateHandler().getById(id);

                        lore.add(" &f" + crate.getColor() + crate.getName() + "&7: &fx" + profile.getStoredKeys().get(id));
                    });
                    lore.add(" ");
                    lore.add(" &eClick here to claim keys");
                }
                lore.add(" ");

                builder.setLore(lore);

                return builder.build();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                if (InventoryUtil.isFull(player)) {
                    Replacement replacement = new Replacement(LanguageConfig.INVENTORY_FULL);
                    replacement.add("<prefix>", LanguageConfig.PREFIX);

                    player.sendMessage(ColorUtil.colorize(replacement.toString()));
                    return;
                }
                profile.getStoredKeys().keySet().stream().filter(id -> Argon.get().getCrateHandler().getById(id) != null).forEach(id -> {
                    Crate crate = Argon.get().getCrateHandler().getById(id);

                    if (crate.isVirtualKeys()) {
                        Argon.get().getCrateHandler().giveVirtualKey(player, crate, profile.getStoredKeys().get(id));
                    } else {
                        Argon.get().getCrateHandler().giveKey(player, crate, profile.getStoredKeys().get(id));
                    }
                });
            }

        });

        buttons.put(5, new Button() {

            @Override
            public ItemStack getButtonItem(Player player) {
                ItemBuilder builder = new ItemBuilder(Material.DIAMOND);

                builder.setName(SettingsConfig.SECONDARY_COLOR + "Your Virtual Keys");

                List<String> lore = new ArrayList<>();

                lore.add(" ");
                if (profile.getVirtualKeys().keySet().isEmpty()) {
                    lore.add(" &cEmpty");
                } else {
                    profile.getVirtualKeys().keySet().stream().filter(id -> Argon.get().getCrateHandler().getById(id) != null).forEach(id -> {
                        Crate crate = Argon.get().getCrateHandler().getById(id);

                        lore.add(" &f" + crate.getColor() + crate.getName() + "&7: &fx" + profile.getVirtualKeys().get(id));
                    });
                }
                lore.add(" ");

                builder.setLore(lore);

                return builder.build();
            }
        });

        return buttons;
    }
}
