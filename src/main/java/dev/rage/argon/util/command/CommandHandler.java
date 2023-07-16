package dev.rage.argon.util.command;

import dev.rage.argon.util.command.binding.Binder;

public interface CommandHandler {
	
	CommandContext getMainContext();
	
	CommandConfig getConfig();
	
	Binder getBinder();
		
	void register(Object object);
}