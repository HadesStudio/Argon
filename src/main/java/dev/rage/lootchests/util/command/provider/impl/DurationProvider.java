package dev.rage.lootchests.util.command.provider.impl;

import dev.rage.lootchests.util.command.provider.Provider;
import dev.rage.lootchests.util.command.provider.exception.ProviderException;
import dev.rage.lootchests.util.time.Duration;
import org.bukkit.command.CommandSender;

public class DurationProvider implements Provider<Duration> {

	@Override
	public Duration provide(CommandSender sender, String source) {
		Duration duration = Duration.fromString(source);

		if (duration.getValue() != -1) {
			return duration;
		}
		
		throw new ProviderException("No parse duration '<source>' found.", source);
	}
}
