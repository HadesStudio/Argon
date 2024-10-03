package dev.astro.argon.box.editor;

import com.google.common.collect.Maps;
import dev.astro.argon.ArgonPlugin;
import dev.astro.argon.box.editor.button.GeneralEditButton;
import dev.astro.argon.box.editor.prompt.MysteryBoxEditStringPrompt;
import dev.astro.argon.box.mystery.MysteryBox;
import dev.astro.argon.config.SettingsConfig;
import dev.astro.argon.util.ColorUtil;
import dev.astro.argon.util.ItemBuilder;
import dev.astro.argon.util.TextUtil;
import dev.astro.argon.util.menu.Menu;
import dev.astro.argon.util.menu.button.Button;
import dev.astro.argon.util.menu.button.impl.BackButton;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

@RequiredArgsConstructor
public class MysteryBoxEditMenu extends Menu {

	private final MysteryBox mysteryBox;

	@Override
	public String getTitle(Player player) {
		return SettingsConfig.SECONDARY_COLOR + "Mystery Box Editor";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = Maps.newHashMap();

		buttons.put(8, new BackButton(new GeneralViewAllMenu()));

		buttons.put(0, new GeneralEditButton(
				mysteryBox,
				new ItemBuilder(Material.FIREWORK)
						.setName(SettingsConfig.SECONDARY_COLOR + "Current Announcement&7: &r" + (TextUtil.isNull(mysteryBox.getAnnouncement()) ? "&cEmpty" : mysteryBox.getAnnouncement()))
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
				(mysteryBox) -> {
					new MysteryBoxEditStringPrompt(mysteryBox, (announce) -> {
						mysteryBox.setAnnouncement(announce);

						ArgonPlugin.get().getBoxHandler().setMysteryBoxAnnounce(announce);
						ArgonPlugin.get().getBoxHandler().saveLoot();

						player.sendMessage(ColorUtil.colorize("&aYou have updated the mystery box announce to '&r" + announce + "&a'!"));
					}).startPrompt(player);

					player.sendMessage(ColorUtil.colorize("&aWrite new announce for this mystery box or type '&fnone&a' to remove..."));
				}
		));
		
		return buttons;
	}
}
