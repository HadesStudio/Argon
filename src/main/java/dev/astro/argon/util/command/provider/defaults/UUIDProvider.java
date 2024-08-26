package dev.astro.argon.util.command.provider.defaults;

import dev.astro.argon.util.JavaUtil;
import dev.astro.argon.util.command.provider.Provider;
import dev.astro.argon.util.command.provider.exception.ProviderException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
	public List<String> suggest(CommandSender sender, List<String> flags) {
		return Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
	}
}
