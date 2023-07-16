package dev.rage.argon.util.command.provider.impl;

import dev.rage.argon.util.command.provider.Provider;
import dev.rage.argon.util.command.provider.exception.ProviderException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class OfflinePlayerProvider implements Provider<OfflinePlayer> {

	@Override
	public OfflinePlayer provide(CommandSender sender, String source) {
		if(source.equalsIgnoreCase("self")) {
			return ((OfflinePlayer) sender);
		}
		
		if (Bukkit.getOfflinePlayer(source) != null) {
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(source);
			
			if(!offlinePlayer.hasPlayedBefore()) {
				return null;
			}
			
			return offlinePlayer;
		}
		
		throw new ProviderException("Offline player named '<source>' not found.", source);
	}
}
