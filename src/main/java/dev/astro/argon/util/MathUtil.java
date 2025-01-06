package dev.astro.argon.util;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * This code has been created by
 * gatogamer#6666 A.K.A. gatogamer.
 * If you want to use my code, please
 * ask first, and give me the credits.
 * Arigato! n.n
 */
public class MathUtil {
    public static List<Location> createCylinder(Location base, double radius, int quantity) {
        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            double angle, x, z;
            angle = 2 * Math.PI * i / quantity;
            x = Math.cos(angle) * radius;
            z = Math.sin(angle) * radius;
            locations.add(base.clone().add(x, 0, z));
        }
        return locations;
    }
}
