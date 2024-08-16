package dev.rage.argon.util.menu.button;

import com.cryptomorin.xseries.XSound;
import dev.rage.argon.util.ItemBuilder;
import dev.rage.argon.util.ReflectionUtil;
import dev.rage.argon.util.version.ServerVersion;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Button {
	
    public static Button placeholder(ItemStack itemStack, String title) {
        return new Button() {

        	@Override
            public ItemStack getButtonItem(Player player) {
        		ItemBuilder itemBuilder = new ItemBuilder(itemStack);
        		itemBuilder.setName(title);

                return itemBuilder.build();
            }
        };
    }
    
    public static void playFail(Player player) {
        player.playSound(player.getLocation(), XSound.BLOCK_GRASS_BREAK.parseSound(), 20.0f, 0.1f);
    }
    
    public static void playSuccess(Player player) {
        player.playSound(player.getLocation(), XSound.UI_BUTTON_CLICK.parseSound(), 20.0f, 15.0f);
    }
    
    public static void playNeutral(Player player) {
        player.playSound(player.getLocation(), XSound.BLOCK_LEVER_CLICK.parseSound(), 20.0f, 1.0f);
    }
    
    public String getName(Player player) {
    	return null;
    }

    public List<String> getDescription(Player player) {
    	return null;
    }

    public Material getMaterial(Player player) {
    	return null;
    }

    public byte getDamageValue(Player player) {
        return 0;
    }
    
    public int getAmount(Player player) {
    	return 1;
    }
    
    public ItemStack getButtonItem(Player player) {
    	if (getMaterial(player) == null) {
    		return null;
    	}
    	
    	ItemBuilder builder = new ItemBuilder(getMaterial(player));
    	builder.setDurability(getDamageValue(player));
    	builder.setAmount(getAmount(player));
    	
    	if (getName(player) != null) {
    		builder.setName(getName(player));
    	}
    	
    	if (getDescription(player) != null) {
    		builder.setLore(getDescription(player));
    	}
    	
    	return builder.build();
    }
    
    public void clicked(Player player, ClickType clickType) { }
    
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) { }
    
    public boolean shouldCancel(Player player, ClickType clickType) { return true; }
    
    public boolean shouldUpdate(Player player, ClickType clickType) { return false; }
}
