package dev.rage.argon.crate.menu.editor.button;

import dev.rage.argon.crate.Crate;
import dev.rage.argon.util.Callback;
import dev.rage.argon.util.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class CrateEditButton extends Button {

	private final Crate crate;
	private final ItemStack item;
	private final Callback<Crate> callback;
	
	@Override
	public ItemStack getButtonItem(Player player) {
		return item;
	}
	
	@Override
	public void clicked(Player player, ClickType clickType) {
		if (clickType.isLeftClick()) {
			callback.callback(crate);
		}
	}
}
