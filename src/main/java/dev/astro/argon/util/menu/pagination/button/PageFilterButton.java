package dev.astro.argon.util.menu.pagination.button;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.Lists;
import dev.astro.argon.config.LanguageConfig;
import dev.astro.argon.config.MenusConfig;
import dev.astro.argon.util.ColorUtil;
import dev.astro.argon.util.ItemBuilder;
import dev.astro.argon.util.UnicodeUtil;
import dev.astro.argon.util.config.Replacement;
import dev.astro.argon.util.menu.button.Button;
import dev.astro.argon.util.menu.pagination.FilterablePaginatedMenu;
import dev.astro.argon.util.menu.pagination.PageFilter;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
public class PageFilterButton<T> extends Button {

	private FilterablePaginatedMenu<T> menu;

	@Override
	public ItemStack getButtonItem(Player player) {
		if (menu.getFilters() == null || menu.getFilters().isEmpty()) {
			return new ItemStack(Material.AIR);
		}

		List<String> lore = Lists.newArrayList();
		lore.add("");

		for (PageFilter<?> filter : menu.getFilters()) {
			String color;
			String decoration = "";
			String icon;

			if (filter.isEnabled()) {
				color = ChatColor.GREEN.toString();
				icon = UnicodeUtil.CORRECT;
			} else {
				color = ChatColor.RED.toString();
				icon = UnicodeUtil.INCORRECT;
			}

			if (menu.getFilters().get(menu.getScrollIndex()).equals(filter)) {
				decoration = ChatColor.YELLOW + StringEscapeUtils.unescapeJava("» ") + " ";
			}

			Replacement replacement = new Replacement(MenusConfig.FILTER_TYPE_FORMAT);
			replacement.add("<filter>", filter.getName());

			lore.add(decoration + color + (decoration.isEmpty() ? "" : ChatColor.ITALIC.toString()) + icon + " " + replacement.toString());
		}

		lore.addAll(MenusConfig.FILTER_BUTTON_LORE);

		Replacement replacement = new Replacement(MenusConfig.FILTERS_BUTTON_TITLE);
		replacement.add("<filter-count>", menu.getFilters().size());

		return new ItemBuilder(XMaterial.HOPPER.parseMaterial())
				.setName(replacement.toString())
				.setLore(lore)
				.build();
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		if (menu.getFilters() == null || menu.getFilters().isEmpty()) {
			player.sendMessage(ColorUtil.colorize(LanguageConfig.NO_FILTERS));
		} else {
			if (clickType == ClickType.LEFT) {
				if (menu.getScrollIndex() == menu.getFilters().size() - 1) {
					menu.setScrollIndex(0);
				} else {
					menu.setScrollIndex(menu.getScrollIndex() + 1);
				}
			} else if (clickType == ClickType.RIGHT) {
				PageFilter<T> filter = menu.getFilters().get(menu.getScrollIndex());
				filter.setEnabled(!filter.isEnabled());
			}
		}
	}

	@Override
	public boolean shouldUpdate(Player player, ClickType clickType) {
		return true;
	}
}
