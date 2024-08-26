package dev.astro.argon.crate.menu.loot.button;

import dev.astro.argon.item.LootItem;
import dev.astro.argon.util.ItemBuilder;
import dev.astro.argon.util.menu.button.Button;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CrateLootButton extends Button {

	private final LootItem crateItem;

	public CrateLootButton(LootItem crateItem) {
		this.crateItem = crateItem;
	}

	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(crateItem.getItemStack().clone()).build();
	}
}