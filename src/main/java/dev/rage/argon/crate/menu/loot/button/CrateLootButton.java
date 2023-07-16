package dev.rage.argon.crate.menu.loot.button;

import dev.rage.argon.item.LootItem;
import dev.rage.argon.util.ItemBuilder;
import dev.rage.argon.util.menu.button.Button;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CrateLootButton extends Button {

	private LootItem crateItem;

	public CrateLootButton(LootItem crateItem) {
		this.crateItem = crateItem;
	}

	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(crateItem.getItemStack().clone()).build();
	}
}