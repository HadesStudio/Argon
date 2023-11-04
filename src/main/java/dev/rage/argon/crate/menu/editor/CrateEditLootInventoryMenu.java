package dev.rage.argon.crate.menu.editor;

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

public class CrateEditLootInventoryMenu implements Listener {

	// Created for register listener.
	public CrateEditLootInventoryMenu() { }

	public CrateEditLootInventoryMenu(Player target, Crate crate) {
		Inventory inventory = Bukkit.createInventory(null, 54, (SettingsConfig.SECONDARY_COLOR + "Editing loot: " + crate.getColor() + crate.getName()));

		crate.getLootItems().forEach(item -> {
			inventory.setItem(item.getPosition(), setData(item));
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
					Crate crate = ArgonPlugin.get().getCrateHandler().getByName(crateName);

					List<LootItem> items = new ArrayList<>();


					for (int i = 0; i < inventory.getSize(); i++) {
						ItemStack itemStack = inventory.getItem(i);
						if (itemStack != null && itemStack.getType() != Material.AIR) {
							LootItem item = loadData(itemStack);

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

			for (int i = 0; i < player.getInventory().getContents().length; i++) {
				ItemStack item = player.getInventory().getContents()[i];

				if(item != null && item.getType() != Material.AIR) {
					if (hasData(item)) {
						player.getInventory().setItem(i, removeData(item));
					}
				}
			}
		}
	}

	private boolean hasData(ItemStack stack) {
		ItemMeta meta = stack.getItemMeta();

		if (meta != null) {
			List<String> lore = meta.getLore();
			if (lore != null) {
				for (String line : lore) {
					if (line.startsWith(ChatColor.AQUA + "Chance: ")) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private ItemStack setData(LootItem item) {
		ItemStack stack = item.getItemStack().clone();
		ItemMeta meta = stack.getItemMeta();

		if (meta != null) {
			List<String> lore = meta.getLore();
			if (lore == null) {
				lore = new ArrayList<>();
			}

			lore.add(ChatColor.STRIKETHROUGH.toString());
			lore.add(ChatColor.AQUA + "Chance: " + ChatColor.WHITE + item.getChance());

			if (item.getAnnouncement() != null) lore.add(ChatColor.AQUA + "Announcement: " + ChatColor.WHITE + item.getAnnouncement());

			if (!item.getCommands().isEmpty()) {
				lore.add(ChatColor.AQUA + "Commands:");

				for (String command : item.getCommands()) {
					lore.add("  /" + ChatColor.WHITE + command);
				}
			}

			meta.setLore(lore);
			stack.setItemMeta(meta);
		}

		return stack;
	}

	private ItemStack removeData(ItemStack stack) {
		if (!hasData(stack)) return stack;

		ItemMeta meta = stack.getItemMeta();

		if (meta != null) {
			List<String> lore = meta.getLore();
			if (lore != null) {
				Iterator<String> iterator = lore.iterator();
				boolean inCustomList = false;
				while (iterator.hasNext()) {
					String line = iterator.next();

					if (line.equalsIgnoreCase(ChatColor.STRIKETHROUGH.toString())) {
						iterator.remove();
					}

					if (line.startsWith(ChatColor.AQUA + "Chance: ") || line.startsWith(ChatColor.AQUA + "Announcement: ")) {
						iterator.remove();
					}

					if (inCustomList) {
						if (line.startsWith("  /")) {
							iterator.remove();
						} else {
							break;
						}
					} else if (line.startsWith(ChatColor.AQUA + "Commands:")) {
						inCustomList = true;
						iterator.remove();
					}
				}
				meta.setLore(lore);
				stack.setItemMeta(meta);
			}
		}

		return stack;
	}

	private LootItem loadData(ItemStack stack) {
		LootItem item = new LootItem(removeData(stack.clone()));

		ItemMeta meta = stack.getItemMeta();

		if (meta != null) {
			List<String> lore = meta.getLore();
			if (lore != null) {
				boolean inCustomList = false;
				for (String line : lore) {
					if (line.startsWith(ChatColor.AQUA + "Chance: ")) {
						String c = line.substring((ChatColor.AQUA + "Chance: " + ChatColor.WHITE).length());

						item.setChance(Integer.parseInt(c));
					}

					if (line.startsWith(ChatColor.AQUA + "Announcement: ")) {
						String a = line.substring((ChatColor.AQUA + "Announcement: " + ChatColor.WHITE).length());

						item.setAnnouncement(a);
					}

					List<String> commands = new ArrayList<>();
					if (inCustomList) {
						if (line.startsWith("  /")) {
							commands.add(line.substring(3));
						} else {
							break;
						}
					} else if (line.startsWith(ChatColor.AQUA + "Commands:")) {
						inCustomList = true;
					}

					item.setCommands(commands);
				}
			}
		}

		return item;
	}
}
