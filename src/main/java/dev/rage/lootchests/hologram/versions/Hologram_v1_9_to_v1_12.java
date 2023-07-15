package dev.rage.lootchests.hologram.versions;

import dev.rage.lootchests.hologram.Hologram;
import dev.rage.lootchests.hologram.IHologram;
import dev.rage.lootchests.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Hologram_v1_9_to_v1_12 implements IHologram {

    private Class<?>
            // Packet classes
            PACKET, PACKET_PLAY_OUT_SPAWN_ENTITY_LIVING, PACKET_PLAY_OUT_ENTITY_DESTROY, PACKET_PLAY_OUT_ENTITY_METADATA,
            // Entity classes
            ENTITY, ENTITY_LIVING, ENTITY_ARMOR_STAND,
            // World classes
            CRAFT_WORLD, NMS_WORLD,
            // DataWatcher classes
            DATA_WATCHER, DATA_WATCHER_SERIALIZER, DATA_WATCHER_REGISTRY
            ;

    private final List<UUID> viewers;

    private Object entityArmorStand;

    public Hologram_v1_9_to_v1_12() {
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
            DATA_WATCHER_SERIALIZER = Class.forName(nms + ".DataWatcherSerializer");
            DATA_WATCHER_REGISTRY = Class.forName(nms + ".DataWatcherRegistry");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void spawnHologram(Player player, Hologram hologram) {
        if (viewers.contains(player.getUniqueId())) return;

        Location location = hologram.getLocation().clone().add(0, 1, 0);
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

            entityArmorStand = armorStand;
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
            Object dataWatcher = ENTITY_LIVING.getMethod("getDataWatcher").invoke(entityArmorStand);

            Method set = DATA_WATCHER.getMethod("set", DATA_WATCHER_SERIALIZER, Object.class);

            Object dataWatcherRegistry = DATA_WATCHER_REGISTRY.getField("d").get(null);
            Object dataWatcherObject = DATA_WATCHER_SERIALIZER.getConstructor(int.class, DATA_WATCHER_REGISTRY).newInstance(2, dataWatcherRegistry);

            set.invoke(dataWatcher, dataWatcherObject, ColorUtil.colorize(hologram.getDisplayText()));

            Object packet = PACKET_PLAY_OUT_ENTITY_METADATA.getConstructor(int.class, DATA_WATCHER, boolean.class).newInstance(hologram.getEntityId(), dataWatcher, true);
            sendPacket(player, packet);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
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
