package dev.rage.argon.util.command.binding;

import java.util.Map;

public interface Binder {

	Map<Class<?>, Binding<?>> getBindings();
	
	<T> Binding<T> getBinding(Class<T> clazz);
	
	<T> BindingBuilder<T> bind(Class<T> clazz);
}
