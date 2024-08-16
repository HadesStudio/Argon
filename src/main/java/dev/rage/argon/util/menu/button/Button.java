package dev.rage.argon.util.menu.button;

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
	
    public static Button placeholder(Material material, byte data, String title) {
        return new Button() {

        	@Override
            public ItemStack getButtonItem(Player player) {
        		ItemBuilder itemBuilder = new ItemBuilder(material);
        		itemBuilder.setName(title);
        		if (data != -1) {
                    itemBuilder.setDurability(data);
                }

                return itemBuilder.build();
            }
        };
    }
    
    public static void playFail(Player player) {
        player.playSound(player.getLocation(), (ReflectionUtil.getServerVersion().isBefore(ServerVersion.MINECRAFT_1_9) ? Sound.CLICK : Sound.valueOf("BLOCK_GRASS_BREAK")), 20.0f, 0.1f);
    }
    
    public static void playSuccess(Player player) {
        player.playSound(player.getLocation(), (ReflectionUtil.getServerVersion().isBefore(ServerVersion.MINECRAFT_1_9) ? Sound.CLICK : (ReflectionUtil.getServerVersion().isBefore(ServerVersion.MINECRAFT_1_12) ? Sound.valueOf("BLOCK_NOTE_PLING") : Sound.valueOf("BLOCK_NOTE_BLOCK_PLING"))), 20.0f, 15.0f);
    }
    
    public static void playNeutral(Player player) {
        player.playSound(player.getLocation(), (ReflectionUtil.getServerVersion().isBefore(ServerVersion.MINECRAFT_1_9) ? Sound.CLICK : Sound.valueOf("BLOCK_LEVER_CLICK")), 20.0f, 1.0f);
    }
    
    public static void play(Player player, Sound sound, float pitch, float volume) {
    	player.playSound(player.getLocation(), (ReflectionUtil.getServerVersion().isBefore(ServerVersion.MINECRAFT_1_9) ? Sound.CLICK : Sound.valueOf("BLOCK_LEVER_CLICK")), pitch, volume);
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
