package dev.rage.argon.crate.menu.editor.button;

import dev.rage.argon.crate.Crate;
import dev.rage.argon.crate.menu.editor.CrateEditItemMenu;
import dev.rage.argon.item.LootItem;
import dev.rage.argon.util.ItemBuilder;
import dev.rage.argon.util.TextUtil;
import dev.rage.argon.util.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CrateEditLootButton extends Button {

	private final Crate crate;
	private final LootItem crateItem;
	
	@Override
	public ItemStack getButtonItem(Player player) {
		ItemBuilder builder = new ItemBuilder(crateItem.getItemStack().clone());

		List<String> lore = new ArrayList<>();

		lore.add(" ");
		lore.add(" &bChance&7: &r" + crateItem.getChance());
		lore.add(" &bAnnouncement&7: &r" + (TextUtil.isNull(crateItem.getAnnouncement()) ? "&cEmpty" : crateItem.getAnnouncement()));
		lore.add(" &bCommands&7: &r");
		if (crateItem.getCommands().isEmpty()) {
			lore.add(" &cEmpty");
		} else {
			crateItem.getCommands().forEach(cmd -> lore.add(" &7- &r/" + cmd));
		}

		lore.add(" ");
		lore.add(" &7» &eClick to edit item&7«");
		lore.add(" ");

		lore.forEach(builder::addLore);

		return builder.build();
	}
	
	@Override
	public void clicked(Player player, ClickType clickType) {
		new CrateEditItemMenu(crate, crateItem).openMenu(player);
	}
}