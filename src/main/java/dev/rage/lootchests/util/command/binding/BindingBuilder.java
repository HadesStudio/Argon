package dev.rage.lootchests.util.command.binding;

import dev.rage.lootchests.util.command.provider.Provider;

public interface BindingBuilder<T> {

	void toProvider(Provider<T> provider);
}
