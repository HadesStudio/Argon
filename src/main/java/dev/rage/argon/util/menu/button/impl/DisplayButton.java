package dev.rage.argon.util.menu.button.impl;

import dev.rage.argon.util.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class DisplayButton extends Button {
	
    private final ItemStack displayItem;
    
    @Override
    public ItemStack getButtonItem(Player player) {
        return displayItem;
    }
}
