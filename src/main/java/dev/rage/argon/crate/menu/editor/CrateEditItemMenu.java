package dev.rage.argon.crate.menu.editor;

import com.google.common.collect.Maps;
import dev.rage.argon.config.SettingsConfig;
import dev.rage.argon.crate.Crate;
import dev.rage.argon.crate.menu.editor.button.CrateEditButton;
import dev.rage.argon.crate.menu.editor.button.CrateItemCommandsButton;
import dev.rage.argon.crate.prompt.CrateEditIntegerPrompt;
import dev.rage.argon.crate.prompt.CrateEditStringPrompt;
import dev.rage.argon.item.LootItem;
import dev.rage.argon.util.ColorUtil;
import dev.rage.argon.util.ItemBuilder;
import dev.rage.argon.util.TextUtil;
import dev.rage.argon.util.menu.Menu;
import dev.rage.argon.util.menu.button.Button;
import dev.rage.argon.util.menu.button.impl.BackButton;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

@RequiredArgsConstructor
public class CrateEditItemMenu extends Menu {

	private final Crate crate;
	private final LootItem item;

	public int selectedCommand = 0;

	{
		setEditable(true);
	}

	@Override
	public String getTitle(Player player) {
		return SettingsConfig.SECONDARY_COLOR + "Editing &f" + item.getItemStack().getType().name();
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
