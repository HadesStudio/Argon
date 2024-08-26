package dev.astro.argon.crate.menu.editor;

import dev.astro.argon.config.SettingsConfig;
import dev.astro.argon.crate.Crate;
import dev.astro.argon.crate.menu.editor.button.CrateEditLootButton;
import dev.astro.argon.util.menu.Menu;
import dev.astro.argon.util.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CrateEditLootMenu extends Menu {

	private final Crate crate;

	{
		setEditable(true);
	}

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
