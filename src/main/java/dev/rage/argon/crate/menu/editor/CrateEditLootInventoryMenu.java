package dev.rage.argon.crate.menu.editor;

import dev.rage.argon.Argon;
import dev.rage.argon.config.LanguageConfig;
import dev.rage.argon.config.SettingsConfig;
import dev.rage.argon.crate.Crate;
import dev.rage.argon.item.LootItem;
import dev.rage.argon.util.ColorUtil;
import dev.rage.argon.util.ItemBuilder;
import dev.rage.argon.util.JavaUtil;
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

import java.util.*;

public class CrateEditLootInventoryMenu implements Listener {

	// Created for register listener.
	public CrateEditLootInventoryMenu() { }

	private Map<UUID, LootItem> items;

	public CrateEditLootInventoryMenu(Player target, Crate crate) {
		items = new HashMap<>();

		Inventory inventory = Bukkit.createInventory(null, 54, (SettingsConfig.SECONDARY_COLOR + "Editing loot: " + crate.getColor() + crate.getName()));

		crate.getLootItems().forEach(item -> {
			UUID id = UUID.randomUUID();

			items.put(id, item);
			inventory.setItem(item.getPosition(), applyId(item, id));
		});
		
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

					List<LootItem> items = new ArrayList<>();

					for(int i = 0; i < inventory.getSize(); i++) {
						ItemStack itemStack = inventory.getItem(i);
						if(itemStack != null && itemStack.getType() != Material.AIR) {
							LootItem item = readId(itemStack);
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

		for (int i = 0; i < player.getInventory().getContents().length; i++) {
			ItemStack item = player.getInventory().getContents()[i];

			if (hasId(item)) {
				player.getInventory().setItem(i, removeId(item));
			}
		}
	}

	public ItemStack applyId(LootItem item, UUID id) {
		ItemBuilder builder = new ItemBuilder(item.getItemStack().clone());

		builder.addLore(" ");
		builder.addLore("&8" + id.toString());

		return builder.build();
	}

	public LootItem readId(ItemStack stack) {
		ItemMeta meta = stack.getItemMeta();

		if (meta.hasLore()) {
			List<String> lore = meta.getLore();

			String line = ChatColor.stripColor(lore.get(lore.size() - 1));

			if (JavaUtil.isUUID(line)) {
				UUID id = UUID.fromString(line);

				return items.get(id);
			}
		}

		return new LootItem(stack);
	}

	public ItemStack removeId(ItemStack stack) {
		ItemMeta meta = stack.getItemMeta();

		List<String> newLore = new ArrayList<>();
		if (meta.hasLore()) {
			List<String> lore = meta.getLore();

			String line = ChatColor.stripColor(lore.get(lore.size() - 1));

			if (JavaUtil.isUUID(line)) {
				for (int i = 0; i < lore.size() - 2; i++) {
					newLore.add(lore.get(i));
				}
			}
		}

		return new ItemBuilder(stack).setLore(newLore).build();
	}

	public boolean hasId(ItemStack stack) {
		ItemMeta meta = stack.getItemMeta();

		if (meta.hasLore()) {
			List<String> lore = meta.getLore();

			String line = ChatColor.stripColor(lore.get(lore.size() - 1));

			return JavaUtil.isUUID(line);
		}

		return false;
	}
}
