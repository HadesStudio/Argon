package dev.astro.argon.util.config;

import dev.astro.argon.util.config.serialize.ConfigSerializer;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

@Getter
public class Config extends YamlConfiguration {

    private String file;
    private JavaPlugin plugin;

    public Config(JavaPlugin plugin, String file) {
        this.plugin = plugin;
        this.file = file;

        this.createFile();
        this.load();
    }

    private void createFile() {
        File file = new File(plugin.getDataFolder(), this.file);

        // Create any directories leading to this file if we have too
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            if (!file.exists()) {
                if (plugin.getResource(this.file) != null) {
                    plugin.saveResource(this.file, false);
                } else {
                    file.createNewFile();
                }
            }
            super.load(file);
        } catch (Exception ex) {
            plugin.getLogger().log(Level.SEVERE, "Config \"" + this.file + "\" could not be loaded!");
            ex.printStackTrace();
        }
    }

    public void deleteFile() {
        File file = new File(plugin.getDataFolder(), this.file);
        file.delete();
    }

    public void load() {
        try {
            File file = new File(plugin.getDataFolder(), this.file);

            if (!file.exists()) {
                this.createFile();
            } else {
            	super.load(file);
            }
        } catch (Exception ex) {
            plugin.getLogger().log(Level.SEVERE, "Config \"" + this.file + "\" could not be loaded!");
            ex.printStackTrace();
        }
    }


    public void save() {
        File file = new File(plugin.getDataFolder(), this.file);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            super.save(file);
        } catch (Exception ex) {
            plugin.getLogger().log(Level.SEVERE, "Config \"" + this.file + "\" could not be saved!");
            ex.printStackTrace();
        }
    }
    
    public void reload() {
    	this.load();
    	this.save();
    }
    
    public <T> void set(String path, T object, Class<? extends ConfigSerializer<T>> serializerClass) {
    	try {
			ConfigSerializer<T> serializer = serializerClass.newInstance();
			
			super.set(path, serializer.toString(object));
		} catch (Exception e) {
			plugin.getLogger().log(Level.SEVERE, "Config \"" + this.file + "\" could not set '" + path + "'!");
            e.printStackTrace();
		}
    }
    
    public <T> T get(String path, Class<? extends ConfigSerializer<T>> serializerClass) {
    	try {
    		ConfigSerializer<T> serializer = serializerClass.newInstance();
    		
    		return serializer.fromString(super.getString(path));
		} catch (Exception e) {
			plugin.getLogger().log(Level.SEVERE, "Config \"" + this.file + "\" could not get '" + path + "'!");
            e.printStackTrace();
            
            return null;
		}
    }
}