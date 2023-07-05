package dev.rage.lootchests.crate.menu.editor;

import dev.rage.lootchests.config.SettingsConfig;
import dev.rage.lootchests.crate.Crate;
import dev.rage.lootchests.crate.menu.editor.button.CrateEditLootButton;
import dev.rage.lootchests.util.menu.Menu;
import dev.rage.lootchests.util.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CrateEditLootMenu extends Menu {

	private final Crate crate;
	
	@Override
	public String getTitle(Player player) {
		return crate.getColor() + crate.getName() + SettingsConfig.SECONDARY_COLOR + " Loot";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		crate.getLootItems().forEach(item -> {
			buttons.put(item.getPosition(), new CrateEditLootButton(crate, item));
		});
		
		return buttons;
	}
}
