package dev.astro.argon.box;

import dev.astro.argon.ArgonPlugin;
import dev.astro.argon.box.mystery.MysteryBox;
import dev.astro.argon.box.mystery.menu.MysteryMenu;
import dev.astro.argon.config.SettingsConfig;
import dev.astro.argon.util.BroadcastUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;

@RequiredArgsConstructor
public class BoxListener implements Listener {

    private final BoxHandler boxHandler;

    @EventHandler(ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        ItemStack hand = event.getItemInHand();

        if (hand != null) {
            if (boxHandler.isLootBox(hand)) {
                event.setCancelled(true);
                return;
            }

            if (boxHandler.isLuckyBlock(hand)) {
                Block block = event.getBlock();
                block.setMetadata("luckyblock", new FixedMetadataValue(ArgonPlugin.getPlugin(), "luckyblock"));
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (block.hasMetadata("luckyblock")) {
            boxHandler.dropLuckyBlockLoot(event.getPlayer(), block.getLocation());
            block.setType(Material.AIR);
            block.removeMetadata("luckyblock", ArgonPlugin.getPlugin());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) {
            ItemStack hand = event.getItem();

            if (hand != null) {
                if (boxHandler.isMysteryBox(hand)) {
                    MysteryBox box = new MysteryBox(boxHandler.getMysteryBoxCommon(), boxHandler.getMysteryBoxOp());
                    new MysteryMenu(box).openMenu(player);

                    if (hand.getAmount() == 1) {
                        player.setItemInHand(new ItemStack(Material.AIR, 1));
                    } else {
                        hand.setAmount(hand.getAmount() - 1);
                    }

                    event.setCancelled(true);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    return;
                }

                if (boxHandler.isLootBox(hand)) {
                    boxHandler.giveLootBoxLoot(player);

                    if (hand.getAmount() == 1) {
                        player.setItemInHand(new ItemStack(Material.AIR, 1));
                    } else {
                        hand.setAmount(hand.getAmount() - 1);
                    }

                    event.setCancelled(true);
                    event.setUseInteractedBlock(Event.Result.DENY);

                    if (SettingsConfig.LOOT_BOX_FIREWORK) {
                        try {
                            Firework firework = (Firework) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                            FireworkMeta fireworkMeta = firework.getFireworkMeta();

                            fireworkMeta.setPower(1);
                            fireworkMeta.addEffect(FireworkEffect.builder().trail(true).withColor(Color.YELLOW).flicker(true).build());

                            firework.setFireworkMeta(fireworkMeta);
                        } catch (Exception e) {
                            BroadcastUtil.sendConsole("&c[Argon] Failed to spawn a firework when open a loot box");
                        }
                    }
                }
            }
        }
    }
}
