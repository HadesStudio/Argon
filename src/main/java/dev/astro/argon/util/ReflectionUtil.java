package dev.astro.argon.util;

import dev.astro.argon.util.version.ServerVersion;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class ReflectionUtil {

    @Getter private static final ServerVersion serverVersion;

    static {
        serverVersion = findVersion();
    }

    private String getNmsVersion() {
        try {
            return (Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);
        } catch (Exception ignored) {}

        return Bukkit.getServer().getVersion();
    }

    public ItemStack getSkullItem() {
        try {
            return new ItemStack(Material.valueOf("PLAYER_HEAD"), 1);
        } catch (Exception e) {
            return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
        }
    }


    private ServerVersion findVersion() {
        String currentVersion = getNmsVersion();
        String[] split = currentVersion.split("-");

        String fixedVersion = split[0].replace(".", "_");

        for (ServerVersion version : ServerVersion.values()) {
            String name = version.name();

            name = name.replace("MINECRAFT_", "v");
            name = (name.length() <= 4 ? name.substring(0, 4) : name.substring(0, 5));

            if (name.contains(fixedVersion)) return version;
        }

        return ServerVersion.UNKNOWN;
    }
}
