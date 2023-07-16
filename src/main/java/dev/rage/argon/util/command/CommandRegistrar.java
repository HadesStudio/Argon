package dev.rage.argon.util.command;

import org.bukkit.plugin.java.JavaPlugin;

public interface CommandRegistrar {

	void registerObject(JavaPlugin owner, Object object);
	
	void registerClass(JavaPlugin owner, Class<?> clazz);
}
