package dev.rage.lootchests.util.command;

import dev.rage.lootchests.util.command.binding.Binder;

public interface CommandHandler {
	
	CommandContext getMainContext();
	
	CommandConfig getConfig();
	
	Binder getBinder();
		
	void register(Object object);
}