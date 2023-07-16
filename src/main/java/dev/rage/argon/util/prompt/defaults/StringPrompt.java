package dev.rage.argon.util.prompt.defaults;

import dev.rage.argon.util.TextUtil;
import dev.rage.argon.util.prompt.Prompt;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class StringPrompt extends Prompt<String> {

	@Override
	public void handleFailed(Player player, String input) {
		player.sendMessage(ChatColor.RED + "The input '" + ChatColor.WHITE + input + ChatColor.RED + "' cannot be null.");
	}

	@Override
	public String handleInput(Player player, String input) {
		if(TextUtil.isNull(input)) {
			return null;
		}
		
		return input;
	}
}
