package dev.rage.lootchests.hologram;

import dev.rage.lootchests.LootChests;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

@RequiredArgsConstructor
public class HologramListener implements Listener {

	private final HologramHandler hologramHandler;
	
	private final int HOLOGRAM_VIEW_DISTANCE = 25;
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		hologramHandler.getHolograms().stream()
				.filter(hologram -> hologram.getLocation().getWorld().equals(player.getLocation().getWorld()))
				.filter(hologram -> hologram.getLocation().distance(player.getLocation()) <= HOLOGRAM_VIEW_DISTANCE)
				.forEach(hologram -> hologram.spawnAll(player));
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();

		hologramHandler.getHolograms().forEach(hologram -> hologram.despawnAll(player));
		hologramHandler.getHolograms().stream()
				.filter(hologram -> hologram.getLocation().getWorld().equals(player.getLocation().getWorld()))
				.filter(hologram -> hologram.getLocation().distance(player.getLocation()) <= HOLOGRAM_VIEW_DISTANCE)
				.forEach(hologram -> hologram.spawnAll(player));
	}

	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();

		hologramHandler.getHolograms().forEach(hologram -> hologram.despawnAll(player));
		hologramHandler.getHolograms().stream()
				.filter(hologram -> hologram.getLocation().getWorld().equals(player.getLocation().getWorld()))
				.filter(hologram -> hologram.getLocation().distance(player.getLocation()) <= HOLOGRAM_VIEW_DISTANCE)
				.forEach(hologram -> hologram.spawnAll(player));
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();

		hologramHandler.getHolograms().forEach(hologram -> hologram.despawnAll(player));

		Bukkit.getServer().getScheduler().runTaskLater(LootChests.get(), () -> {
			hologramHandler.getHolograms().stream()
					.filter(hologram -> hologram.getLocation().getWorld().equals(player.getLocation().getWorld()))
					.filter(hologram -> hologram.getLocation().distance(player.getLocation()) <= HOLOGRAM_VIEW_DISTANCE)
					.forEach(hologram -> hologram.spawnAll(player));
		}, 10L);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
    	Location from = event.getFrom();
    	Location to = event.getTo();
    	
    	if (from.getX() == to.getX() && from.getY() == to.getY() && from.getZ() == to.getZ()) return;
    	
    	// spawn/despawn methods
    	hologramHandler.getHolograms().stream()
    		.filter(hologram -> hologram.getLocation().getWorld().equals(player.getLocation().getWorld()))
    		.forEach(hologram -> {
    			if (hologram.getLocation().distance(player.getLocation()) > HOLOGRAM_VIEW_DISTANCE) {
    				hologram.despawnAll(player);
    			} else {
    				hologram.spawnAll(player);
    			}
    		});
	}
}
