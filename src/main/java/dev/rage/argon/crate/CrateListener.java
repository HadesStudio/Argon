package dev.rage.argon.crate;

import com.cryptomorin.xseries.XSound;
import dev.rage.argon.ArgonPlugin;
import dev.rage.argon.config.LanguageConfig;
import dev.rage.argon.crate.menu.editor.CrateEditorMenu;
import dev.rage.argon.crate.menu.loot.CrateLootMenu;
import dev.rage.argon.profile.Profile;
import dev.rage.argon.util.ColorUtil;
import dev.rage.argon.util.LocationUtil;
import dev.rage.argon.util.ReflectionUtil;
import dev.rage.argon.util.config.Replacement;
import dev.rage.argon.util.version.ServerVersion;
import lombok.RequiredArgsConstructor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class CrateListener implements Listener {

    private final CrateHandler crateHandler;

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack hand = event.getItemInHand();

        if(hand != null) {
            if(crateHandler.isCrate(hand)) {
                Crate crate = crateHandler.getByItem(hand);

                if(crate != null) {
                    Location blockLocation = event.getBlockPlaced().getLocation();

                    crate.getLocations().add(blockLocation);

                    crate.spawnHolograms();

                    Replacement replacement = new Replacement(LanguageConfig.CRATE_PLACED);
                    replacement.add("<prefix>", LanguageConfig.PREFIX);
                    replacement.add("<crate>", crate.getName());
                    replacement.add("<location>", LocationUtil.formatLocation(blockLocation, true));

                    player.sendMessage(ColorUtil.colorize(replacement.toString()));
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location location = event.getBlock().getLocation();
        Crate crate = crateHandler.getByLocation(location);

        if(crate != null) {
            if(player.isSneaking() && player.getGameMode().equals(GameMode.CREATIVE)) {
                crateHandler.removeLocation(crate, location);

                crate.deleteHologram(location);

                Replacement replacement = new Replacement(LanguageConfig.CRATE_REMOVED);
                replacement.add("<prefix>", LanguageConfig.PREFIX);
                replacement.add("<crate>", crate.getName());
                replacement.add("<location>", LocationUtil.formatLocation(location, true));

                player.sendMessage(ColorUtil.colorize(replacement.toString()));
                return;
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action =  event.getAction();
        Block block = event.getClickedBlock();

        ItemStack hand = event.getItem();

        Profile profile = ArgonPlugin.get().getProfileHandler().getProfile(player);
        Crate crate = crateHandler.getByKey(hand);

        if (crate != null) {
            if (crate.isOpenFromKey()) {
                if (crate.giveLoot(player)) {
                    if (hand.getAmount() == 1) {
                        player.setItemInHand(new ItemStack(Material.AIR, 1));
                    } else {
                        hand.setAmount(hand.getAmount() - 1);
                    }

                    player.updateInventory();
                    player.playSound(player.getLocation(), XSound.BLOCK_NOTE_BLOCK_PLING.parseSound(), 20.0f, 15.0f);
                }

                event.setCancelled(true);
                return;
            }
        }


        if (block != null) {
            Location blockLocation = block.getLocation();

            crate = crateHandler.getByLocation(blockLocation);
            if (crate != null) {
                if (action == Action.RIGHT_CLICK_BLOCK) {
                    if (crate.isVirtualKeys()) {
                        if (profile.getVirtualKeys().containsKey(crate.getId())) {
                            int count = profile.getVirtualKeys().get(crate.getId());
                            if (count <= 0) {
                                Replacement replacement = new Replacement(LanguageConfig.CRATE_NO_VIRTUAL_KEY);
                                replacement.add("<prefix>", LanguageConfig.PREFIX);
                                replacement.add("<crate>", crate.getName());

                                player.sendMessage(ColorUtil.colorize(replacement.toString()));
                            } else {
                                if (crate.giveLoot(player)) {
                                    profile.removeVirtualKey(crate.getId(), 1);

                                    player.updateInventory();
                                    player.playSound(player.getLocation(), XSound.BLOCK_LEVER_CLICK.parseSound(), 20.0f, 15.0f);
                                }
                            }
                        } else {
                            Replacement replacement = new Replacement(LanguageConfig.CRATE_NO_VIRTUAL_KEY);
                            replacement.add("<prefix>", LanguageConfig.PREFIX);
                            replacement.add("<crate>", crate.getName());

                            player.sendMessage(ColorUtil.colorize(replacement.toString()));
                        }
                    } else {
                        if (crateHandler.isKey(crate, hand)) {
                            if (crate.giveLoot(player)) {
                                if (hand.getAmount() == 1) {
                                    player.setItemInHand(new ItemStack(Material.AIR, 1));
                                } else {
                                    hand.setAmount(hand.getAmount() - 1);
                                }

                                player.updateInventory();
                                player.playSound(player.getLocation(), XSound.BLOCK_LEVER_CLICK.parseSound(), 20.0f, 15.0f);
                            }
                        } else {
                            if (player.isSneaking() && player.getGameMode().equals(GameMode.CREATIVE)) {
                                if (player.hasPermission("lootchest.command.crate.edit")) {
                                    new CrateEditorMenu(crate).openMenu(player);
                                }
                            } else {
                                Replacement replacement = new Replacement(LanguageConfig.CRATE_NO_KEY);
                                replacement.add("<prefix>", LanguageConfig.PREFIX);
                                replacement.add("<crate>", crate.getName());

                                player.sendMessage(ColorUtil.colorize(replacement.toString()));
                            }
                        }
                    }

                    event.setCancelled(true);
                } else if (action == Action.LEFT_CLICK_BLOCK) {
                    if (player.isSneaking() && player.getGameMode().equals(GameMode.CREATIVE) && player.hasPermission("lootchests.command.crate.edit")) {
                        return;
                    }

                    if (!crate.getLootItems().isEmpty()) {
                        new CrateLootMenu(crate).openMenu(player);
                    }
                }

                event.setCancelled(true);
            }
        }
    }
}
