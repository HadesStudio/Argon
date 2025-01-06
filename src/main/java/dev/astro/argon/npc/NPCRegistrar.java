package dev.astro.argon.npc;

import com.cryptomorin.xseries.XSound;
import com.github.juliarn.npclib.api.Npc;
import com.github.juliarn.npclib.api.NpcActionController;
import com.github.juliarn.npclib.api.Platform;
import com.github.juliarn.npclib.api.event.AttackNpcEvent;
import com.github.juliarn.npclib.api.event.InteractNpcEvent;
import com.github.juliarn.npclib.api.event.ShowNpcEvent;
import com.github.juliarn.npclib.api.protocol.enums.EntityAnimation;
import com.github.juliarn.npclib.api.protocol.meta.EntityMetadataFactory;
import com.github.juliarn.npclib.bukkit.BukkitPlatform;
import com.github.juliarn.npclib.bukkit.protocol.BukkitProtocolAdapter;
import dev.astro.argon.ArgonPlugin;
import dev.astro.argon.config.LanguageConfig;
import dev.astro.argon.crate.Crate;
import dev.astro.argon.crate.CrateHandler;
import dev.astro.argon.crate.menu.editor.CrateEditorMenu;
import dev.astro.argon.crate.menu.loot.CrateLootMenu;
import dev.astro.argon.profile.Profile;
import dev.astro.argon.util.ColorUtil;
import dev.astro.argon.util.NekoLocation;
import dev.astro.argon.util.config.Replacement;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static dev.astro.argon.config.SettingsConfig.PACKET_LIBRARY;

/**
 * This code has been created by
 * gatogamer#6666 A.K.A. gatogamer.
 * If you want to use my code, please
 * ask first, and give me the credits.
 * Arigato! n.n
 */
@Getter
public class NPCRegistrar {
    @Getter(AccessLevel.NONE)
    private final ArgonPlugin argonPlugin;
    private final Map<String, NPCProfile> profiles = new ConcurrentHashMap<>();
    private final Platform<World, Player, ItemStack, Plugin> platform;
    private final Map<Npc<World, Player, ItemStack, Plugin>, NPCProfile> npcs = new ConcurrentHashMap<>();

    public NPCRegistrar(ArgonPlugin argonPlugin) {
        this.argonPlugin = argonPlugin;
        this.platform = BukkitPlatform.bukkitNpcPlatformBuilder()
                .extension(argonPlugin)
                .actionController(builder -> builder
                        .flag(NpcActionController.SPAWN_DISTANCE, 100)
                        .flag(NpcActionController.IMITATE_DISTANCE, 50)
                )
                .packetFactory(
                        PACKET_LIBRARY.equals("protocollib")
                                ? BukkitProtocolAdapter.protocolLib()
                                : BukkitProtocolAdapter.packetEvents()
                )
                .build();

        this.platform.eventManager().registerEventHandler(ShowNpcEvent.class, showNpcEvent -> {
            Npc<World, Player, ItemStack, Plugin> npc = showNpcEvent.npc();
            Player player = showNpcEvent.player();
            NPCProfile profile = this.npcs.get(npc);
            if (profile == null) {
                return;
            }

            NekoLocation nekoLocation = profile.getNekoLocation();
            npc.rotate(nekoLocation.getYaw(), nekoLocation.getPitch()).schedule(player);
            npc.changeMetadata(EntityMetadataFactory.skinLayerMetaFactory(), true).schedule(player);

            npc.playAnimation(EntityAnimation.SWING_MAIN_ARM).schedule(player);
        });

        CrateHandler crateHandler = ArgonPlugin.get().getCrateHandler();
        this.platform.eventManager().registerEventHandler(InteractNpcEvent.class, interactNpcEvent -> {
            Npc<World, Player, ItemStack, Plugin> npc = interactNpcEvent.npc();
            Player player = interactNpcEvent.player();
            NPCProfile npcProfile = this.npcs.get(npc);
            if (npcProfile == null) {
                return;
            }

            Crate crate = crateHandler.getByName(npcProfile.getCrateName());
            if (crate == null) {
                return;
            }

            ItemStack hand = player.getItemInHand();
            Profile profile = ArgonPlugin.get().getProfileHandler().getProfile(player);

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
        });
        this.platform.eventManager().registerEventHandler(AttackNpcEvent.class, attackNpcEvent -> {
            Npc<World, Player, ItemStack, Plugin> npc = attackNpcEvent.npc();
            Player player = attackNpcEvent.player();
            NPCProfile profile = this.npcs.get(npc);
            if (profile == null) {
                return;
            }

            Crate crate = crateHandler.getByName(profile.getCrateName());
            if (crate == null) {
                return;
            }

            if (player.isSneaking() && player.getGameMode().equals(GameMode.CREATIVE) && player.hasPermission("lootchests.command.crate.edit")) {
                return;
            }

            if (!crate.getLootItems().isEmpty()) {
                new CrateLootMenu(crate).openMenu(player);
            }
        });

        // delete team first
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("argon-npc");
        if (team != null) team.unregister();
        team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("argon-npc");

        team.setDisplayName("Argon NPCs");
        team.setNameTagVisibility(NameTagVisibility.NEVER);
        team.setPrefix(ChatColor.DARK_GRAY + "[NPC] ");
    }

    public void fireUp() {
        File npcsFolder = new File(argonPlugin.getDataFolder(), "npcs");
        for (File npcFile : npcsFolder.listFiles()) {
            if (npcFile.getName().endsWith(".yml")) {
                createNPC(npcFile.getName().replace(".yml", ""));
            }
        }
    }

    public NPCProfile createNPC(String name) {
        return this.profiles.put(name, new NPCProfile(name));
    }

    public NPCProfile createNPC(String name, String crateName, Location location) {
        return this.profiles.put(name, new NPCProfile(name, crateName, location));
    }

    public boolean deleteNPC(String name) {
        NPCProfile npcProfile = this.profiles.remove(name);
        if (npcProfile != null) {
            npcProfile.invalidate();
            npcProfile.getConfigFile().getFile().delete();
            return true;
        }
        return false;
    }
}
