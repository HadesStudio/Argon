package dev.rage.lootchests.util.config.serialize.defaults;

import dev.rage.lootchests.util.config.serialize.ConfigSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerializer implements ConfigSerializer<Location> {

	public static LocationSerializer INSTANCE = new LocationSerializer();
	
	@Override
	public String toString(Location location) {
		if(location != null) {
			String string = (location.getWorld() == null ? "world" : location.getWorld().getName())
					+ ","
					+ location.getX()
					+ ","
					+ location.getY()
					+ ","
					+ location.getZ()
					+ ","
					+ location.getYaw()
					+ ","
					+ location.getPitch();
			
			return string;
		}
		
		return null;
	}
	
	@Override
	public Location fromString(String source) {
		
		if (source == null) {
			return null;
		}
		
		if (source.isEmpty()) {
			return null;
		}
		
		try {
			String[] split = source.split(",");
			
			String world = split[0];
			String x = split[1];
			String y = split[2];
			String z = split[3];
			String yaw = split[4];
			String pitch = split[5];
			
			return new Location(
					Bukkit.getWorld(world.trim()),
					Double.valueOf(x.trim()),
					Double.valueOf(y.trim()),
					Double.valueOf(z.trim()),
					Float.valueOf(yaw.trim()),
					Float.valueOf(pitch.trim())
					);
		} catch (Exception ex) {
			return null;
		}
	}
}