package dev.astro.argon.util.command.provider;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public interface Provider<T> {

    T provide(CommandSender sender, String source);

    default List<String> suggest(CommandSender sender, List<String> flags) {
    	return Collections.emptyList();
    }

}
