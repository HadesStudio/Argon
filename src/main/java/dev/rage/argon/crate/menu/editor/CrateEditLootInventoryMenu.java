package dev.rage.argon.crate.menu.editor;

import dev.rage.argon.Argon;
import dev.rage.argon.config.LanguageConfig;
import dev.rage.argon.config.SettingsConfig;
import dev.rage.argon.crate.Crate;
import dev.rage.argon.item.LootItem;
import dev.rage.argon.util.ColorUtil;
import dev.rage.argon.util.config.Replacement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CrateEditLootInventoryMenu implements Listener {

	// Created for register listener.
	public CrateEditLootInventoryMenu() { }
	
	public CrateEditLootInventoryMenu(Player target, Crate crate) {
		Inventory inventory = Bukkit.createInventory(null, 54, (SettingsConfig.SECONDARY_COLOR + "Editing loot: " + crate.getColor() + crate.getName()));

		crate.getLootItems().forEach(item -> inventory.setItem(item.getPosition(), item.getItemStack().clone()));
		
		target.openInventory(inventory);
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		Inventory inventory = event.getInventory();
		Player player = (Player) event.getPlayer();
		String title = event.getView().getTitle();

		if (inventory != null) {
			if (inventory != player.getInventory()) {
				if (player.hasPermission("lootchests.command.crate.edit") && title.contains(SettingsConfig.SECONDARY_COLOR + "Editing loot: ")) {
					String crateName = ChatColor.stripColor(title.replace(SettingsConfig.SECONDARY_COLOR + "Editing loot: ", ""));
					Crate crate = Argon.get().getCrateHandler().getByName(crateName);

					List<LootItem> lastItems = crate.getLootItems();
					List<LootItem> items = new ArrayList<>();

					for(int i = 0; i < inventory.getSize(); i++) {
						ItemStack itemStack = inventory.getItem(i);
						if(itemStack != null && itemStack.getType() != Material.AIR) {
							LootItem item = getOrCreateItem(lastItems, itemStack);
							item.setPosition(i);

							items.add(item);
						}
					}

					crate.setLootItems(items);

					Replacement replacement = new Replacement(LanguageConfig.CRATE_EDITED_LOOT);
					replacement.add("<prefix>", LanguageConfig.PREFIX);
					replacement.add("<crate>", crate.getName());

					player.sendMessage(ColorUtil.colorize(replacement.toString()));
				}
			}
		}
	}

	private LootItem getOrCreateItem(List<LootItem> items, ItemStack itemStack) {
		LootItem item = items.stream().filter(i -> i.getItemStack().equals(itemStack)).findFirst().orElse(null);

		if (item == null) {
			return new LootItem(itemStack.clone());
		}

		return item;
	}
}
