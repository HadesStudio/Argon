package dev.rage.lootchests.util.command.provider.exception;

import dev.rage.lootchests.util.config.Replacement;
import org.bukkit.ChatColor;

public class ProviderException extends RuntimeException {

	private static final long serialVersionUID = -7427299241098346359L;

	public ProviderException(Throwable cause) {
        super(cause);
    }
	
	public ProviderException(String message, String source) {
		super(process(message, source));
	}
	
	public ProviderException(String message) {
		this(message, null);
	}
	
	private static String process(String message, String source) {
		Replacement replacement = new Replacement(message);
		
		if (source != null) replacement.add("<source>", ChatColor.WHITE + source + ChatColor.RED);
		
		for (ChatColor color : ChatColor.values()) {
			replacement.add("<" + color.name().toLowerCase() + ">", color.toString());
		}
		
		return ChatColor.RED + replacement.toString();
	}
}
