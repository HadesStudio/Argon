package dev.rage.argon.util;

import org.bukkit.Location;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LocationUtil {

    public String formatLocation(Location location, boolean world) {
    	return (world ? location.getWorld().getName() + ", " : "") + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();
    }
}
