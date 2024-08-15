package dev.rage.argon.util.menu.button.impl;

import com.cryptomorin.xseries.XMaterial;
import dev.rage.argon.config.MenusConfig;
import dev.rage.argon.util.ItemBuilder;
import dev.rage.argon.util.menu.Menu;
import dev.rage.argon.util.menu.button.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BackButton extends Button {
	
    private final Menu back;
    private final ItemStack backItem;
    
    public BackButton(Menu back) {
        this.back = back;
        this.backItem = XMaterial.RED_BED.parseItem();
    }
    
    public BackButton(Menu back, ItemStack backItem) {
		this.back = back;
		this.backItem = backItem;
	}
    
    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(backItem);

        builder.setName(MenusConfig.BACK_BUTTON_TITLE);
        builder.setLore(MenusConfig.BACK_BUTTON_LORE);

        return builder.build();
    }
    
    @Override
    public void clicked(Player player, ClickType clickType) {
        Button.playNeutral(player);
        back.openMenu(player);
    }
}
