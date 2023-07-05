package dev.rage.lootchests.crate.menu.loot.button;

import dev.rage.lootchests.item.LootItem;
import dev.rage.lootchests.util.ItemBuilder;
import dev.rage.lootchests.util.menu.button.Button;
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