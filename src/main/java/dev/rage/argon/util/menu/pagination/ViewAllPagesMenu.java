package dev.rage.argon.util.menu.pagination;

import dev.rage.argon.config.MenusConfig;
import dev.rage.argon.util.menu.Menu;
import dev.rage.argon.util.menu.button.Button;
import dev.rage.argon.util.menu.button.impl.BackButton;
import dev.rage.argon.util.menu.pagination.button.JumpToPageButton;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter @AllArgsConstructor
public class ViewAllPagesMenu extends Menu {

	@NonNull public PaginatedMenu menu;

	@Override
	public String getTitle(Player player) {
		return MenusConfig.PAGES_MENU_TITLE;
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		HashMap<Integer, Button> buttons = new HashMap<>();

		buttons.put(0, new BackButton(menu));

		int index = 10;

		for (int i = 1; i <= menu.getPages(player); i++) {
			buttons.put(index++, new JumpToPageButton(i, menu, menu.getPage() == i));

			if ((index - 8) % 9 == 0) {
				index += 2;
			}
		}

		return buttons;
	}

	@Override
	public boolean isAutoUpdate() {
		return true;
	}

}
