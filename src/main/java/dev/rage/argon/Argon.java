package dev.rage.argon;

import dev.rage.argon.command.LootChestCommand;
import dev.rage.argon.config.LanguageConfig;
import dev.rage.argon.config.MenusConfig;
import dev.rage.argon.config.SettingsConfig;
import dev.rage.argon.crate.CrateHandler;
import dev.rage.argon.hologram.HologramHandler;
import dev.rage.argon.profile.ProfileHandler;
import dev.rage.argon.util.command.CommandHandler;
import dev.rage.argon.util.command.bukkit.BukkitCommandHandler;
import dev.rage.argon.util.menu.MenuListener;
import dev.rage.argon.util.menu.runnable.MenuUpdateRunnable;
import dev.rage.argon.util.prompt.PromptHandler;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

@Getter
public class Argon extends JavaPlugin {

    private static Optional<Argon> instance;

    private CommandHandler commandHandler;
    private PromptHandler promptHandler;

    private CrateHandler crateHandler;
    private ProfileHandler profileHandler;
    private HologramHandler hologramHandler;

    @Override
    public void onLoad() {
        instance = Optional.of(this);
    }

    @Override
    public void onEnable() {
        new LanguageConfig(this);
        new MenusConfig(this);
        new SettingsConfig(this);

        commandHandler = new BukkitCommandHandler(this);
        promptHandler = new PromptHandler(this);

        crateHandler = new CrateHandler(this);
        profileHandler = new ProfileHandler(this);
        hologramHandler = new HologramHandler();

        commandHandler.register(new LootChestCommand());

        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new MenuListener(this), this);

        getServer().getScheduler().runTaskTimer(this, new MenuUpdateRunnable(), 10L, 10L);
    }

    @Override
    public void onDisable() {
        crateHandler.handleDisable();
        profileHandler.handleDisable();
    }

    public static Argon get() {
        return instance.orElseThrow(() -> new IllegalStateException("LootChests instance cannot be null"));
    }
}
