package dev.rage.lootchests.crate.menu.editor;

import com.google.common.collect.Maps;
import dev.rage.lootchests.config.SettingsConfig;
import dev.rage.lootchests.crate.Crate;
import dev.rage.lootchests.crate.menu.editor.button.CrateEditButton;
import dev.rage.lootchests.crate.menu.editor.button.CrateItemCommandsButton;
import dev.rage.lootchests.crate.prompt.CrateEditIntegerPrompt;
import dev.rage.lootchests.crate.prompt.CrateEditStringPrompt;
import dev.rage.lootchests.item.LootItem;
import dev.rage.lootchests.util.ColorUtil;
import dev.rage.lootchests.util.ItemBuilder;
import dev.rage.lootchests.util.TextUtil;
import dev.rage.lootchests.util.menu.Menu;
import dev.rage.lootchests.util.menu.button.Button;
import dev.rage.lootchests.util.menu.button.impl.BackButton;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

@RequiredArgsConstructor
public class CrateEditItemMenu extends Menu {

	private final Crate crate;
	private final LootItem item;

	public int selectedCommand = 0;
	
	@Override
	public String getTitle(Player player) {
		return "&9Editing &f" + item.getItemStack().getType().name();
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = Maps.newHashMap();
		
		buttons.put(0, new BackButton(new CrateEditLootMenu(crate)));
		
		buttons.put(9, new CrateEditButton(
				crate,
				new ItemBuilder(Material.FIREWORK)
					.setName(SettingsConfig.SECONDARY_COLOR + "Current Announcement&7: &r" + (TextUtil.isNull(item.getAnnouncement()) ? "&cEmpty" : item.getAnnouncement()))
					.setLore(
							"",
							SettingsConfig.SECONDARY_COLOR + " Placeholders&7:",
							" &7- &f<player>",
							" &7- &f<item-amount>",
							" &7- &f<item-name>",
							"",
							" &7» &eClick to set new announcement &7«",
							""
							)
					.build(),
				(crate) -> {
					new CrateEditStringPrompt(crate, item, (announce) -> {
						item.setAnnouncement(announce);
						
						player.sendMessage(ColorUtil.colorize("&aYou have updated the item announce to '&r" + announce + "&a'!"));
					}).startPrompt(player);
					
					player.sendMessage(ColorUtil.colorize("&aWrite new announce for this item or type '&fnone&a' to remove..."));
				}
		));

		buttons.put(10, new CrateEditButton(
				crate,
				new ItemBuilder(Material.DIAMOND)
					.setName(SettingsConfig.SECONDARY_COLOR + "Current Chance&7: &r" + item.getChance())
					.setLore(
							"",
							" &7» &eClick to edit chance &7«",
							""
							)
					.build(),
				(crate) -> {
					new CrateEditIntegerPrompt(crate, item, (chance) -> {
						item.setChance(chance);
						
						player.sendMessage(ColorUtil.colorize("&aYou have updated the item chance to '&r" + chance + "&a'!"));
					}).startPrompt(player);
					
					player.sendMessage(ColorUtil.colorize("&aWrite new chance for this item..."));
				}
		));

		buttons.put(11, new CrateItemCommandsButton(crate, item, this));
		
		return buttons;
	}
}
