package dev.rage.lootchests.util.command.bukkit;

import dev.rage.lootchests.util.command.CommandConfig;
import dev.rage.lootchests.util.command.CommandContext;
import dev.rage.lootchests.util.command.CommandHandler;
import dev.rage.lootchests.util.command.CommandRegistrar;
import dev.rage.lootchests.util.command.binding.Binder;
import dev.rage.lootchests.util.command.internal.InternalBinder;
import dev.rage.lootchests.util.command.internal.InternalCommandConfig;
import dev.rage.lootchests.util.command.internal.InternalCommandContext;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitCommandHandler implements CommandHandler {

	private final JavaPlugin plugin;
	
	@Setter private CommandConfig config;
	private final CommandContext rootContext;
    private final Binder binder;
	
    private final CommandRegistrar commandRegistrar;
    
	public BukkitCommandHandler(JavaPlugin plugin) {
		this(plugin, new InternalCommandConfig());
	}
	
	public BukkitCommandHandler(JavaPlugin plugin, CommandConfig config) {
		this.plugin = plugin;
		
		this.config = config;
		this.binder = new InternalBinder();
		this.rootContext = new InternalCommandContext(this);
		
		this.commandRegistrar = new BukkitCommandRegistrar(this);
	}
	
	@Override
	public CommandConfig getConfig() {
		return this.config;
	}

	@Override
	public CommandContext getMainContext() {
		return this.rootContext;
	}

	@Override
	public Binder getBinder() {
		return this.binder;
	}

	@Override
	public void register(Object object) {
		commandRegistrar.registerObject(plugin, object);
	}
}
