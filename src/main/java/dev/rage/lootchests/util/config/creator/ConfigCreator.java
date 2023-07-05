package dev.rage.lootchests.util.config.creator;

import dev.rage.lootchests.util.UnicodeUtil;
import dev.rage.lootchests.util.config.Config;
import dev.rage.lootchests.util.config.creator.annotation.Header;
import dev.rage.lootchests.util.config.creator.annotation.Path;
import dev.rage.lootchests.util.config.serialize.ConfigSerializer;
import dev.rage.lootchests.util.config.serialize.defaults.ChatColorSerializer;
import dev.rage.lootchests.util.config.serialize.defaults.ItemStackSerializer;
import dev.rage.lootchests.util.config.serialize.defaults.LocationSerializer;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ConfigCreator implements Listener {

	@Getter private final Config config;
	
	private final Map<Class<?>, ConfigSerializer> serializers = new HashMap<>();
	
	public ConfigCreator(JavaPlugin plugin, String file) {
		serializers.put(ChatColor.class, new ChatColorSerializer());
		serializers.put(ItemStack.class, new ItemStackSerializer());
		serializers.put(Location.class, new LocationSerializer());
		
		this.config = new Config(plugin, file);
		
		reload();
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onDisable(PluginDisableEvent event) {
		save();
	}
	
	private void setup() {
		if (this.getClass().isAnnotationPresent(Header.class)) {
			StringBuilder header = new StringBuilder();
			for (String line : this.getClass().getAnnotation(Header.class).lines()) {
				header.append(line).append(UnicodeUtil.SEPARATOR);
			}
			
			config.options().header(header.toString());
		}
		
		for (Field field : this.getClass().getDeclaredFields()) {
			if (!field.isAnnotationPresent(Path.class)) {
				continue;
			}
			
			Path path = field.getAnnotation(Path.class);
			if (config.contains(path.id())) {
				try {
					
					field.setAccessible(true);
					
					Object update = deserialize(path, field.getType());					
					field.set(null, update);
					
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			} else {
				config.set(path.id(), serialize(path, field));
			}
		}
		
		config.save();
	}
	
	public void reload() {
		config.reload();
		setup();
	}
	
	public <T> void updateField(String path, Object value) {
		if (serializers.containsKey(value.getClass())) {
			config.set(path, (T) value, (Class<? extends ConfigSerializer<T>>) serializers.get(value.getClass()).getClass());
		} else {
			config.set(path, value);
		}
		config.save();
		
		setup();
	}
	
	private void save() {
		for (Field field : this.getClass().getDeclaredFields()) {
			if (!field.isAnnotationPresent(Path.class)) {
				continue;
			}
			
			Path path = field.getAnnotation(Path.class);
			
			config.set(path.id(), serialize(path, field));
		}
	}
	
	private <T> Object deserialize(Path path, Class<?> type) {
		if (serializers.containsKey(type)) {
			return config.get(path.id(), (Class<? extends ConfigSerializer<T>>) serializers.get(type).getClass());
		}
		
    	return config.get(path.id());
    }
	
	private Object serialize(Path path, Field field) {
		if (serializers.containsKey(field.getType())) {
			try {
				return serializers.get(field.getType()).toString(field.get(null));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		try {
			return field.get(null);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}