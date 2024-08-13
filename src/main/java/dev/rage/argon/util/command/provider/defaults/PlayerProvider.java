package dev.rage.argon.util.command.provider.defaults;

import dev.rage.argon.util.JavaUtil;
import dev.rage.argon.util.command.provider.Provider;
import dev.rage.argon.util.command.provider.exception.ProviderException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
	public List<String> suggest(CommandSender sender, List<String> flags) {
		return Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
	}
}
