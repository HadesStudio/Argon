package dev.astro.argon.util.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public abstract class CommandRegistrar {

	private final Map<String, CommandContext> commands;
	
	public abstract void registerObject(JavaPlugin owner, Object object);
	
	public abstract void registerCommand(JavaPlugin owner, CommandContext context);
}
