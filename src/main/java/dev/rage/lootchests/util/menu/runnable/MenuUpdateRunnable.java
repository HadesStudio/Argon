package dev.rage.lootchests.util.menu.runnable;

import dev.rage.lootchests.util.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MenuUpdateRunnable implements Runnable {

	@Override
	public void run() {
		Menu.currentlyOpenedMenus.forEach((key, value) -> {
			if (value.isAutoUpdate()) {
				Player player = Bukkit.getPlayer(key);

				if (player != null) {
					value.openMenu(player);
				}
			}
		});
	}
}
