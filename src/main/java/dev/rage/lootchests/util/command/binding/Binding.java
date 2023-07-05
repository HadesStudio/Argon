package dev.rage.lootchests.util.command.binding;

import dev.rage.lootchests.util.command.provider.Provider;

public interface Binding<T> {

	Class<? extends T> getInstance();
	
	Provider<T> getProvider();
	
	void setProvider(Provider<T> provider);
}
