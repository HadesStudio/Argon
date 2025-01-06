package dev.astro.argon.crate.menu.editor;

import dev.astro.argon.ArgonPlugin;
import dev.astro.argon.crate.Crate;
import dev.astro.argon.crate.menu.editor.button.CrateAnimationButton;
import dev.astro.argon.util.menu.button.Button;
import dev.astro.argon.util.menu.pagination.PaginatedMenu;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CrateAnimationsMenu extends PaginatedMenu {
    private final Crate crate;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&8Animations";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        AtomicInteger slots = new AtomicInteger();
        ArgonPlugin.get().getAnimationManager().getAnimations().forEach((s, animation) -> {
            buttons.put(slots.getAndIncrement(), new CrateAnimationButton(this.crate, animation));
        });

        return buttons;
    }
}
