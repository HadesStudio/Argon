package dev.rage.lootchests.crate.menu.editor.button;

import dev.rage.lootchests.crate.Crate;
import dev.rage.lootchests.util.Callback;
import dev.rage.lootchests.util.menu.button.Button;
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
