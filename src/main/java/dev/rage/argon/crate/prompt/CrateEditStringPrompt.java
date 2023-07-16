package dev.rage.argon.crate.prompt;

import dev.rage.argon.crate.Crate;
import dev.rage.argon.crate.menu.editor.CrateEditItemMenu;
import dev.rage.argon.crate.menu.editor.CrateEditorMenu;
import dev.rage.argon.item.LootItem;
import dev.rage.argon.util.Callback;
import dev.rage.argon.util.prompt.Prompt;
import dev.rage.argon.util.prompt.defaults.StringPrompt;
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
