package dev.astro.argon.util.command.provider.defaults;

import com.google.common.collect.Maps;
import dev.astro.argon.util.command.provider.Provider;
import dev.astro.argon.util.command.provider.exception.ProviderException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatColorProvider implements Provider<ChatColor> {

	private final Map<String, ChatColor> colors = Maps.newConcurrentMap();

	public ChatColorProvider() {
		colors.put("pink", ChatColor.LIGHT_PURPLE);
		colors.put("orange", ChatColor.GOLD);
		colors.put("purple", ChatColor.DARK_PURPLE);

		for (ChatColor chatColor : ChatColor.values()) {
			colors.put(chatColor.name().toLowerCase().replace("_", ""), chatColor);
		}
		
	}
	
	@Override
	public ChatColor provide(CommandSender sender, String source) {
		if(!colors.containsKey(source.toLowerCase())) {
			throw new ProviderException("Color named '<source>' not found.", source);
		}
		
		return getColorFromName(source);
	}
	
	@Override
	public List<String> suggest(CommandSender sender, List<String> flags) {
		return new ArrayList<>(colors.keySet());
	}
	
	private ChatColor getColorFromName(String name) {
		if (colors.containsKey(name.trim().toLowerCase())) {
			return colors.get(name.trim().toLowerCase());
		}

		ChatColor color;

		try {
			color = ChatColor.valueOf(name.toUpperCase().replace(" ", "_"));
		} catch (Exception e) {
			return null;
		}

		return color;
	}
}