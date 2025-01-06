package dev.astro.argon.crate.menu.crates;

import dev.astro.argon.ArgonPlugin;
import dev.astro.argon.config.LanguageConfig;
import dev.astro.argon.crate.CrateHandler;
import dev.astro.argon.crate.menu.button.CrateOpenButton;
import dev.astro.argon.profile.Profile;
import dev.astro.argon.util.menu.button.Button;
import dev.astro.argon.util.menu.pagination.PaginatedMenu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This code has been created by
 * gatogamer#6666 A.K.A. gatogamer.
 * If you want to use my code, please
 * ask first, and give me the credits.
 * Arigato! n.n
 */
public class CratesMenu extends PaginatedMenu {

    @Override
    public String getPrePaginatedTitle(Player player) {
        return LanguageConfig.CRATES_MENU_TITLE;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Profile profile = ArgonPlugin.get().getProfileHandler().getProfile(player);

        Map<Integer, Button> buttons = new HashMap<>();
        CrateHandler crateHandler = ArgonPlugin.get().getCrateHandler();
        AtomicInteger slot = new AtomicInteger();

        crateHandler.getCrates().forEach(crate -> profile.getVirtualKeys().forEach((key, amount) -> {
            if (crate.getId().equals(key)) {
                buttons.put(slot.getAndIncrement(), new CrateOpenButton(profile, crate, amount));
            }
        }));

        return buttons;
    }
}
