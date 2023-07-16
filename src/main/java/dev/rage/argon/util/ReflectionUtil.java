package dev.rage.argon.util;

import dev.rage.argon.util.reflect.Reflection;
import dev.rage.argon.util.version.ServerVersion;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@UtilityClass
public class ReflectionUtil {

    public String getNmsVersion() {
        return (Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);
    }

    public ServerVersion getServerVersion() {
        for (ServerVersion version : ServerVersion.values()) {
            String name = version.name();

            name = name.replace("MINECRAFT_", "v");
            name = (name.length() <= 4 ? name.substring(0, 4) : name.substring(0, 5));

            if (getNmsVersion().contains(name)) return version;
        }

        return ServerVersion.UNKNOWN;
    }

    public ItemStack getItemInHand(Player player) {
        PlayerInventory inventory = player.getInventory();
        if (getServerVersion().isBefore(ServerVersion.MINECRAFT_1_9)) {
            return inventory.getItemInHand();
        }

        return (ItemStack) Reflection.getMethod(PlayerInventory.class, "getItemInMainHand").invoke(inventory);
    }
}
