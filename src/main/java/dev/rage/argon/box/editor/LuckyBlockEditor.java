package dev.rage.argon.box.editor;

import dev.rage.argon.ArgonPlugin;
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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LuckyBlockEditor implements Listener {

	// Created for register listener.
	public LuckyBlockEditor() { }

	public LuckyBlockEditor(Player target, ItemStack[] currentLoot) {
		Inventory inventory = Bukkit.createInventory(null, 54, (SettingsConfig.SECONDARY_COLOR + "Lucky Block Editor"));

		for (int i = 0; i < currentLoot.length; i++) {
			inventory.setItem(i, currentLoot[i]);
		}

		target.openInventory(inventory);
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		Inventory inventory = event.getInventory();
		Player player = (Player) event.getPlayer();
		String title = event.getView().getTitle();

		if (inventory != null) {
			if (inventory != player.getInventory()) {
				if (player.hasPermission("argon.command.lootbox.edit.luckyblock") && title.contains(SettingsConfig.SECONDARY_COLOR + "Lucky Block Editor")) {
					List<ItemStack> items = new ArrayList<>();
					for (int i = 0; i < inventory.getSize(); i++) {
						ItemStack itemStack = inventory.getItem(i);
						if (itemStack != null && itemStack.getType() != Material.AIR) {
							items.add(itemStack);
						}
					}

					ArgonPlugin.get().getBoxHandler().setLuckyBlock(items.toArray(new ItemStack[0]));
					ArgonPlugin.get().getBoxHandler().saveLoot();
				}
			}
		}
	}
}
