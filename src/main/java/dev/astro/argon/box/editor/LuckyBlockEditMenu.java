package dev.astro.argon.box.editor;

import com.google.common.collect.Maps;
import dev.astro.argon.ArgonPlugin;
import dev.astro.argon.box.editor.prompt.GeneralStringPrompt;
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
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@RequiredArgsConstructor
public class LuckyBlockEditMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return SettingsConfig.SECONDARY_COLOR + "Lucky Block Editor  &f";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = Maps.newHashMap();

		buttons.put(8, new BackButton(new GeneralViewAllMenu()));

		buttons.put(0, new GeneralEditButton());
		
		return buttons;
	}

	public static class GeneralEditButton extends Button {

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.FIREWORK)
					.setName(SettingsConfig.SECONDARY_COLOR + "Current Announcement&7: &r" + (TextUtil.isNull(ArgonPlugin.get().getBoxHandler().getLuckyBlockAnnounce()) ? "&cEmpty" : ArgonPlugin.get().getBoxHandler().getLuckyBlockAnnounce()))
					.setLore(
							"",
							SettingsConfig.SECONDARY_COLOR + " Placeholder&7:",
							" &7- &f<player>",
							"",
							" &7» &eClick to set new announcement &7«",
							""
					)
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			new GeneralStringPrompt((announce) -> {
				ArgonPlugin.get().getBoxHandler().setLuckyBlockAnnounce(announce);
				ArgonPlugin.get().getBoxHandler().saveLoot();

				player.sendMessage(ColorUtil.colorize("&aYou have updated the lucky block announce to '&r" + announce + "&a'!"));
				new LuckyBlockEditMenu().openMenu(player);
			}).startPrompt(player);

			player.sendMessage(ColorUtil.colorize("&aWrite new announce for this lucky block or type '&fnone&a' to remove..."));
		}
	}
}
