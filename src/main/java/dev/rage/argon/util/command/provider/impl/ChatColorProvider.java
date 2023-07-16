package dev.rage.argon.util.command.provider.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.rage.argon.util.command.provider.Provider;
import dev.rage.argon.util.command.provider.exception.ProviderException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

public class ChatColorProvider implements Provider<ChatColor> {

	private final Map<String, ChatColor> colorMap = Maps.newConcurrentMap();

	public ChatColorProvider() {
		colorMap.put("pink", ChatColor.LIGHT_PURPLE);
		colorMap.put("orange", ChatColor.GOLD);
		colorMap.put("purple", ChatColor.DARK_PURPLE);

		for (ChatColor chatColor : ChatColor.values()) {
			colorMap.put(chatColor.name().toLowerCase().replace("_", ""), chatColor);
		}
		
	}
	
	@Override
	public ChatColor provide(CommandSender sender, String source) {
		if(!colorMap.containsKey(source.toLowerCase())) {
			throw new ProviderException("Color named '<source>' not found.", source);
		}
		
		return getColorFromName(source);
	}
	
	@Override
	public List<String> suggest(CommandSender sender, String source) {
		List<String> completions = Lists.newArrayList();
		
		for (String color : colorMap.keySet()) {
			if (match(color, source)) {
				completions.add(color);
			}
		}
		
		return (completions);
	}
	
	private ChatColor getColorFromName(String name) {
		if (colorMap.containsKey(name.trim().toLowerCase())) {
			return colorMap.get(name.trim().toLowerCase());
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