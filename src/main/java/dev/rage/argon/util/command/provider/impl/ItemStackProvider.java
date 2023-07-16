package dev.rage.argon.util.command.provider.impl;

import com.google.common.collect.Lists;
import dev.rage.argon.util.JavaUtil;
import dev.rage.argon.util.command.provider.Provider;
import dev.rage.argon.util.command.provider.exception.ProviderException;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemStackProvider implements Provider<ItemStack> {

	@Override
	public ItemStack provide(CommandSender sender, String source) {
		
		boolean hasValue = source.contains(":");
		
		if(hasValue) {
			String[] split = source.split(":");
			if(JavaUtil.tryParseInt(split[1]) == null) {
				sender.sendMessage(ChatColor.RED + "Item value is invalid.");
				return null;
			}
			
			Integer id = JavaUtil.tryParseInt(split[1]);
			
			Material material = Material.matchMaterial(source);
			if(material != null) {
				ItemStack itemStack = new ItemStack(material, 1, Short.valueOf(String.valueOf(id)));
				
				return itemStack;
			}
			
		} else {
			Material material = Material.matchMaterial(source);
			if(material != null) {
				ItemStack itemStack = new ItemStack(material);
				
				return itemStack;
			}
		}
		
		throw new ProviderException("Item with name or ID '<source>' not found.", source);
	}

	@Override
	public List<String> suggest(CommandSender sender, String source) {
		List<String> completions = Lists.newArrayList();
		
		for (Material material : Material.values()) {
			if (match(material.name(), source)) {
				completions.add(material.name().toLowerCase());
			}
		}
		
		return completions;
	}
}
