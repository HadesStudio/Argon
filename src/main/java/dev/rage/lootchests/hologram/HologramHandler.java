package dev.rage.lootchests.hologram;

import dev.rage.lootchests.LootChests;
import dev.rage.lootchests.hologram.versions.Hologram_v1_13;
import dev.rage.lootchests.hologram.versions.Hologram_v1_14_to_v1_16;
import dev.rage.lootchests.hologram.versions.Hologram_v1_8;
import dev.rage.lootchests.hologram.versions.Hologram_v1_9_to_v1_12;
import dev.rage.lootchests.util.BroadcastUtil;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HologramHandler {

    private final List<Hologram> holograms;

    public HologramHandler() {
        holograms = new ArrayList<>();

        Bukkit.getServer().getPluginManager().registerEvents(new HologramListener(this), LootChests.get());
    }

    public IHologram construct() {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

        if (version.contains("v1_8")) {
            return new Hologram_v1_8();
        } else if (version.contains("v1_9") || version.contains("v1_10") || version.contains("v1_11") || version.contains("v1_12")) {
            return new Hologram_v1_9_to_v1_12();
        } else if (version.contains("v1_13")) {
            return new Hologram_v1_13();
        } else if (version.contains("v1_14") || version.contains("v1_15") || version.contains("v1_16")) {
            return new Hologram_v1_14_to_v1_16();
        }

        throw new RuntimeException("[LootChests] Unsupported version");
    }

    public Hologram createHologram(String name, Location location) {
        Hologram hologram = new Hologram(name, location);

        holograms.add(hologram);

        return hologram;
    }

    public void deleteHologram(String id) {
        if (getHologram(id) != null) {
            Hologram hologram = getHologram(id);

            holograms.remove(hologram);
        }
    }

    public Hologram getHologram(String name) {
        return holograms.stream().filter(holo -> holo.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
