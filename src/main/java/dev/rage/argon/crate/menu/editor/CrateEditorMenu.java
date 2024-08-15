package dev.rage.argon.crate.menu.editor;

import dev.rage.argon.config.SettingsConfig;
import dev.rage.argon.crate.Crate;
import dev.rage.argon.crate.menu.CrateViewAllMenu;
import dev.rage.argon.crate.menu.button.CrateInfoButton;
import dev.rage.argon.crate.menu.editor.button.CrateEditButton;
import dev.rage.argon.crate.menu.editor.button.CrateHologramButton;
import dev.rage.argon.crate.menu.location.CrateLocationsMenu;
import dev.rage.argon.crate.menu.loot.CrateLootMenu;
import dev.rage.argon.util.ColorUtil;
import dev.rage.argon.util.ItemBuilder;
import dev.rage.argon.util.TextUtil;
import dev.rage.argon.util.menu.Menu;
import dev.rage.argon.util.menu.button.Button;
import dev.rage.argon.util.menu.button.impl.BackButton;
import dev.rage.argon.util.menu.color.ColorListMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class CrateEditorMenu extends Menu {

	private final Crate crate;

	public int selectedHologramLine = 0;

	@Override
	public String getTitle(Player player) {
		return SettingsConfig.SECONDARY_COLOR + "Editing &r" + crate.getColor() + crate.getName();
	}

	{
		setAutoUpdate(true);
	}
	
	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();
		
		IntStream.range(0, 36).forEach(i -> buttons.put(i, getPlaceholderButton()));
		
		buttons.put(0, new BackButton(new CrateViewAllMenu()));
		buttons.put(4, new CrateInfoButton(crate, false));
		
		buttons.put(6, new Button() {
			
			@Override
			public ItemStack getButtonItem(Player player) {
				return new ItemBuilder(Material.CHEST)
						.setName(SettingsConfig.SECONDARY_COLOR + "Loot&7: &r" + crate.getLootItems().size())
						.setLore(
								"",
								" &7» &eLeft click to view loot &7«",
								" &7» &eRight click to edit loot &7«",
								""
								)
						.build();
			}
			
			@Override
			public void clicked(Player player, ClickType clickType) {
				if (clickType.isLeftClick()) {
					new CrateLootMenu(crate).openMenu(player);
				} else if (clickType.isRightClick()) {
					new CrateEditLootInventoryMenu(player, crate);
				}
			}
		});
		
		buttons.put(7, new Button() {
			
			@Override
			public ItemStack getButtonItem(Player player) {
				return new ItemBuilder(Material.ENDER_CHEST)
						.setName(SettingsConfig.SECONDARY_COLOR + "Edit Loot")
						.setLore(
								"",
								" &7» &eClick to edit loot items &7«",
								""
								)
						.build();
			}
			
			@Override
			public void clicked(Player player, ClickType clickType) {
				new CrateEditLootMenu(crate).openMenu(player);
			}
		});
		
		buttons.put(8, new CrateEditButton(
				crate,
				new ItemBuilder(Material.COMPASS)
				.setName(SettingsConfig.SECONDARY_COLOR + "Locations&7: &r" + crate.getLocations().size())
				.setLore(
						"",
						" &7» &eClick to view all locations &7«",
						""
						)
				.build(),
				(crate) -> new CrateLocationsMenu(crate).openMenu(player)
		));
		
		buttons.put(9, new CrateEditButton(
				crate,
				new ItemBuilder(ColorUtil.getWoolFromChatColor(crate.getColor()))
				.setName(SettingsConfig.SECONDARY_COLOR + "Current Color&7: &r" + crate.getColor() + TextUtil.formatPretty(crate.getColor().name()))
				.setLore(
						"",
						" &7» &eClick to edit color &7«",
						""
						)
				.build(),
				(crate) -> new ColorListMenu(crate::setColor, this).openMenu(player)
		));

		buttons.put(10, new CrateEditButton(
				crate,
				new ItemBuilder(Material.INK_SACK)
						.setName(SettingsConfig.SECONDARY_COLOR + "Open from Key&7: &r" + (crate.isOpenFromKey() ? "&aYes" : "&cNo"))
						.setDurability(crate.isOpenFromKey() ? 10 : 1)
						.setLore(
								"",
								" &7» &eClick to toggle open from key &7«",
								""
						)
						.build(),

				(crate) -> {
					crate.setOpenFromKey(!crate.isOpenFromKey());
				}
		));

		buttons.put(11, new CrateEditButton(
				crate,
				new ItemBuilder(Material.INK_SACK)
						.setName(SettingsConfig.SECONDARY_COLOR + "Virtual key&7: &r" + (crate.isVirtualKeys() ? "&aYes" : "&cNo"))
						.setDurability(crate.isVirtualKeys() ? 10 : 1)
						.setLore(
								"",
								" &7» &eClick to toggle virtual key &7«",
								""
						)
						.build(),

				(crate) -> {
					crate.setVirtualKeys(!crate.isVirtualKeys());
				}
		));

		buttons.put(12, new CrateHologramButton(crate, this));
		
		buttons.put(30, new Button() {
			
			@Override
			public ItemStack getButtonItem(Player player) {
				return new ItemBuilder(crate.getOpener())
						.setName(SettingsConfig.SECONDARY_COLOR + "Current Crate Opener&7: &r" + TextUtil.formatPretty(crate.getOpener().getType().name()))
						.setAmount(1)
						.setLore(
								"",
								" &7» &eClick with another block to change &7«",
								""
								)
						.build();
			}
			
			@Override
			public void clicked(Player player, ClickType clickType) {
				ItemStack cursor = player.getItemOnCursor();
				
				if (cursor.getType() == Material.AIR) {
					return;
				}
				
				if (!cursor.getType().isBlock()) {
					return;
				}
				
				crate.setOpener(cursor);
				player.setItemOnCursor(new ItemStack(Material.AIR));
			}
		});
		
		buttons.put(32, new Button() {
			
			@Override
			public ItemStack getButtonItem(Player player) {
				return new ItemBuilder(crate.getKey())
						.setName(SettingsConfig.SECONDARY_COLOR + "Current Crate Key&7: &r" + TextUtil.formatPretty(crate.getKey().getType().name()))
						.setAmount(1)
						.setLore(
								"",
								" &7» &eClick with another item to change &7«",
								""
								)
						.build();
			}
			
			@Override
			public void clicked(Player player, ClickType clickType) {
				ItemStack cursor = player.getItemOnCursor();
				
				if (cursor.getType() == Material.AIR) {
					return;
				}
				
				crate.setKey(cursor);
				player.setItemOnCursor(new ItemStack(Material.AIR));
			}
		});
		
		return buttons;
	}
}
