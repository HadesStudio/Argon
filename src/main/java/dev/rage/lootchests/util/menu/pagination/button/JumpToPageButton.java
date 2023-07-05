package dev.rage.lootchests.util.menu.pagination.button;

import dev.rage.lootchests.config.MenusConfig;
import dev.rage.lootchests.util.ItemBuilder;
import dev.rage.lootchests.util.config.Replacement;
import dev.rage.lootchests.util.menu.button.Button;
import dev.rage.lootchests.util.menu.pagination.PaginatedMenu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@Getter @AllArgsConstructor
public class JumpToPageButton extends Button {

	private int page;
	private PaginatedMenu menu;
	private boolean current;
	
	@Override
	public ItemStack getButtonItem(Player player) {
		ItemBuilder itemBuilder = new ItemBuilder(current ? Material.ENCHANTED_BOOK : Material.BOOK);

		Replacement replacement = new Replacement(MenusConfig.PAGE_BUTTON_TITLE);
		replacement.add("<page>", page);

		itemBuilder.setName(replacement.toString());
		itemBuilder.setAmount(page);
		
		if(current) {
			itemBuilder.setLore(MenusConfig.PAGE_BUTTON_CURRENT_PAGE);
		}

		return itemBuilder.build();
	}

	@Override
	public void clicked(Player player, int i, ClickType clickType, int hb) {
		menu.modPage(player, page - menu.getPage());
		Button.playNeutral(player);
	}
}
