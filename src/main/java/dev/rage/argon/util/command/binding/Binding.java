package dev.rage.argon.util.command.binding;

import dev.rage.argon.util.command.provider.Provider;

public interface Binding<T> {

	Class<? extends T> getInstance();
	
	Provider<T> getProvider();
	
	void setProvider(Provider<T> provider);
}
