package dev.astro.argon.util.command.binding;

import dev.astro.argon.util.command.provider.Provider;

public interface BindingBuilder<T> {

	void toProvider(Provider<T> provider);
}
