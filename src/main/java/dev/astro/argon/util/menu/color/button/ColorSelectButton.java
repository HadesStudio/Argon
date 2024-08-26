package dev.astro.argon.util.menu.color.button;

import dev.astro.argon.util.Callback;
import dev.astro.argon.util.ColorUtil;
import dev.astro.argon.util.ItemBuilder;
import dev.astro.argon.util.TextUtil;
import dev.astro.argon.util.menu.Menu;
import dev.astro.argon.util.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class ColorSelectButton extends Button {

	private final Callback<ChatColor> callback;
	private final ChatColor color;
	private final Menu backMenu;

	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(ColorUtil.getWoolFromChatColor(color)).setName(color + TextUtil.formatPretty(color.name()))
				.setLore("", " &eClick to select color", " ")
				.build();
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		if (clickType.isLeftClick()) {
			callback.callback(color);
			player.closeInventory();

			backMenu.openMenu(player);
		}
	}
}