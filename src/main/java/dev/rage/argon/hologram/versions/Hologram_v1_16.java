package dev.rage.argon.hologram.versions;

import dev.rage.argon.hologram.Hologram;
import dev.rage.argon.hologram.IHologram;
import dev.rage.argon.util.ColorUtil;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Hologram_v1_16 implements IHologram {

    private final List<UUID> viewers;
    private EntityArmorStand entityArmorStand;

    public Hologram_v1_16() {
        viewers = new ArrayList<>();
    }

    @Override
    public void spawnHologram(Player player, Hologram hologram) {
        if (viewers.contains(player.getUniqueId())) return;

        Location location = hologram.getLocation();
        World world = this.getWorld(location);

        String message = ColorUtil.colorize(hologram.getDisplayText());

        EntityArmorStand armorStand = new EntityArmorStand(EntityTypes.ARMOR_STAND, world);

        armorStand.setCustomName(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"));
        armorStand.setCustomNameVisible(true);
        armorStand.setInvisible(true);
        armorStand.setSmall(true);
        armorStand.setArms(false);
        armorStand.setNoGravity(true);
        armorStand.setBasePlate(false);

        armorStand.setPosition(location.getX(), location.getY(), location.getZ());

        PacketPlayOutSpawnEntityLiving spawnEntityLiving = new PacketPlayOutSpawnEntityLiving(armorStand);

        sendPacket(player, spawnEntityLiving);

        hologram.setEntityId(armorStand.getId());

        entityArmorStand = armorStand;
        viewers.add(player.getUniqueId());
    }

    @Override
    public void destroyHologram(Player player, Hologram hologram) {
        if (!viewers.contains(player.getUniqueId())) return;

        viewers.remove(player.getUniqueId());

        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(hologram.getEntityId(), hologram.getEntityId() + 1);
        this.sendPacket(player, packet);
    }

    @Override
    public void updateHologram(Player player, Hologram hologram) {
        if (!viewers.contains(player.getUniqueId())) return;
        if (entityArmorStand == null) return;

        String message = hologram.getDisplayText();

        DataWatcher dataWatcher = entityArmorStand.getDataWatcher();

        IChatBaseComponent component = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ColorUtil.colorize(message) + "\"}");

        assert component != null;

        Optional<IChatBaseComponent> opt = Optional.of(component);

        dataWatcher.set(new DataWatcherObject<>(2, DataWatcherRegistry.f), opt);

        this.sendPacket(player, new PacketPlayOutEntityMetadata(hologram.getEntityId(), dataWatcher, true));
    }

    /**
     * Send a packet to the player
     *
     * @param player the player
     * @param packet the packet to send
     */
    public void sendPacket(Player player, Packet<?> packet) {
        getPlayerConnection(player).sendPacket(packet);
    }

    /**
     * Get the {@link PlayerConnection} of a player
     *
     * @param player the player to get the player connection object from
     * @return the object
     */
    public PlayerConnection getPlayerConnection(Player player) {
        return this.getEntityPlayer(player).playerConnection;
    }

    /**
     * Get the {@link EntityPlayer} object of a player
     *
     * @param player the player to get the entity player object from
     * @return the entity player
     */
    public EntityPlayer getEntityPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }


    /**
     * Get the {@link World} of a location
     *
     * @param location the location to get the world handle
     * @return the world handle
     */
    public World getWorld(Location location) {
        return ((CraftWorld) location.getWorld()).getHandle();
    }
}
