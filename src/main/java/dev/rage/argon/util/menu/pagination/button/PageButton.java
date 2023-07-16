package dev.rage.argon.util.menu.pagination.button;

import dev.rage.argon.config.MenusConfig;
import dev.rage.argon.util.ItemBuilder;
import dev.rage.argon.util.menu.button.Button;
import dev.rage.argon.util.menu.pagination.PaginatedMenu;
import dev.rage.argon.util.menu.pagination.ViewAllPagesMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class PageButton extends Button {

	private int mod;
	private PaginatedMenu menu;

	@Override
	public ItemStack getButtonItem(Player player) {
		ItemBuilder itemBuilder = new ItemBuilder(Material.ARROW);

		if (hasNext(player)) {
			itemBuilder.setName(mod > 0 ? MenusConfig.NEXT_PAGE_BUTTON_TITLE : MenusConfig.PREVIOUS_PAGE_BUTTON_TITLE);
		} else {
			itemBuilder.setName(mod > 0 ? "&aLast Page" : "&aFirst Page");
		}

		itemBuilder.setLore(MenusConfig.JUMP_PAGE_BUTTON_LORE);
		
		return itemBuilder.build();
	}

	@Override
	public void clicked(Player player, int i, ClickType clickType, int hb) {
		if (clickType.equals(ClickType.RIGHT)) {
			new ViewAllPagesMenu(menu).openMenu(player);
			playNeutral(player);
		} else {
			if (hasNext(player)) {
				menu.modPage(player, mod);
				Button.playNeutral(player);
			} else {
				Button.playFail(player);
			}
		}
		
	}

	private boolean hasNext(Player player) {
		int pg = menu.getPage() + mod;
		return pg > 0 && menu.getPages(player) >= pg;
	}

}
