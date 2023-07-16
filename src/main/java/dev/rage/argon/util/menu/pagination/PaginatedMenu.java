package dev.rage.argon.util.menu.pagination;

import com.google.common.collect.Maps;
import dev.rage.argon.util.menu.Menu;
import dev.rage.argon.util.menu.button.Button;
import dev.rage.argon.util.menu.pagination.button.PageButton;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class PaginatedMenu extends Menu {

    @Getter private int page = 1;

    @Override
    public String getTitle(Player player) {
		return (getPrePaginatedTitle(player) + " [" + page + '/' + getPages(player) + "]");
    }

    /**
     * Changes the page number
     *
     * @param player player viewing the inventory
     * @param mod    delta to modify the page number by
     */
    public final void modPage(Player player, int mod) {
        page += mod;
        getButtons().clear();
        openMenu(player);
    }

    /**
     * @param player player viewing the inventory
     * @return the number of pages
     */
    public final int getPages(Player player) {
        int buttonAmount = getAllPagesButtons(player).size();

        if (buttonAmount == 0) {
            return 1;
        }

        return (int) Math.ceil(buttonAmount / (double) 21);
    }

    @Override
    public final Map<Integer, Button> getButtons(Player player) {

        int minIndex = (int) ((double) (page - 1) * 21) + 1;
        int maxIndex = (int) ((double) (page) * 21);

        HashMap<Integer, Button> buttons = Maps.newHashMap();


        for (int i = 0; i < 9; i++) {
			buttons.put(getSlot(i, 0), getPlaceholderButton());
			buttons.put(getSlot(i, (27 / 9) + 1), getPlaceholderButton());
		}
        
        for (int i = 0; i < 5; i++) {
			buttons.put(getSlot(0, i), getPlaceholderButton());
			buttons.put(getSlot(8, i), getPlaceholderButton());
		}

        
        if (page > 1) {
        	buttons.put(getSlot(0, 2), new PageButton(-1, this));
        }
        
		if (page < getPages(player)) {
			buttons.put(getSlot(8, 2), new PageButton(1, this));
		}
        
		
		int index = 1, added = 0, currentSlot = 0;
		for (Map.Entry<Integer, Button> entry : this.getAllPagesButtons(player).entrySet()) {
			int current = index++;

            switch (currentSlot) {
                case 16:
                case 25: {
                    index += 2;
                    added += 2;
                    current += 2;
                    break;
                }
            }

            if (current >= minIndex && current <= maxIndex + added) {
                current -= (int) ((double) (21) * (page - 1)) - 9;
                currentSlot = current;

                buttons.put(current, entry.getValue());
            }
		}

        Map<Integer, Button> global = getGlobalButtons(player);

        if (global != null) {
            for (Map.Entry<Integer, Button> gent : global.entrySet()) {
                buttons.put(gent.getKey(), gent.getValue());
            }
        }


        return buttons;
    }

    /**
     * @param player player viewing the inventory
     * @return a Map of buttons that returns items which will be present on all pages
     */
    public Map<Integer, Button> getGlobalButtons(Player player) {
        return null;
    }

    /**
     * @param player player viewing the inventory
     * @return title of the inventory before the page number is added
     */
    public abstract String getPrePaginatedTitle(Player player);

    /**
     * @param player player viewing the inventory
     * @return a map of buttons that will be paginated and spread across pages
     */
    public abstract Map<Integer, Button> getAllPagesButtons(Player player);

    @Override
    public int getSize() {
        return 9 * 5;
    }
}
