package dev.rage.argon.util.config.serialize.defaults;

import dev.rage.argon.util.ItemBuilder;
import dev.rage.argon.util.config.serialize.ConfigSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class ItemStackSerializer implements ConfigSerializer<ItemStack> {

	public static ItemStackSerializer INSTANCE = new ItemStackSerializer();

	@Override
	public String toString(ItemStack item) {
		if (item == null) {
			return "null";
		}
		
		return ItemBuilder.toBase64(new ItemStack[] {item});
	}
	
	@Override
	public ItemStack fromString(String source) {
		try {
			return ItemBuilder.fromBase64(source)[0];
		} catch (IOException e) { }
		
		return new ItemStack(Material.AIR);
	}
}