package dev.rage.argon.crate;

import dev.rage.argon.Argon;
import dev.rage.argon.config.LanguageConfig;
import dev.rage.argon.config.SettingsConfig;
import dev.rage.argon.crate.command.CrateCommands;
import dev.rage.argon.crate.command.provider.CrateProvider;
import dev.rage.argon.crate.menu.editor.CrateEditLootInventoryMenu;
import dev.rage.argon.item.LootItem;
import dev.rage.argon.profile.Profile;
import dev.rage.argon.util.ColorUtil;
import dev.rage.argon.util.InventoryUtil;
import dev.rage.argon.util.ItemBuilder;
import dev.rage.argon.util.config.Config;
import dev.rage.argon.util.config.Replacement;
import dev.rage.argon.util.config.serialize.defaults.ItemStackSerializer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class CrateHandler {

    private final List<Crate> crates;
    private final Config config;

    public CrateHandler(Argon instance) {
        crates = new ArrayList<>();
        config = new Config(instance, "/data/crates.yml");

        load();

        instance.getCommandHandler().getBinder().bind(Crate.class).toProvider(new CrateProvider(this));
        instance.getCommandHandler().register(new CrateCommands());

        Bukkit.getServer().getPluginManager().registerEvents(new CrateListener(this), instance);
        Bukkit.getServer().getPluginManager().registerEvents(new CrateEditLootInventoryMenu(), instance);
    }

    public void handleDisable() {
        save();
    }

    private void load() {
        if (config.contains("crates")) {
            for (String key : config.getConfigurationSection("crates").getKeys(false)) {
                Map<String, Object> map = new HashMap<>();

                String name = config.getString("crates." + key + ".name");

                map.put("name", name);

                map.put("color", config.getString("crates." + key + ".color"));
                map.put("opener", config.getString("crates." + key + ".opener"));
                map.put("key", config.getString("crates." + key + ".key"));

                map.put("virtual-keys", config.getBoolean("crates." + key + ".virtual-keys"));
                map.put("open-from-key", config.getBoolean("crates." + key + ".open-from-key"));

                map.put("locations", config.getStringList("crates." + key + ".locations"));

                List<LootItem> items = new ArrayList<>();
                if (config.contains("crates." + key + ".loot")) {
                    for (String k : config.getConfigurationSection("crates." + key + ".loot").getKeys(false)) {
                        LootItem item = new LootItem(ItemStackSerializer.INSTANCE.fromString(config.getString("crates." + key + ".loot." + k + ".item-stack")));
                        item.setPosition(Integer.parseInt(k));
                        item.setAnnouncement(config.getString("crates." + key + ".loot." + k + ".announcement"));
                        item.setChance(config.getInt("crates." + key + ".loot." + k + ".chance"));
                        item.setCommands(config.getStringList("crates." + key + ".loot." + k + ".commands"));

                        items.add(item);
                    }
                }
                map.put("loot", items);

                Crate crate = new Crate(UUID.fromString(key), name);
                crate.deserialize(map);

                crate.spawnHolograms();
                crates.add(crate);
            }
        }
    }

    private void save() {
        config.set("crates", null);

        for (Crate crate : crates) {
            crate.serialize().forEach((key, value) -> {
                if (key.equalsIgnoreCase("loot")) {
                    List<LootItem> items = (List<LootItem>) value;

                    for (LootItem item : items) {
                        config.set("crates." + crate.getId().toString() + ".loot." + item.getPosition() + ".item-stack", ItemStackSerializer.INSTANCE.toString(item.getItemStack()));
                        config.set("crates." + crate.getId().toString() + ".loot." + item.getPosition() + ".announcement", item.getAnnouncement());
                        config.set("crates." + crate.getId().toString() + ".loot." + item.getPosition() + ".chance", item.getChance());
                        config.set("crates." + crate.getId().toString() + ".loot." + item.getPosition() + ".commands", item.getCommands());
                    }
                } else {
                    config.set("crates." + crate.getId().toString() + "." + key, value);
                }
            });
        }

        config.save();
    }

    public Crate createCrate(String name) {
        if(getByName(name) == null) {
            Crate crate = new Crate(UUID.randomUUID(), name);

            ItemStack itemStack = new ItemBuilder(Material.DIAMOND_SWORD).setName("&6Example Sword").build();

            LootItem lootItem = new LootItem(itemStack);
            lootItem.setPosition(4);

            crate.getLootItems().add(lootItem);

            crates.add(crate);

            return crate;
        }

        return null;
    }

    public void deleteCrate(Crate target) {
        crates.remove(target);
    }

    public void giveCrate(Player player, Crate crate) {

        ItemBuilder builder = new ItemBuilder(crate.getOpener());
        builder.setName(crate.getColor().toString() + crate.getName() + "&7 Crate");
        builder.setLore("&8" + crate.getName() + " Crate");

        player.getInventory().addItem(builder.build());
    }

    public Crate getByName(String name) {
        return crates.stream().filter(c -> c.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Crate getByLocation(Location location) {
        for(Crate crate : crates) {
            for(Location crateLocation : crate.getLocations()) {
                if(crateLocation.getWorld().equals(location.getWorld()) &&
                        crateLocation.getBlockX() == location.getBlockX() &&
                        crateLocation.getBlockY() == location.getBlockY() &&
                        crateLocation.getBlockZ() == location.getBlockZ()) {
                    return crate;
                }
            }
        }

        return null;
    }

    public void removeLocation(Crate crate, Location location) {
        for(Location crateLocation : crate.getLocations()) {
            if(crateLocation.getWorld().equals(location.getWorld()) &&
                    crateLocation.getBlockX() == location.getBlockX() &&
                    crateLocation.getBlockY() == location.getBlockY() &&
                    crateLocation.getBlockZ() == location.getBlockZ()) {

                crate.getLocations().remove(crateLocation);
                break;
            }
        }
    }

    public Crate getByKey(ItemStack key) {
        if(key.hasItemMeta() && key.getItemMeta().hasLore()) {
            List<String> lore = key.getItemMeta().getLore();
            if (lore.size() > 0) {
                String fistLine = lore.get(0);

                if (fistLine.contains(" Key")) {
                    String crateName = ChatColor.stripColor(fistLine.replace(" Key", ""));

                    return getByName(crateName);
                }
            }
        }

        return null;
    }

    public Crate getByItem(ItemStack item) {
        if(item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> lore = item.getItemMeta().getLore();
            if (lore.size() > 0) {
                String fistLine = lore.get(0);

                if (fistLine.contains(" Crate")) {
                    String crateName = ChatColor.stripColor(fistLine.replace(" Crate", ""));

                    return getByName(crateName);
                }
            }
        }

        return null;
    }

    public boolean isKey(Crate crate, ItemStack item) {
        if(item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> lore = item.getItemMeta().getLore();
            if (lore.size() > 0) {
                String fistLine = lore.get(0);

                if (fistLine.contains(" Key")) {
                    String crateName = ChatColor.stripColor(fistLine.replace(" Key", ""));

                    return crate.getName().equalsIgnoreCase(crateName);
                }
            }
        }

        return false;
    }

    public boolean isCrate(ItemStack item) {
        if(item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> lore = item.getItemMeta().getLore();
            if (lore.size() > 0) {
                String fistLine = lore.get(0);

                if (fistLine.contains(" Crate")) {
                    String crateName = ChatColor.stripColor(fistLine.replace(" Crate", ""));

                    return getByName(crateName) != null;
                }
            }
        }

        return false;
    }

    public void giveKey(Player player, Crate crate, int amount) {
        ItemBuilder builder = new ItemBuilder(crate.getKey());

        builder.setName(SettingsConfig.KEY_NAME.replace("<crate>", crate.getColor() + crate.getName()));

        List<String> lore = new ArrayList<>();
        lore.add("&8" + crate.getName() + " Key"); // mini key metadata xd
        lore.addAll(SettingsConfig.KEY_LORE);

        builder.setLore(lore);
        builder.setAmount(amount);

        ItemStack item = builder.build();

        if (InventoryUtil.isFull(player)) {
            Argon.get().getProfileHandler().getProfile(player).addStoredKey(crate.getId(), 1);
        } else {
            player.getInventory().addItem(item);
        }

        Replacement replacement = new Replacement(LanguageConfig.CRATE_ADDED_KEYS);
        replacement.add("<prefix>", LanguageConfig.PREFIX);
        replacement.add("<amount>", amount);
        replacement.add("<crate>", crate.getName());

        player.sendMessage(ColorUtil.colorize(replacement.toString()));
    }

    public void giveVirtualKey(Player player, Crate crate, int amount) {
        Profile profile = Argon.get().getProfileHandler().getProfile(player);

        if (profile != null) {
            profile.addVirtualKey(crate.getId(), amount);

            Replacement replacement = new Replacement(LanguageConfig.CRATE_ADDED_VIRTUAL_KEYS);
            replacement.add("<prefix>", LanguageConfig.PREFIX);
            replacement.add("<amount>", amount);
            replacement.add("<crate>", crate.getName());

            player.sendMessage(ColorUtil.colorize(replacement.toString()));
        }
    }
}
