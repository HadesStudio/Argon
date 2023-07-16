package dev.rage.argon.util.command.provider.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.rage.argon.util.command.provider.Provider;
import dev.rage.argon.util.command.provider.exception.ProviderException;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

public class GameModeProvider implements Provider<GameMode> {

	private final Map<String, GameMode> gamemodeMap;
	
	public GameModeProvider() {
		gamemodeMap = Maps.newHashMap();
		
		for(GameMode gamemode : GameMode.values()) {
			gamemodeMap.put(gamemode.name().toLowerCase(), gamemode);
		}
		
		gamemodeMap.put("0", GameMode.SURVIVAL);
		gamemodeMap.put("s", GameMode.SURVIVAL);
		
		gamemodeMap.put("1", GameMode.CREATIVE);
		gamemodeMap.put("c", GameMode.CREATIVE);
		
		gamemodeMap.put("2", GameMode.ADVENTURE);
		gamemodeMap.put("a", GameMode.ADVENTURE);
		
		gamemodeMap.put("3", GameMode.SPECTATOR);
		gamemodeMap.put("spec", GameMode.SPECTATOR);
	}
	
	@Override
	public GameMode provide(CommandSender sender, String source) {
		if(!gamemodeMap.containsKey(source.toLowerCase())) {
			throw new ProviderException("Gamemode named '<source>' not found.", source);
		}
		
		return gamemodeMap.get(source.toLowerCase());
	}

	@Override
	public List<String> suggest(CommandSender sender, String source) {
		List<String> completions = Lists.newArrayList();
		
		for (String mode : gamemodeMap.keySet()) {
			if (match(mode, source)) {
				completions.add(mode);
			}
		}
		
		return completions;
	}
}