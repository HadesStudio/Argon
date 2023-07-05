package dev.rage.lootchests.crate.menu.loot;

import dev.rage.lootchests.crate.Crate;
import dev.rage.lootchests.crate.menu.loot.button.CrateLootButton;
import dev.rage.lootchests.item.LootItem;
import dev.rage.lootchests.util.menu.Menu;
import dev.rage.lootchests.util.menu.button.Button;
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
