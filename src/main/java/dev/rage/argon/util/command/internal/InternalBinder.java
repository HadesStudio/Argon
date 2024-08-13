package dev.rage.argon.util.command.internal;

import com.google.common.base.Function;
import dev.rage.argon.util.command.binding.Binder;
import dev.rage.argon.util.command.binding.Binding;
import dev.rage.argon.util.command.binding.BindingBuilder;
import dev.rage.argon.util.command.provider.Provider;
import dev.rage.argon.util.command.provider.defaults.ChatColorProvider;
import dev.rage.argon.util.command.provider.defaults.PlayerProvider;
import dev.rage.argon.util.command.provider.defaults.UUIDProvider;
import dev.rage.argon.util.command.provider.defaults.enums.EnumBinding;
import dev.rage.argon.util.command.provider.exception.ProviderException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class InternalBinder implements Binder {

	private final Map<Class<?>, Binding<?>> bindings = new HashMap<>();
	
	public InternalBinder() {
        bind(String.class).toProvider(new Provider<String>() {
            @Override
            public String provide(CommandSender sender, String source) {
                return source;
            }
        });

		bind(ChatColor.class).toProvider(new ChatColorProvider());
        bind(Player.class).toProvider(new PlayerProvider());
        bind(UUID.class).toProvider(new UUIDProvider());

		bindNumber(Integer::parseInt, "Entry '<source>' is not a valid integer.", Integer.class, int.class);
        bindNumber(Float::parseFloat, "Entry '<source>' is not a valid float.", Float.class, float.class);
        bindNumber(Double::parseDouble, "Entry '<source>' is not a valid double.", Double.class, double.class);
        bindNumber(Short::parseShort, "Entry '<source>' is not a valid short.", Short.class, short.class);
        
        bind(Boolean.class).toProvider(this::parseBoolean);
        bind(boolean.class).toProvider(this::parseBoolean);
	}
	
	@Override
	public Map<Class<?>, Binding<?>> getBindings() {
		return bindings;
	}

	@Override
	public <T> Binding<T> getBinding(Class<T> clazz) {
		Binding<?> binding = getBindings().get(clazz);
		
		if (binding == null && clazz.isEnum()) {
			binding = new EnumBinding(clazz);
		}
		
		return (binding == null ? null : (Binding<T>) binding);
	}

	@Override
	public <T> BindingBuilder<T> bind(Class<T> clazz) {
		return new InternalBindingBuilder<>(getBindings(), new InternalBinding<>(clazz));
	}
	
	private <N extends Number> void bindNumber(Function<String, N> function, String message, Class<N>... classes) {
        Provider<N> provider = (sender, source) -> {
            try {
                return function.apply(source);
            } catch (Exception e) {
                throw new ProviderException(message, source);
            }
        };

        for (Class<N> clazz : classes) {
            bind(clazz).toProvider(provider);
        }
    }

    private Boolean parseBoolean(CommandSender sender, String source) {
        if (source.equalsIgnoreCase("yes") || source.equalsIgnoreCase("true")) {
            return true;
        }
        
        if (source.equalsIgnoreCase("no") || source.equalsIgnoreCase("false")) {
            return false;
        }
        
        throw new ProviderException("Entry '<source>' is not a valid boolean. <gray>(true/false)", source);
    }
}
