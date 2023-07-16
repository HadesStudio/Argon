package dev.rage.argon.crate.prompt;

import dev.rage.argon.crate.Crate;
import dev.rage.argon.crate.menu.editor.CrateEditItemMenu;
import dev.rage.argon.item.LootItem;
import dev.rage.argon.util.Callback;
import dev.rage.argon.util.prompt.Prompt;
import dev.rage.argon.util.prompt.defaults.IntegerPrompt;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class CrateEditIntegerPrompt extends IntegerPrompt {

	private final Crate crate;
	private final LootItem item;
	private final Callback<Integer> callback;
	
	@Override
	public void handleBegin(Player player) {
		player.closeInventory();
	}

	@Override
	public void handleCancel(Player player) {
		new CrateEditItemMenu(crate, item).openMenu(player);
	}

	@Override
	public Prompt<?> acceptInput(Player player, Integer value) {
		
		callback.callback(value);
		
		new CrateEditItemMenu(crate, item).openMenu(player);
		
		return null;
	}
}
