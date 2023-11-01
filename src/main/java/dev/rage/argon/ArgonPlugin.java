package dev.rage.argon;

import dev.rage.argon.util.Argon;
import org.bukkit.plugin.java.JavaPlugin;

public class ArgonPlugin extends JavaPlugin {

    private static Argon instance;

    @Override
    public void onEnable() {
        instance = new Argon(this);
        instance.onEnable();
    }

    @Override
    public void onDisable() {
        instance.onDisable();
    }

    public static Argon get() {
        return instance;
    }

    public static ArgonPlugin getPlugin() {
        return ArgonPlugin.getPlugin(ArgonPlugin.class);
    }
}