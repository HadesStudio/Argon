package dev.rage.argon;

import dev.rage.argon.command.ArgonCommand;
import dev.rage.argon.config.LanguageConfig;
import dev.rage.argon.config.MenusConfig;
import dev.rage.argon.config.SettingsConfig;
import dev.rage.argon.crate.CrateHandler;
import dev.rage.argon.hologram.HologramHandler;
import dev.rage.argon.license.License;
import dev.rage.argon.profile.ProfileHandler;
import dev.rage.argon.util.BroadcastUtil;
import dev.rage.argon.util.command.CommandHandler;
import dev.rage.argon.util.command.bukkit.BukkitCommandHandler;
import dev.rage.argon.util.config.Config;
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

    private boolean refused = false;

    @Override
    public void onLoad() {
        instance = Optional.of(this);
    }

    @Override
    public void onEnable() {
        Config config = new Config(this, "license.yml");

        if (!new License(config.getString("license"), this).isEnabled()) {
            refused = true;
            getServer().getPluginManager().disablePlugin(this);
            getServer().getScheduler().cancelTasks(this);

            BroadcastUtil.sendConsole(SettingsConfig.PRIMARY_COLOR + "[Argon] &cLicense key is invalid!");

            return;
        }

        new LanguageConfig(this);
        new MenusConfig(this);
        new SettingsConfig(this);

        commandHandler = new BukkitCommandHandler(this);
        promptHandler = new PromptHandler(this);

        hologramHandler = new HologramHandler();
        crateHandler = new CrateHandler(this);
        profileHandler = new ProfileHandler(this);

        commandHandler.register(new ArgonCommand());

        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new MenuListener(this), this);

        getServer().getScheduler().runTaskTimer(this, new MenuUpdateRunnable(), 10L, 10L);
    }

    @Override
    public void onDisable() {
        if (!refused) {
            crateHandler.handleDisable();
            profileHandler.handleDisable();
        }
    }

    public static Argon get() {
        return instance.orElseThrow(() -> new IllegalStateException("LootChests instance cannot be null"));
    }
}
