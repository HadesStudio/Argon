package dev.rage.lootchests.util.command.provider.impl;

import com.google.common.collect.Lists;
import dev.rage.lootchests.util.BukkitUtil;
import dev.rage.lootchests.util.JavaUtil;
import dev.rage.lootchests.util.command.provider.Provider;
import dev.rage.lootchests.util.command.provider.exception.ProviderException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class PlayerProvider implements Provider<Player> {

	@Override
	public Player provide(CommandSender sender, String source) {
		if(source.equalsIgnoreCase("self")) {
			return ((Player) sender);
		}
		
		if (JavaUtil.isUUID(source)) {
			UUID uuid = UUID.fromString(source);
			
			if(Bukkit.getPlayer(uuid) != null) {
				return Bukkit.getPlayer(uuid);
			}
		}
		
		if (Bukkit.getPlayer(source) != null) {
			return Bukkit.getPlayer(source);
		}
		
		throw new ProviderException("Player with name or with UUID '<source>' not found.", source);
	}

	@Override
	public List<String> suggest(CommandSender sender, String source) {
		List<String> completions = Lists.newArrayList();
		
		for (Player player : BukkitUtil.getOnlinePlayers()) {
			if (match(player.getName(), source)) {
				completions.add(player.getName());
			}
		}
		
		return completions;
	}
}
