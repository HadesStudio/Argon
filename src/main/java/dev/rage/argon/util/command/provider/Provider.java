package dev.rage.argon.util.command.provider;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public interface Provider<T> {

	T provide(CommandSender sender, String source);
	
	default List<String> suggest(CommandSender sender, String source) {
		return Collections.emptyList();
	}
	
	default boolean match(String word, String source) {
		return StringUtils.startsWithIgnoreCase(word, source);
	}
}
