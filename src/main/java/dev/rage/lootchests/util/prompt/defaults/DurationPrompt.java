package dev.rage.lootchests.util.prompt.defaults;

import dev.rage.lootchests.util.prompt.Prompt;
import dev.rage.lootchests.util.time.Duration;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class DurationPrompt extends Prompt<Duration> {

	@Override
	public void handleFailed(Player player, String input) {
		player.sendMessage(ChatColor.RED + "Duration format '" + ChatColor.RESET + input + ChatColor.RED + "' is invalid.");
		player.sendMessage(ChatColor.RED + "Example format: 2d30m10s");
	}
	
	@Override
	public Duration handleInput(Player player, String input) {
		Duration duration = Duration.fromString(input);

		if (duration.getValue() != -1) {
			return duration;
		}
		
		return null;
	}
}
