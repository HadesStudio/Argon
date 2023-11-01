package dev.rage.argon.profile;

import dev.rage.argon.ArgonPlugin;
import dev.rage.argon.profile.command.ProfileCommands;
import dev.rage.argon.util.config.Config;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class ProfileHandler {

    private final List<Profile> profiles;

    private final Config config;

    public ProfileHandler(ArgonPlugin instance) {
        profiles = new ArrayList<>();

        config = new Config(instance, "data/profiles.yml");

        ArgonPlugin.get().getCommandHandler().register(new ProfileCommands());

        Bukkit.getServer().getPluginManager().registerEvents(new ProfileListener(this), instance);
    }

    public Profile getProfile(Player player) {
        Profile profile = findProfile(player.getUniqueId());

        if (profile == null) return new Profile(player.getUniqueId());

        return profile;
    }

    public Profile findProfile(UUID id) {
        return profiles.stream().filter(profile -> profile.getId().equals(id)).findFirst().orElse(null);
    }

    public void loadProfile(UUID id) {
        Profile profile = new Profile(id);

        if (config.contains("profiles." + id.toString())) {
            Map<UUID, Integer> storedKeys = new HashMap<>();
            Map<UUID, Integer> virtualKeys = new HashMap<>();

            for (String key : config.getConfigurationSection("profiles." + id.toString() + ".stored-keys").getKeys(false)) {
                storedKeys.put(UUID.fromString(key), config.getInt("profiles." + id.toString() + ".stored-keys." + key));
            }

            for (String key : config.getConfigurationSection("profiles." + id.toString() + ".virtual-keys").getKeys(false)) {
                virtualKeys.put(UUID.fromString(key), config.getInt("profiles." + id.toString() + ".virtual-keys." + key));
            }

            profile.setStoredKeys(storedKeys);
            profile.setVirtualKeys(virtualKeys);
        }

        profiles.add(profile);
    }

    public void handleDisable() {
        config.set("profiles", null);

        profiles.forEach(profile -> {
            profile.getStoredKeys().forEach((key, value) -> {
                config.set("profiles." + profile.getId().toString() + ".stored-keys." + key.toString(), value);
            });

            profile.getVirtualKeys().forEach((key, value) -> {
                config.set("profiles." + profile.getId().toString() + ".virtual-keys." + key.toString(), value);
            });
        });

        config.save();
    }
}
