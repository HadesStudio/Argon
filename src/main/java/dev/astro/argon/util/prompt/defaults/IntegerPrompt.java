package dev.astro.argon.util.prompt.defaults;

import dev.astro.argon.util.JavaUtil;
import dev.astro.argon.util.prompt.Prompt;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class IntegerPrompt extends Prompt<Integer> {

	@Override
	public void handleFailed(Player player, String input) {
		player.sendMessage(ChatColor.RED + "Integer value '" + ChatColor.WHITE + input + ChatColor.RED + "' is invalid.");
	}
	
	@Override
	public Integer handleInput(Player player, String input) {

		return JavaUtil.tryParseInt(input);
	}
}
