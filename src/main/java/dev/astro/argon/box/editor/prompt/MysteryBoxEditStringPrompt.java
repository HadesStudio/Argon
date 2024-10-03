package dev.astro.argon.box.editor.prompt;

import dev.astro.argon.box.editor.MysteryBoxEditMenu;
import dev.astro.argon.box.mystery.MysteryBox;
import dev.astro.argon.crate.Crate;
import dev.astro.argon.crate.menu.editor.CrateEditItemMenu;
import dev.astro.argon.crate.menu.editor.CrateEditorMenu;
import dev.astro.argon.item.LootItem;
import dev.astro.argon.util.Callback;
import dev.astro.argon.util.prompt.Prompt;
import dev.astro.argon.util.prompt.defaults.StringPrompt;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class MysteryBoxEditStringPrompt extends StringPrompt {

	private final MysteryBox mysteryBox;
	private final Callback<String> callback;
	
	@Override
	public void handleBegin(Player player) {
		player.closeInventory();
	}

	@Override
	public void handleCancel(Player player) {
		new MysteryBoxEditMenu(mysteryBox).openMenu(player);
	}

	@Override
	public Prompt<?> acceptInput(Player player, String value) {
		
		if (value.equalsIgnoreCase("none")) {
			callback.callback(null);
		} else {
			callback.callback(value);
		}

		new MysteryBoxEditMenu(mysteryBox).openMenu(player);

		
		return null;
	}
}
