package dev.rage.argon.hologram;

import dev.rage.argon.Argon;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
public class Hologram {

	private final String name;
	private Location location;
	
	private String text;
	private String displayText;

	private List<Hologram> aboveLines = new ArrayList<>();
	private List<Hologram> belowLines = new ArrayList<>();

	private int entityId;
	
	private IHologram entityHologram;
	
	public Hologram(String name, Location location) {
		this.name = name;
		this.location = location;

		entityHologram  = Argon.get().getHologramHandler().construct();
	}

	public void setText(String text) {
		this.text = text;
		this.displayText = text;
	}

	public void spawnAll() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			this.spawnAll(player);
		}
	}

	public void respawn(Player player) {
		despawn(player);
		spawn(player);
	}

	public void respawnAll(Player player) {
		despawnAll(player);
		spawnAll(player);
	}

	public void respawnAll() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			this.respawnAll(player);
		}
	}

	public void spawnAll(Player player) {
		this.aboveLines.forEach(hologram -> hologram.spawn(player));
		this.spawn(player);
		this.belowLines.forEach(hologram -> hologram.spawn(player));
	}

	public void despawnAll() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			this.despawnAll(player);
		}
	}

	public void despawnAll(Player player) {
		this.aboveLines.forEach(hologram -> hologram.despawn(player));
		this.despawn(player);
		this.belowLines.forEach(hologram -> hologram.despawn(player));
	}

	public void spawn() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			this.spawn(player);
		}
	}

	public void spawn(Player player) {
		World playerWorld = player.getWorld();

		if (playerWorld.equals(location.getWorld())) {
			entityHologram.spawnHologram(player, this);
		}
	}

	public void despawn(Player player) {
		World playerWorld = player.getWorld();

		if (playerWorld.equals(location.getWorld())) {
			entityHologram.destroyHologram(player, this);
		}
	}

	public void updateAll() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if (player.getWorld().equals(location.getWorld())) {
				updateAll(player);
			}
		}
	}

	public void updateAll(Player player ) {
		aboveLines.forEach(above -> above.update(player));
		update(player);
		belowLines.forEach(below -> below.update(player));
	}

	public void update() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if (player.getWorld().equals(location.getWorld())) {
				this.update(player);
			}
		}
	}

	public void update(Player player) {
		entityHologram.updateHologram(player, this);
	}

	public Hologram addAboveLine(String text, double highest) {
		Hologram hologram = aboveLines.size() > 0 ? aboveLines.get(aboveLines.size() - 1) : this;
		Location location = hologram.getLocation().clone().add(0.0, highest, 0.0);

		UUID id = UUID.randomUUID();
		Hologram aboveHologram = new Hologram(id.toString(), location);
		aboveHologram.setEntityHologram(Argon.get().getHologramHandler().construct());
		aboveHologram.setText(text);

		aboveLines.add(aboveHologram);
		return aboveHologram;
	}

	public void addBelowLine(String text, double lowest) {
		Hologram hologram = belowLines.size() > 0 ? belowLines.get(belowLines.size() - 1) : this;
		Location location = hologram.getLocation().clone().add(0.0, -lowest, 0.0);

		UUID id = UUID.randomUUID();
		Hologram belowHologram = new Hologram(id.toString(), location);
		belowHologram.setEntityHologram(Argon.get().getHologramHandler().construct());
		belowHologram.setText(text);

		belowLines.add(belowHologram);
	}
}
