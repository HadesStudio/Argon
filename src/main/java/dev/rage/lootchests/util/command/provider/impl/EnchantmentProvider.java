package dev.rage.lootchests.util.command.provider.impl;

import com.google.common.collect.Lists;
import dev.rage.lootchests.util.command.provider.Provider;
import dev.rage.lootchests.util.command.provider.exception.ProviderException;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;

import java.util.List;

public class EnchantmentProvider implements Provider<Enchantment> {

	@Override
	public Enchantment provide(CommandSender sender, String source) {
		if (Enchantment.getByName(source.toUpperCase()) == null) {
			throw new ProviderException("Enchantment named '<source>' not found.", source);
		}
		
		return Enchantment.getByName(source.toUpperCase());
	}

	@Override
	public List<String> suggest(CommandSender sender, String source) {
		List<String> completions = Lists.newArrayList();
		
		for (Enchantment enchant : Enchantment.values()) {
			if (match(enchant.getName(), source)) {
				completions.add(enchant.getName().toLowerCase());
			}
		}
		
		return completions;
	}
}