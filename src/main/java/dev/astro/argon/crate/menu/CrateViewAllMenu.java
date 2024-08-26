package dev.astro.argon.crate.menu;

import dev.astro.argon.ArgonPlugin;
import dev.astro.argon.crate.Crate;
import dev.astro.argon.crate.menu.button.CrateInfoButton;
import dev.astro.argon.util.menu.button.Button;
import dev.astro.argon.util.menu.pagination.PaginatedMenu;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrateViewAllMenu extends PaginatedMenu {

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Crates";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<Crate> crates = ArgonPlugin.get().getCrateHandler().getCrates();
        crates.sort(Comparator.comparing(Crate::getName));

        for (Crate crate : crates) {
            buttons.put(buttons.size(), new CrateInfoButton(crate, true));
        }

        return buttons;
    }
}
