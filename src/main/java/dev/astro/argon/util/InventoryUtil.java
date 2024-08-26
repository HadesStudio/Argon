package dev.astro.argon.util;

import org.bukkit.entity.Player;

public class InventoryUtil {

	public static boolean isFull(Player player) {
		return player.getInventory().firstEmpty() == -1;
	}
}