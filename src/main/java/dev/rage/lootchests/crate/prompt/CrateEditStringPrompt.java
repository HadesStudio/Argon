package dev.rage.lootchests.crate.prompt;

import dev.rage.lootchests.crate.Crate;
import dev.rage.lootchests.crate.menu.editor.CrateEditItemMenu;
import dev.rage.lootchests.crate.menu.editor.CrateEditorMenu;
import dev.rage.lootchests.item.LootItem;
import dev.rage.lootchests.util.Callback;
import dev.rage.lootchests.util.prompt.Prompt;
import dev.rage.lootchests.util.prompt.defaults.StringPrompt;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class CrateEditStringPrompt extends StringPrompt {

	private final Crate crate;
	private final LootItem item;
	private final Callback<String> callback;
	
	@Override
	public void handleBegin(Player player) {
		player.closeInventory();
	}

	@Override
	public void handleCancel(Player player) {
		new CrateEditorMenu(crate).openMenu(player);
	}

	@Override
	public Prompt<?> acceptInput(Player player, String value) {
		
		if (value.equalsIgnoreCase("none")) {
			callback.callback(null);
		} else {
			callback.callback(value);
		}
		
		if (item == null) {
			new CrateEditorMenu(crate).openMenu(player);
		} else {
			new CrateEditItemMenu(crate, item).openMenu(player);
		}
		
		return null;
	}
}
