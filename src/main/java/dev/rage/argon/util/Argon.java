package dev.rage.argon.util;

import dev.rage.argon.ArgonPlugin;
import dev.rage.argon.box.BoxHandler;
import dev.rage.argon.crate.CrateHandler;
import dev.rage.argon.profile.ProfileHandler;
import dev.rage.argon.util.command.CommandHandler;
import dev.rage.argon.util.config.Config;
import dev.rage.argon.util.prompt.PromptHandler;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Argon {

    private CommandHandler commandHandler;
    private PromptHandler promptHandler;

    private CrateHandler crateHandler;
    private ProfileHandler profileHandler;
    private BoxHandler boxHandler;

    private Config licenseFile;

    private boolean refused = false;

    private Plugin plugins;
    private ArgonPlugin plugin;

    public Argon(ArgonPlugin plugin) {
        this.plugin = plugin;
    }

    public void onEnable() {
        this.licenseFile = new Config(ArgonPlugin.getPlugin(), "license.yml");

        this.plugins = new Plugin(this.getLicenseFile().getString("license"), plugin);
    }

    public void onDisable() {
        if (!refused) {
            crateHandler.handleDisable();
            profileHandler.handleDisable();
        }
    }
}
