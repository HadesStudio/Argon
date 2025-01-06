package dev.astro.argon.crate.menu.button;

import com.cryptomorin.xseries.XSound;
import dev.astro.argon.config.LanguageConfig;
import dev.astro.argon.config.SettingsConfig;
import dev.astro.argon.crate.Crate;
import dev.astro.argon.profile.Profile;
import dev.astro.argon.util.ColorUtil;
import dev.astro.argon.util.ItemBuilder;
import dev.astro.argon.util.config.Replacement;
import dev.astro.argon.util.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * This code has been created by
 * gatogamer#6666 A.K.A. gatogamer.
 * If you want to use my code, please
 * ask first, and give me the credits.
 * Arigato! n.n
 */
@RequiredArgsConstructor
public class CrateOpenButton extends Button {
    private final Profile profile;
    private final Crate crate;
    private final int amount;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(this.crate.getOpener());

        builder.setName(this.crate.getColor() + this.crate.getName());

        List<String> lore = new ArrayList<>();
        LanguageConfig.CRATES_MENU_LORE.forEach(s -> {
            if (s.equals("<key_lore>")) {
                ItemStack key = this.crate.getKey();
                if (key != null && key.getItemMeta() != null && key.getItemMeta().getLore() != null) {
                    lore.addAll(key.getItemMeta().getLore());
                }
                return;
            }
            lore.add(s
                    .replace("<massive_open>", SettingsConfig.SHIFT_CLICK_TO_MASSIVE_OPEN_AMOUNT + "")
                    .replace("<amount>", amount + "")
            );
        });

        builder.setLore(lore);

        return builder.build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (SettingsConfig.SHIFT_CLICK_TO_MASSIVE_OPEN_ENABLED && clickType.isShiftClick()) {
            open(player, SettingsConfig.SHIFT_CLICK_TO_MASSIVE_OPEN_AMOUNT);
            return;
        }
        open(player, 1);
    }

    public void open(Player player, int amount) {
        if (this.profile.getVirtualKeys().containsKey(this.crate.getId())) {
            int count = this.profile.getVirtualKeys().get(this.crate.getId());
            if (count <= 0) {
                Replacement replacement = new Replacement(LanguageConfig.CRATE_NO_VIRTUAL_KEY);
                replacement.add("<prefix>", LanguageConfig.PREFIX);
                replacement.add("<crate>", this.crate.getName());

                player.sendMessage(ColorUtil.colorize(replacement.toString()));
            } else {
                for (int i = 0; i < Math.min(amount, count); i++) {

                    if (this.crate.giveLoot(player)) {
                        this.profile.removeVirtualKey(this.crate.getId(), 1);

                        player.updateInventory();
                        player.playSound(player.getLocation(), XSound.BLOCK_LEVER_CLICK.parseSound(), 20.0f, 15.0f);
                    }
                }
            }
        } else {
            Replacement replacement = new Replacement(LanguageConfig.CRATE_NO_VIRTUAL_KEY);
            replacement.add("<prefix>", LanguageConfig.PREFIX);
            replacement.add("<crate>", this.crate.getName());

            player.sendMessage(ColorUtil.colorize(replacement.toString()));
        }
    }
}
