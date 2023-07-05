package dev.rage.lootchests.util.menu.color.button;

import dev.rage.lootchests.util.Callback;
import dev.rage.lootchests.util.ColorUtil;
import dev.rage.lootchests.util.ItemBuilder;
import dev.rage.lootchests.util.TextUtil;
import dev.rage.lootchests.util.menu.Menu;
import dev.rage.lootchests.util.menu.button.Button;
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
		return new ItemBuilder(Material.WOOL).setName(color + TextUtil.formatPretty(color.name()))
				.setDurability(ColorUtil.getWoolColorFromChatColor(color)).setLore("", " &eClick to select color", " ")
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