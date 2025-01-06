package dev.astro.argon.npc;

import com.github.juliarn.npclib.api.Npc;
import com.github.juliarn.npclib.api.Platform;
import com.github.juliarn.npclib.api.profile.Profile;
import com.github.juliarn.npclib.bukkit.util.BukkitPlatformUtil;
import dev.astro.argon.ArgonPlugin;
import dev.astro.argon.config.ConfigFile;
import dev.astro.argon.util.NekoLocation;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This code has been created by
 * gatogamer#6666 A.K.A. gatogamer.
 * If you want to use my code, please
 * ask first, and give me the credits.
 * Arigato! n.n
 */
@Getter
@Setter
public class NPCProfile {
    private final String name;
    private final ConfigFile configFile;

    private NekoLocation nekoLocation;
    private String crateName;
    private String skinName;
    private boolean hideName;
    private String animation;
    private List<String> hologramLines;

    private Npc<World, Player, ItemStack, Plugin> npc;

    public NPCProfile(String name) {
        this.name = name;

        this.configFile = new ConfigFile(ArgonPlugin.getPlugin(), "npcs/" + name);
        this.crateName = this.configFile.getString("crate-name");
        this.skinName = this.configFile.getString("skin-name", "Steve");
        this.animation = this.configFile.getString("animation", "");
        this.hologramLines = this.configFile.getStringList("hologram-lines");
        this.nekoLocation = new NekoLocation(this.configFile.getString("location.world"), this.configFile.getDouble("location.x"), this.configFile.getDouble("location.y"), this.configFile.getDouble("location.z"), this.configFile.getFloat("location.yaw"), this.configFile.getFloat("location.pitch"));

        createNPC();
    }

    public NPCProfile(String name, String crateName, Location location) {
        this.name = name;
        this.crateName = crateName;

        this.configFile = new ConfigFile(ArgonPlugin.getPlugin(), "npcs/" + name);
        this.hologramLines = new ArrayList<>();
        this.nekoLocation = new NekoLocation(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.flushToConfig();

        createNPC();
    }

    public void flushToConfig() {
        this.configFile.set("crate-name", this.crateName);
        this.configFile.set("hologram-lines", this.hologramLines);
        this.configFile.set("location.world", this.nekoLocation.getWorld());
        this.configFile.set("location.x", this.nekoLocation.getX());
        this.configFile.set("location.y", this.nekoLocation.getY());
        this.configFile.set("location.z", this.nekoLocation.getZ());
        this.configFile.set("location.yaw", this.nekoLocation.getYaw());
        this.configFile.set("location.pitch", this.nekoLocation.getPitch());
        this.configFile.set("skin-name", this.skinName);
        this.configFile.set("hide-name", this.hideName);
        this.configFile.set("animation", this.animation);
        this.configFile.save();
    }

    public void reload() {
        this.configFile.reload();
        this.crateName = this.configFile.getString("crate-name");
        this.hologramLines = this.configFile.getStringList("hologram-lines");
        this.nekoLocation = new NekoLocation(
                this.configFile.getString("location.world"),
                this.configFile.getDouble("location.x"),
                this.configFile.getDouble("location.y"),
                this.configFile.getDouble("location.z"),
                this.configFile.getFloat("location.yaw"),
                this.configFile.getFloat("location.pitch")
        );
        this.skinName = this.configFile.getString("skin-name");
        this.hideName = this.configFile.getBoolean("hide-name");
        this.createNPC();
    }

    @SneakyThrows
    public void createNPC() {
        invalidate();
        Platform<World, Player, ItemStack, Plugin> platform = ArgonPlugin.get()
                .getNpcRegistrar()
                .getPlatform();
        this.npc = platform.newNpcBuilder()
                .position(BukkitPlatformUtil.positionFromBukkitLegacy(
                        new Location(
                                Bukkit.getWorld(this.nekoLocation.getWorld()),
                                this.nekoLocation.getX(), this.nekoLocation.getY(), this.nekoLocation.getZ(),
                                this.nekoLocation.getYaw(), this.nekoLocation.getPitch()
                        ))
                )
                .npcSettings(builder -> builder
                        .profileResolver((player, npc) -> {
                            Profile playerProfile = Profile.unresolved(this.skinName == null ? "Steve" : this.skinName);
                            return platform.profileResolver()
                                    .resolveProfile(playerProfile)
                                    .thenApply(profile -> npc.profile().withProperties(profile.properties()));
                        }))
                .profile(Profile.resolved(this.name, UUID.randomUUID()))
                .buildAndTrack();

        ArgonPlugin.get().getNpcRegistrar().getNpcs().put(this.npc, this);
        if (!this.hideName) {
            return;
        }
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("argon-npc");
        if (team != null) {
            team.addEntry(this.name);
        }
    }

    public void invalidate() {
        if (this.npc != null) {
            this.npc.unlink();
            ArgonPlugin.get().getNpcRegistrar().getNpcs().remove(this.npc);
            this.npc = null;

            Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("argon-npc");
            if (team != null) {
                team.removeEntry(this.name);
            }
        }
    }
}
