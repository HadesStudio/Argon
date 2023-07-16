package dev.rage.argon.util.command.internal;

import dev.rage.argon.util.command.binding.Binding;
import dev.rage.argon.util.command.provider.Provider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InternalBinding<T> implements Binding<T> {

	private final Class<?> instance;
	private Provider<T> provider;
	
	@Override
	public Class<? extends T> getInstance() {
		return (Class<? extends T>) instance;
	}

	@Override
	public Provider<T> getProvider() {
		return provider;
	}

	@Override
	public void setProvider(Provider<T> provider) {
		this.provider = provider;
	}
}
