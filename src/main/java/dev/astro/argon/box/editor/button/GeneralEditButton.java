package dev.astro.argon.box.editor.button;

import dev.astro.argon.box.mystery.MysteryBox;
import dev.astro.argon.util.Callback;
import dev.astro.argon.util.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class GeneralEditButton extends Button {

	private final MysteryBox mysteryBox;
	private final ItemStack item;
	private final Callback<MysteryBox> callback;
	
	@Override
	public ItemStack getButtonItem(Player player) {
		return item;
	}
	
	@Override
	public void clicked(Player player, ClickType clickType) {
		if (clickType.isLeftClick()) {
			callback.callback(mysteryBox);
		}
	}
}
