package dev.rage.lootchests.crate.menu.editor;

import dev.rage.lootchests.LootChests;
import dev.rage.lootchests.config.LanguageConfig;
import dev.rage.lootchests.config.SettingsConfig;
import dev.rage.lootchests.crate.Crate;
import dev.rage.lootchests.item.LootItem;
import dev.rage.lootchests.util.ColorUtil;
import dev.rage.lootchests.util.config.Replacement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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

		if (event.getPlayer().hasPermission("lootchests.command.crate.edit") && title.contains(SettingsConfig.SECONDARY_COLOR + "Editing loot: ")) {
			String crateName = ChatColor.stripColor(title.replace(SettingsConfig.SECONDARY_COLOR + "Editing loot: ", ""));
			Crate crate = LootChests.get().getCrateHandler().getByName(crateName);
			crate.getLootItems().clear();

			for(int i = 0; i < inventory.getSize(); i++) {
				ItemStack itemStack = inventory.getItem(i);
				if(itemStack != null && itemStack.getType() != Material.AIR) {
					LootItem item = new LootItem(itemStack);
					item.setPosition(i);

					crate.getLootItems().add(item);
				}
			}

			Replacement replacement = new Replacement(LanguageConfig.CRATE_EDITED_LOOT);
			replacement.add("<prefix>", LanguageConfig.PREFIX);
			replacement.add("<crate>", crate.getName());

			player.sendMessage(ColorUtil.colorize(replacement.toString()));
		}
	}
}
