package dev.astro.argon.crate.menu.loot;

import dev.astro.argon.crate.Crate;
import dev.astro.argon.crate.menu.loot.button.CrateLootButton;
import dev.astro.argon.item.LootItem;
import dev.astro.argon.util.menu.Menu;
import dev.astro.argon.util.menu.button.Button;
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
