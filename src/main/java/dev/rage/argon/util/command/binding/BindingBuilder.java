package dev.rage.argon.util.command.binding;

import dev.rage.argon.util.command.provider.Provider;

public interface BindingBuilder<T> {

	void toProvider(Provider<T> provider);
}
