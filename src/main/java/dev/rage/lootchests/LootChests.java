package dev.rage.lootchests;

import dev.rage.lootchests.command.LootChestCommand;
import dev.rage.lootchests.config.LanguageConfig;
import dev.rage.lootchests.config.MenusConfig;
import dev.rage.lootchests.config.SettingsConfig;
import dev.rage.lootchests.crate.CrateHandler;
import dev.rage.lootchests.profile.ProfileHandler;
import dev.rage.lootchests.util.command.CommandHandler;
import dev.rage.lootchests.util.command.bukkit.BukkitCommandHandler;
import dev.rage.lootchests.util.menu.MenuListener;
import dev.rage.lootchests.util.menu.runnable.MenuUpdateRunnable;
import dev.rage.lootchests.util.prompt.PromptHandler;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

@Getter
public class LootChests extends JavaPlugin {

    private static Optional<LootChests> instance;

    private CommandHandler commandHandler;
    private PromptHandler promptHandler;

    private CrateHandler crateHandler;
    private ProfileHandler profileHandler;

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

    public static LootChests get() {
        return instance.orElseThrow(() -> new IllegalStateException("LootChests instance cannot be null"));
    }
}
