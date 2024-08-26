package dev.astro.argon.util.command.bukkit;

import dev.astro.argon.util.command.CommandConfig;
import dev.astro.argon.util.command.CommandHandler;
import dev.astro.argon.util.command.CommandRegistrar;
import dev.astro.argon.util.command.binding.Binder;
import dev.astro.argon.util.command.internal.InternalBinder;
import dev.astro.argon.util.command.internal.InternalCommandConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitCommandHandler implements CommandHandler {
	
	private final JavaPlugin plugin;

	private final CommandConfig commandConfig;
	private final CommandRegistrar registrar;
	private final Binder binder;
	public BukkitCommandHandler(JavaPlugin plugin, CommandConfig commandConfig) {
		this.plugin = plugin;

		this.commandConfig = commandConfig;
		this.registrar = new BukkitCommandRegistrar(this);
		this.binder = new InternalBinder();
	}

	public BukkitCommandHandler(JavaPlugin plugin) {
		this(plugin, new InternalCommandConfig());
	}

	@Override
	public CommandConfig getConfig() {
		return commandConfig;
	}

	@Override
	public CommandRegistrar getRegistrar() {
		return registrar;
	}
	
	@Override
	public Binder getBinder() {
		return binder;
	}

	@Override
	public void register(Object object) {
		registrar.registerObject(plugin, object);
	}

	@Override
	public void register(Object... objects) {
		for (Object object : objects) register(object);
	}
}
