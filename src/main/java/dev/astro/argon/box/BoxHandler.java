package dev.astro.argon.box;

import com.cryptomorin.xseries.XMaterial;
import dev.astro.argon.ArgonPlugin;
import dev.astro.argon.box.editor.CommonBoxEditor;
import dev.astro.argon.box.editor.LuckyBlockEditor;
import dev.astro.argon.box.editor.MysteryBoxEditor;
import dev.astro.argon.box.editor.MysteryBoxOpEditor;
import dev.astro.argon.config.SettingsConfig;
import dev.astro.argon.item.LootItem;
import dev.astro.argon.util.BroadcastUtil;
import dev.astro.argon.util.InventoryUtil;
import dev.astro.argon.util.ItemBuilder;
import dev.astro.argon.util.TextUtil;
import dev.astro.argon.util.config.Config;
import dev.astro.argon.util.config.Replacement;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter @Setter
public class BoxHandler {

    private final Config config;

    private ItemStack[] lootBox;
    private ItemStack[] luckyBlock;

    private ItemStack[] mysteryBoxCommon;
    private ItemStack[] mysteryBoxOp;

    private String mysteryBoxAnnounce;
    private String luckyBlockAnnounce;
    private String lootBoxAnnounce;

    public BoxHandler(ArgonPlugin instance) {
        this.config = new Config(instance, "boxes.yml");

        Bukkit.getServer().getPluginManager().registerEvents(new BoxListener(this), instance);
        Bukkit.getServer().getPluginManager().registerEvents(new CommonBoxEditor(), instance);
        Bukkit.getServer().getPluginManager().registerEvents(new LuckyBlockEditor(), instance);
        Bukkit.getServer().getPluginManager().registerEvents(new MysteryBoxEditor(), instance);
        Bukkit.getServer().getPluginManager().registerEvents(new MysteryBoxOpEditor(), instance);

        lootBox = Arrays.asList(
                new ItemStack(Material.IRON_BLOCK, 16),
                new ItemStack(Material.GOLD_BLOCK, 16),
                new ItemStack(Material.DIAMOND_BLOCK, 16),
                new ItemStack(Material.EMERALD_BLOCK, 16)
        ).toArray(new ItemStack[0]);

        luckyBlock = Arrays.asList(
                new ItemStack(Material.IRON_BLOCK, 16),
                new ItemStack(Material.GOLD_BLOCK, 16),
                new ItemStack(Material.DIAMOND_BLOCK, 16),
                new ItemStack(Material.EMERALD_BLOCK, 16),
                new ItemStack(XMaterial.DIRT.parseMaterial(), 16),
                new ItemStack(XMaterial.COBBLESTONE.parseMaterial(), 16),
                new ItemStack(XMaterial.STICK.parseMaterial(), 64),
                new ItemStack(XMaterial.BONE.parseMaterial(), 16),
                new ItemStack(XMaterial.BOWL.parseMaterial(), 5)
        ).toArray(new ItemStack[0]);

        mysteryBoxCommon = lootBox;
        mysteryBoxOp = Arrays.asList(
                new ItemStack(Material.IRON_BLOCK, 64),
                new ItemStack(Material.GOLD_BLOCK, 64),
                new ItemStack(Material.DIAMOND_BLOCK, 64),
                new ItemStack(Material.EMERALD_BLOCK, 64)
        ).toArray(new ItemStack[0]);

        loadLoot();
    }

    private void loadLoot() {
        try {
            if (config.contains("common-loot-box")) {
                lootBox = ItemBuilder.fromBase64(config.getString("common-loot-box"));
            }

            if (config.contains("mystery.common-loot-box")) {
                mysteryBoxCommon = ItemBuilder.fromBase64(config.getString("mystery.common-loot-box"));
            }

            if (config.contains("mystery.op-loot-box")) {
                mysteryBoxOp = ItemBuilder.fromBase64(config.getString("mystery.op-loot-box"));
            }

            if (config.contains("mystery.op-loot-box-announcement")) {
                mysteryBoxAnnounce = config.getString("mystery.op-loot-box-announcement");
            }

            if (config.contains("common-loot-box-announcement")) {
                lootBoxAnnounce = config.getString("common-loot-box-announcement");
            }

            if (config.contains("lucky-block-announcement")) {
                luckyBlockAnnounce = config.getString("lucky-block-announcement");
            }

            if (config.contains("lucky-block")) {
                luckyBlock = ItemBuilder.fromBase64(config.getString("lucky-block"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        saveLoot();
    }

    public void saveLoot() {
        config.set("common-loot-box", ItemBuilder.toBase64(lootBox));
        config.set("mystery.common-loot-box", ItemBuilder.toBase64(mysteryBoxCommon));
        config.set("mystery.op-loot-box", ItemBuilder.toBase64(mysteryBoxOp));
        config.set("mystery.op-loot-box-announcement", mysteryBoxAnnounce);
        config.set("common-loot-box-announcement", lootBoxAnnounce);
        config.set("lucky-block-announcement", luckyBlockAnnounce);

        config.set("lucky-block", ItemBuilder.toBase64(luckyBlock));

        config.save();
    }

    public boolean giveLuckyBlock(Player player, int amount) {
        ItemBuilder builder = new ItemBuilder(LootItem.LUCKY_BLOCK_ITEM.clone()).setAmount(amount);
        builder.setName(SettingsConfig.LUCKY_BLOCK_NAME);

        List<String> lore = new ArrayList<>();
        lore.add("&8Lucky Block");
        lore.addAll(SettingsConfig.LUCKY_BLOCK_LORE);

        builder.setLore(lore);

        if (InventoryUtil.isFull(player)) return false;

        player.getInventory().addItem(builder.build());
        return true;
    }

    public boolean isLuckyBlock(ItemStack item) {
        if(item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> lore = item.getItemMeta().getLore();
            if (lore.size() > 0) {
                String fistLine = lore.get(0);

                return fistLine.contains("Lucky Block");
            }
        }

        return false;
    }

    public void dropLuckyBlockLoot(Player player, Location location) {
        for (int i = 0; i < SettingsConfig.LUCKY_BLOCK_DROPS; i++) {
            ItemStack item = luckyBlock[ThreadLocalRandom.current().nextInt(luckyBlock.length)];
            location.getWorld().dropItemNaturally(location, item);
        }

        if (!TextUtil.isNull(ArgonPlugin.get().getBoxHandler().getLuckyBlockAnnounce())) {
            Replacement replacement = new Replacement(ArgonPlugin.get().getBoxHandler().getLuckyBlockAnnounce());
            replacement.add("<player>", player.getName());

            BroadcastUtil.sendAll(replacement.toString());
        }
    }

    public boolean giveLootBox(Player player, int amount) {
        ItemBuilder builder = new ItemBuilder(XMaterial.ENDER_CHEST.parseMaterial()).setAmount(amount);
        builder.setName(SettingsConfig.LOOT_BOX_NAME);

        List<String> lore = new ArrayList<>();
        lore.add("&8Loot Box");
        lore.addAll(SettingsConfig.LOOT_BOX_LORE);

        builder.setLore(lore);

        if (InventoryUtil.isFull(player)) return false;

        player.getInventory().addItem(builder.build());
        return true;
    }

    public boolean isLootBox(ItemStack item) {
        if(item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> lore = item.getItemMeta().getLore();
            if (lore.size() > 0) {
                String fistLine = lore.get(0);

                return fistLine.contains("Loot Box");
            }
        }

        return false;
    }

    public void giveLootBoxLoot(Player player) {
        for (int i = 0; i < SettingsConfig.LOOT_BOX_DROPS; i++) {
            ItemStack item = lootBox[ThreadLocalRandom.current().nextInt(lootBox.length)];

            if (InventoryUtil.isFull(player)) {
                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
            } else {
                player.getInventory().addItem(item);
            }
        }

        if (!TextUtil.isNull(ArgonPlugin.get().getBoxHandler().getLootBoxAnnounce())) {
            Replacement replacement = new Replacement(ArgonPlugin.get().getBoxHandler().getLootBoxAnnounce());
            replacement.add("<player>", player.getName());

            BroadcastUtil.sendAll(replacement.toString());
        }
    }

    public boolean giveMysteryBox(Player player, int amount) {
        ItemBuilder builder = new ItemBuilder(LootItem.MYSTERY_BOX_ITEM.clone()).setAmount(amount);
        builder.setName(SettingsConfig.MYSTERY_BOX_NAME);

        List<String> lore = new ArrayList<>();
        lore.add("&8Mystery Box");
        lore.addAll(SettingsConfig.MYSTERY_BOX_LORE);

        builder.setLore(lore);

        if (InventoryUtil.isFull(player)) return false;

        player.getInventory().addItem(builder.build());
        return true;
    }

    public boolean isMysteryBox(ItemStack item) {
        if(item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> lore = item.getItemMeta().getLore();
            if (lore.size() > 0) {
                String fistLine = lore.get(0);

                return fistLine.contains("Mystery Box");
            }
        }

        return false;
    }
}
