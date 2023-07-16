package dev.rage.argon.crate;

import dev.rage.argon.Argon;
import dev.rage.argon.config.LanguageConfig;
import dev.rage.argon.hologram.Hologram;
import dev.rage.argon.item.LootItem;
import dev.rage.argon.util.*;
import dev.rage.argon.util.config.Replacement;
import dev.rage.argon.util.config.Serializable;
import dev.rage.argon.util.config.serialize.defaults.ItemStackSerializer;
import dev.rage.argon.util.config.serialize.defaults.LocationSerializer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Getter @Setter
public class Crate implements Serializable<Crate> {

    private final UUID id;
    private String name;

    private ChatColor color;
    private ItemStack opener;
    private ItemStack key;

    private boolean virtualKeys;
    private boolean openFromKey;

    private List<Location> locations;
    private List<LootItem> lootItems;

    private String hologramText;

    public Crate(UUID id, String name) {
        this.id = id;
        this.name = name;

        color = ChatColor.WHITE;
        opener = new ItemStack(Material.CHEST);
        key = new ItemStack(Material.TRIPWIRE_HOOK);

        virtualKeys = false;
        openFromKey = false;

        locations = new ArrayList<>();
        lootItems = new ArrayList<>();
    }

    public LootItem selectItem() {
        Random random = new Random();

        int current = random.nextInt(99) + 1;
        List<LootItem> selectedItems = new ArrayList<>();

        for(LootItem lootItem : lootItems) {
            if(lootItem.getItemStack().getType().equals(Material.STAINED_GLASS_PANE)) {
                continue;
            }

            if(current <= lootItem.getChance()) {
                selectedItems.add(lootItem);
            }

        }

        // remove 0 chance items
        selectedItems.removeIf(lootItem -> lootItem.getChance() <= 0.0);

        if(selectedItems.isEmpty()) {

            int rand = random.nextInt(lootItems.size());

            return lootItems.get(rand);
        }

        int select = random.nextInt(selectedItems.size());

        return selectedItems.get(select);
    }

    public boolean giveLoot(Player player) {
        if(InventoryUtil.isFull(player)) {
            Replacement replacement = new Replacement(LanguageConfig.INVENTORY_FULL);
            replacement.add("<prefix>", LanguageConfig.PREFIX);

            player.sendMessage(ColorUtil.colorize(replacement.toString()));
            return false;
        }

        LootItem selected = selectItem();
        if (selected == null) {
            Replacement replacement = new Replacement(LanguageConfig.CRATE_NO_ITEM);
            replacement.add("<prefix>", LanguageConfig.PREFIX);
            player.sendMessage(ColorUtil.colorize(replacement.toString()));
            return false;
        }

        if (selected.getCommands().isEmpty()) {
            player.getInventory().addItem(selected.getItemStack());
        } else {
            selected.getCommands().forEach(cmd -> {
                Replacement replacement = new Replacement(cmd);
                replacement.add("<player>", player.getName());
                replacement.add("<item-name>", selected.getItemStack().getItemMeta().getDisplayName());
                replacement.add("<item-amount>", selected.getItemStack().getAmount());

                Bukkit.getServer().getScheduler().runTask(Argon.get(),
                        () -> Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), replacement.toString()));
            });
        }

        if (!TextUtil.isNull(selected.getAnnouncement())) {
            Replacement replacement = new Replacement(selected.getAnnouncement());
            replacement.add("<player>", player.getName());
            replacement.add("<item-name>", selected.getItemStack().getItemMeta().getDisplayName());
            replacement.add("<item-amount>", selected.getItemStack().getAmount());

            BroadcastUtil.sendAll(replacement.toString());
        }

        return true;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("name", name);

        map.put("color", color.name());
        map.put("opener", ItemStackSerializer.INSTANCE.toString(opener));
        map.put("key", ItemStackSerializer.INSTANCE.toString(key));

        map.put("virtual-keys", virtualKeys);
        map.put("open-from-key", openFromKey);

        List<String> locationsList = locations.stream().map((loc) -> LocationSerializer.INSTANCE.toString(loc)).collect(Collectors.toList());
        map.put("locations", locationsList);

        map.put("loot", lootItems);

        return map;
    }

    @Override
    public void deserialize(Map<String, Object> map) {
        name = (String) map.get("name");

        color = ChatColor.valueOf((String) map.get("color"));

        try {
            opener = ItemBuilder.fromBase64((String) map.get("opener"))[0];
            key = ItemBuilder.fromBase64((String) map.get("key"))[0];
        } catch (IOException e) {
            e.printStackTrace();
        }

        virtualKeys = (boolean) map.get("virtual-keys");
        openFromKey = (boolean) map.get("open-from-key");

        List<String> locationList = (List<String>) map.get("locations");
        locations = locationList.stream().map((str -> LocationSerializer.INSTANCE.fromString(str))).collect(Collectors.toList());

        lootItems = (List<LootItem>) map.get("loot");
    }

    public String getHologramText() {
        if (hologramText == null) {
            return color + name + " &7Crate";
        }

        return hologramText;
    }

    public void spawnHolograms() {
        locations.forEach(location -> {
            String id = location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ();

            Hologram hologram = Argon.get().getHologramHandler().getHologram(id);

            if (hologram == null) {
                hologram = Argon.get().getHologramHandler().createHologram(id, location.clone().add(0.5, 0, 0.5));

                hologram.setText(getHologramText());

                hologram.addBelowLine("&fLeft click to view loot", 0.35);
                hologram.addBelowLine("&fRight click to open key", 0.28);

                hologram.spawnAll();
            } else {
                hologram.respawnAll();
            }
        });
    }

    public void deleteHologram(Location location) {
        String id = location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ();

        Hologram hologram = Argon.get().getHologramHandler().getHologram(id);
        hologram.despawnAll();

        Argon.get().getHologramHandler().deleteHologram(id);
    }

    public void deleteHolograms() {
        locations.forEach(this::deleteHologram);
    }

    public void updateHolograms() {
        deleteHolograms();
        spawnHolograms();
    }
}
