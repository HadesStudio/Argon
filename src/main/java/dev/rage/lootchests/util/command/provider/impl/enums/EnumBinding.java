package dev.rage.lootchests.util.command.provider.impl.enums;

import dev.rage.lootchests.util.command.binding.Binding;
import dev.rage.lootchests.util.command.provider.Provider;

public class EnumBinding<E extends Enum<E>> implements Binding<E> {

	private final Class<E> clazz;
	private Provider<E> provider;
	
	public EnumBinding(Class<E> clazz) {
		this.clazz = clazz;
		this.provider = new EnumProvider<>(clazz);
	}
	
	@Override
	public Class<? extends E> getInstance() {
		return clazz;
	}

	@Override
	public Provider<E> getProvider() {
		return provider;
	}

	@Override
	public void setProvider(Provider<E> provider) {
		this.provider = provider;
	}
}
