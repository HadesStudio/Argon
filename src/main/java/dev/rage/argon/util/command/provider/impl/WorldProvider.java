package dev.rage.argon.util.command.provider.impl;

import com.google.common.collect.Lists;
import dev.rage.argon.util.command.provider.Provider;
import dev.rage.argon.util.command.provider.exception.ProviderException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.List;

public class WorldProvider implements Provider<World> {

	@Override
	public World provide(CommandSender sender, String source) {
		World world = Bukkit.getWorld(source);
		if(world == null) {
			throw new ProviderException("World named '<source>' not found.", source);
		}
		
		return world;
	}

	@Override
	public List<String> suggest(CommandSender sender, String source) {
		List<String> completions = Lists.newArrayList();
		
		for (World world : Bukkit.getWorlds()) {
			if (match(world.getName(), source)) {
				completions.add(world.getName());
			}
		}
		
		return completions;
	}
}