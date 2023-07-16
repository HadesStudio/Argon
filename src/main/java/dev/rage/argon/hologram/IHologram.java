package dev.rage.argon.hologram;

import org.bukkit.entity.Player;

public interface IHologram {

    void spawnHologram(Player player, Hologram hologram);

    void destroyHologram(Player player, Hologram hologram);

    void updateHologram(Player player, Hologram hologram);
}
