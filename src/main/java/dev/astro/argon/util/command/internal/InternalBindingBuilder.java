package dev.astro.argon.util.command.internal;

import com.google.common.base.Preconditions;
import dev.astro.argon.util.command.binding.Binding;
import dev.astro.argon.util.command.binding.BindingBuilder;
import dev.astro.argon.util.command.provider.Provider;
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
