package dev.astro.argon.config;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This code has been created by
 * gatogamer#6666 A.K.A. gatogamer.
 * If you want to use my code, please
 * ask first, and give me the credits.
 * Arigato! n.n
 */
@Getter
public class ConfigFile extends YamlConfiguration {
    private final String fileName;
    private final Plugin plugin;
    private final File file;

    public ConfigFile(Plugin plugin, String filename, String fileExtension, File folder) {
        this.plugin = plugin;
        this.fileName = filename + (filename.endsWith(fileExtension) ? "" : fileExtension);
        this.file = new File(folder, this.fileName);
        this.createFile();
    }

    public ConfigFile(Plugin plugin, String fileName) {
        this(plugin, fileName, ".yml");
    }

    public ConfigFile(Plugin plugin, String fileName, String fileExtension) {
        this(plugin, fileName, fileExtension, plugin.getDataFolder());
    }

    public ConfigFile(Plugin plugin, String fileName, String fileExtension, String filePath) {
        this(plugin, fileName, fileExtension, new File(plugin.getDataFolder().getAbsolutePath() + "/" + filePath));
    }

    private void createFile() {
        try {
            if (!file.exists()) {
                if (this.plugin.getResource(this.fileName) != null) {
                    this.plugin.saveResource(this.fileName, false);
                } else {
                    this.save(file);
                }
                this.load(file);
                return;
            }
            this.load(file);

            this.save(file);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        try {
            load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getString(String path) {
        String s = super.getString(path);
        if (s == null) {
            //Midnight.log(path + " is null xd");
            return "token not found:"+path;
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @Override
    public String getString(String path, String def) {
        String s = super.getString(path, def);
        /*if (s == null) {
            Midnight.log(path + " is null xd");
        }*/
        return ChatColor.translateAlternateColorCodes('&', s != null ? s : def);
    }

    @Override
    public List<String> getStringList(String path) {
        List<String> list = super.getStringList(path);
        //list.replaceAll(MessagesUtils::colorize);
        list.replaceAll(s -> ChatColor.translateAlternateColorCodes('&', s));
        return list;
    }
}