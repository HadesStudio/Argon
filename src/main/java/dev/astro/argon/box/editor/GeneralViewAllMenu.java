package dev.astro.argon.box.editor;

import com.cryptomorin.xseries.XMaterial;
import dev.astro.argon.ArgonPlugin;
import dev.astro.argon.box.mystery.MysteryBox;
import dev.astro.argon.config.SettingsConfig;
import dev.astro.argon.item.LootItem;
import dev.astro.argon.util.ItemBuilder;
import dev.astro.argon.util.menu.Menu;
import dev.astro.argon.util.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class GeneralViewAllMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return SettingsConfig.SECONDARY_COLOR + "Box Editor";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		IntStream.range(0, 9).forEach(i -> buttons.put(i, getPlaceholderButton()));

		buttons.put(2, new Button() {

			@Override
			public ItemStack getButtonItem(Player player) {
				return new ItemBuilder(LootItem.LUCKY_BLOCK_ITEM)
						.setName(SettingsConfig.SECONDARY_COLOR + "Lucky Block Editor")
						.setLore(
								"",
								" &7» &eClick here to edit &7«",
								""
						)
						.build();
			}

			@Override
			public void clicked(Player player, ClickType clickType) {
				new LuckyBlockEditMenu(null).openMenu(player);
			}
		});

		buttons.put(4, new Button() {

			@Override
			public ItemStack getButtonItem(Player player) {
				return new ItemBuilder(LootItem.MYSTERY_BOX_ITEM)
						.setName(SettingsConfig.SECONDARY_COLOR + "Mystery Box Editor")
						.setLore(
								"",
								" &7» &eClick here to edit &7«",
								""
						)
						.build();
			}

			@Override
			public void clicked(Player player, ClickType clickType) {
				MysteryBox box = new MysteryBox(ArgonPlugin.get().getBoxHandler().getMysteryBoxCommon(),
						ArgonPlugin.get().getBoxHandler().getMysteryBoxOp());

				new MysteryBoxEditMenu(box).openMenu(player);
			}
		});

		buttons.put(6, new Button() {

			@Override
			public ItemStack getButtonItem(Player player) {
				return new ItemBuilder(XMaterial.ENDER_CHEST.parseMaterial())
						.setName(SettingsConfig.SECONDARY_COLOR + "Loot Box Editor")
						.setLore(
								"",
								" &7» &eClick here to edit &7«",
								""
						)
						.build();
			}

			@Override
			public void clicked(Player player, ClickType clickType) {
				new LootBoxEditMenu(null).openMenu(player);
			}
		});
		return buttons;
	}
}
