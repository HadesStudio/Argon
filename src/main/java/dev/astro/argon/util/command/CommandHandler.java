package dev.astro.argon.util.command;

import dev.astro.argon.util.command.binding.Binder;

public interface CommandHandler {

	CommandConfig getConfig();

	CommandRegistrar getRegistrar();
	
	Binder getBinder();
		
	void register(Object object);
	
	void register(Object... objects);
}