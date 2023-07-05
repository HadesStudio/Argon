package dev.rage.lootchests.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LocationUtil {

    public String formatLocation(Location location, boolean world) {
    	return (world ? location.getWorld().getName() + ", " : "") + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();
    }
}
