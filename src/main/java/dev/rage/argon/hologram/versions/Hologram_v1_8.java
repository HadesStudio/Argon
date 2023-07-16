package dev.rage.argon.hologram.versions;

import dev.rage.argon.hologram.Hologram;
import dev.rage.argon.hologram.IHologram;
import dev.rage.argon.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Hologram_v1_8 implements IHologram {

    private Class<?>
            // Packet classes
            PACKET, PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING, PACKET_PLAY_OUT_ENTITY_DESTROY, PACKET_PLAY_OUT_ENTITY_METADATA,
            // Entity classes
            ENTITY, ENTITY_LIVING, ENTITY_ARMOR_STAND,
            // World classes
            CRAFT_WORLD, NMS_WORLD,
            // DataWatcher classes
            DATA_WATCHER
            ;

    private final List<UUID> viewers;

    public Hologram_v1_8() {
        viewers = new ArrayList<>();

        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        String cb = "org.bukkit.craftbukkit." + version;
        String nms = "net.minecraft.server." + version;

        try {

            PACKET = Class.forName(nms + ".Packet");
            PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING = Class.forName(nms + ".PacketPlayOutSpawnEntityLiving");
            PACKET_PLAY_OUT_ENTITY_DESTROY = Class.forName(nms + ".PacketPlayOutEntityDestroy");
            PACKET_PLAY_OUT_ENTITY_METADATA = Class.forName(nms + ".PacketPlayOutEntityMetadata");

            ENTITY = Class.forName(nms + ".Entity");
            ENTITY_LIVING = Class.forName(nms + ".EntityLiving");
            ENTITY_ARMOR_STAND = Class.forName(nms + ".EntityArmorStand");

            CRAFT_WORLD = Class.forName(cb + ".CraftWorld");
            NMS_WORLD = Class.forName(nms + ".World");

            DATA_WATCHER = Class.forName(nms + ".DataWatcher");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void spawnHologram(Player player, Hologram hologram) {
        if (viewers.contains(player.getUniqueId())) return;

        Location location = hologram.getLocation().clone().add(0, 0.8, 0);
        try {

            Object craftWorld = getCraftWorld(location);
            Object armorStand = ENTITY_ARMOR_STAND.getConstructor(NMS_WORLD).newInstance(craftWorld);

            Method getId = ENTITY.getMethod("getId");
            Method setPosition = ENTITY_ARMOR_STAND.getMethod("setPosition", double.class, double.class, double.class);

            Method setCustomName = ENTITY_ARMOR_STAND.getMethod("setCustomName", String.class);
            Method setCustomNameVisible = ENTITY_ARMOR_STAND.getMethod("setCustomNameVisible", boolean.class);
            Method setSmall = ENTITY_ARMOR_STAND.getMethod("setSmall", boolean.class);
            Method setBasePlate = ENTITY_ARMOR_STAND.getMethod("setBasePlate", boolean.class);
            Method setInvisible = ENTITY_ARMOR_STAND.getMethod("setInvisible", boolean.class);

            setPosition.invoke(armorStand, location.getX(), location.getY(), location.getZ());

            setCustomName.invoke(armorStand, ColorUtil.colorize(hologram.getDisplayText()));
            setCustomNameVisible.invoke(armorStand, true);
            setSmall.invoke(armorStand, true);
            setBasePlate.invoke(armorStand, false);
            setInvisible.invoke(armorStand, true);

            int entityId = (int) getId.invoke(armorStand);
            hologram.setEntityId(entityId);

            Object packet = PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING.getConstructor(ENTITY_LIVING).newInstance(armorStand);
            sendPacket(player, packet);

            viewers.add(player.getUniqueId());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroyHologram(Player player, Hologram hologram) {
        if (!viewers.contains(player.getUniqueId())) return;

        try {
            Object packet = PACKET_PLAY_OUT_ENTITY_DESTROY.getConstructor(int[].class).newInstance((Object) new int[] { hologram.getEntityId() });
            sendPacket(player, packet);

            viewers.remove(player.getUniqueId());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateHologram(Player player, Hologram hologram) {
        if (!viewers.contains(player.getUniqueId())) return;

        try {
            Object dataWatcher = DATA_WATCHER.getConstructor(ENTITY).newInstance((Object) null);

            Method a = DATA_WATCHER.getMethod("a", int.class, Object.class);

            a.invoke(dataWatcher, 2, ColorUtil.colorize(hologram.getDisplayText()));

            Object packet = PACKET_PLAY_OUT_ENTITY_METADATA.getConstructor(int.class, DATA_WATCHER, boolean.class).newInstance(hologram.getEntityId(), dataWatcher, true);
            sendPacket(player, packet);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

    }

    public Object getCraftWorld(Location location) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return CRAFT_WORLD.getMethod("getHandle").invoke(CRAFT_WORLD.cast(location.getWorld()));
    }

    private void sendPacket(Player player, Object packet) {
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);

            connection.getClass().getMethod("sendPacket", PACKET).invoke(connection, packet);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
