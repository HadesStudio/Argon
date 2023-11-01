package dev.rage.argon.hologram;

import dev.rage.argon.ArgonPlugin;
import dev.rage.argon.config.LanguageConfig;
import dev.rage.argon.hologram.versions.*;
import dev.rage.argon.hologram.versions.unsupported.UnsupportedHologram;
import dev.rage.argon.util.BroadcastUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HologramHandler {

    private final List<Hologram> holograms;

    public HologramHandler() {
        holograms = new ArrayList<>();

        Bukkit.getServer().getPluginManager().registerEvents(new HologramListener(this), ArgonPlugin.getPlugin());
    }

    public IHologram construct() {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

        if (version.contains("v1_8")) {
            return new Hologram_v1_8();
        } else if (version.contains("v1_9") || version.contains("v1_10") || version.contains("v1_11") || version.contains("v1_12")) {
            return new Hologram_v1_9_to_v1_12();
        } else if (version.contains("v1_13")) {
            return new Hologram_v1_13();
        } else if (version.contains("v1_14")) {
            return new Hologram_v1_14();
        } else if (version.contains("v1_15")) {
            return new Hologram_v1_15();
        } else if (version.contains("v1_16")) {
            return new Hologram_v1_16();
        }

        BroadcastUtil.sendConsole(LanguageConfig.PREFIX + "&7» &cUnsupported version to create holograms!");
        return new UnsupportedHologram();
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
