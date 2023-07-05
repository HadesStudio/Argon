package dev.rage.lootchests.util.config.serialize.defaults;

import dev.rage.lootchests.util.ColorUtil;
import dev.rage.lootchests.util.config.serialize.ConfigSerializer;
import org.bukkit.ChatColor;

public class ChatColorSerializer implements ConfigSerializer<ChatColor> {

	@Override
	public String toString(ChatColor object) {
		return object.name();
	}

	@Override
	public ChatColor fromString(String source) {
		if (source.contains("&")) {
			return ColorUtil.getChatColorFromCode(source);
		}
		
		return ColorUtil.getChatColorFromString(source);
	}
}