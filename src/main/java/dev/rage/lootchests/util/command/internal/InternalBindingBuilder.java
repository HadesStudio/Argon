package dev.rage.lootchests.util.command.internal;

import com.google.common.base.Preconditions;
import dev.rage.lootchests.util.command.binding.Binding;
import dev.rage.lootchests.util.command.binding.BindingBuilder;
import dev.rage.lootchests.util.command.provider.Provider;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class InternalBindingBuilder<T> implements BindingBuilder<T> {

	private final Map<Class<?>, Binding<?>> map;
	private final Binding<T> binding;
	
	@Override
	public void toProvider(Provider<T> provider) {
		Preconditions.checkState(binding.getProvider() == null, "Provider already bound.");
		
		binding.setProvider(provider);
		
		map.put(binding.getInstance(), binding);
	}
}
