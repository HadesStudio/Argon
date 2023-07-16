package dev.rage.argon.crate.menu.loot;

import dev.rage.argon.crate.Crate;
import dev.rage.argon.crate.menu.loot.button.CrateLootButton;
import dev.rage.argon.item.LootItem;
import dev.rage.argon.util.menu.Menu;
import dev.rage.argon.util.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CrateLootMenu extends Menu {

	private final Crate crate;
	
	@Override
	public String getTitle(Player player) {
		return crate.getColor() + crate.getName() + "&r Loot";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		for (LootItem item : crate.getLootItems()) {
			buttons.put(item.getPosition(), new CrateLootButton(item));
		}
		
		return buttons;
	}
}
