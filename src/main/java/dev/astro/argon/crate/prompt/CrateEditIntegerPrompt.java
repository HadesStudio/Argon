package dev.astro.argon.crate.prompt;

import dev.astro.argon.crate.Crate;
import dev.astro.argon.crate.menu.editor.CrateEditItemMenu;
import dev.astro.argon.item.LootItem;
import dev.astro.argon.util.Callback;
import dev.astro.argon.util.prompt.Prompt;
import dev.astro.argon.util.prompt.defaults.IntegerPrompt;
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
