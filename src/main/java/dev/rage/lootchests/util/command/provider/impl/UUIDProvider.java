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

public class UUIDProvider implements Provider<UUID> {

	@Override
	public UUID provide(CommandSender sender, String source) {
		
		if (source.equalsIgnoreCase("self")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				
				return player.getUniqueId();
			}
		}
		
		if (JavaUtil.isUUID(source)) {
			UUID uuid = UUID.fromString(source);
			if (Bukkit.getPlayer(uuid) != null) {
				return uuid;
			} else {
				throw new ProviderException("Player with name or with UUID '<source>' not found.", source);
			}
		}
		
		Player player = Bukkit.getPlayer(source);
		if (player == null) {
			throw new ProviderException("Player with name or with UUID '<source>' not found.", source);
		}
		
		return player.getUniqueId();
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
