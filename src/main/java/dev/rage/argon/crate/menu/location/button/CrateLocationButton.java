package dev.rage.argon.crate.menu.location.button;

import dev.rage.argon.config.SettingsConfig;
import dev.rage.argon.util.ItemBuilder;
import dev.rage.argon.util.LocationUtil;
import dev.rage.argon.util.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class CrateLocationButton extends Button {

	private final int count;
	private final Location location;
	
	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(Material.GRASS).setName(SettingsConfig.SECONDARY_COLOR + "Location&7: &r#" + (count + 1)).setLore("", "&r " + LocationUtil.formatLocation(location, true), "", " &7» &eClick to teleport to crate &7«", "").build();
	}
	
	@Override
	public void clicked(Player player, ClickType clickType) {
		player.closeInventory();
		player.teleport(location.clone().add(0.5, 1, 0.5));
	}
}
