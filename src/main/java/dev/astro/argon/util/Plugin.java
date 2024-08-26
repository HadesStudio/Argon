package dev.astro.argon.util;

import dev.astro.argon.ArgonPlugin;
import dev.astro.argon.box.BoxHandler;
import dev.astro.argon.command.ArgonCommand;
import dev.astro.argon.config.LanguageConfig;
import dev.astro.argon.config.MenusConfig;
import dev.astro.argon.config.SettingsConfig;
import dev.astro.argon.crate.CrateHandler;
import dev.astro.argon.profile.ProfileHandler;
import dev.astro.argon.util.command.bukkit.BukkitCommandHandler;
import dev.astro.argon.util.menu.MenuListener;
import dev.astro.argon.util.menu.runnable.MenuUpdateRunnable;
import dev.astro.argon.util.prompt.PromptHandler;
import lombok.Getter;
import okhttp3.*;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

@Getter
public class Plugin {

    private final String licenseKey;
    private final org.bukkit.plugin.Plugin pluginClass;

    private int statusCode;
    private String discordName;
    private String discordID;
    private final String statusMsg;

    private final String product;
    private final String version;

    public Plugin(String licenseKey, org.bukkit.plugin.Plugin pluginClass) {
        this.licenseKey = licenseKey;
        this.pluginClass = pluginClass;
        this.product = pluginClass.getName();
        this.version = pluginClass.getDescription().getVersion();

        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"licensekey\": \"" + licenseKey + "\",\n    \"product\": \"" + product + "\",\n    \"version\": \"" + version + "\"\n}");

            Request request = new Request.Builder()
                    .url("https://astro-operations.com/api/client")
                    .method("POST", body)
                    .addHeader("Authorization", "czJL6eALYaAyqzbmRX6hv3qVdQOnMdLJsDyacztANx5AFU81")
                    .build();

            Response response = client.newCall(request).execute();

            String data = response.body().string();
            JSONObject obj = new JSONObject(data);

            if (obj.has("status_msg") && obj.has("status_id")) {
                statusCode = obj.getInt("status_code");
                statusMsg = obj.getString("status_msg");

                if (obj.has("status_overview")) {
                    discordName = obj.getString("discord_username");
                    discordID = obj.getString("discord_id");

                    Arrays.asList("      &f&l* &4&lArgon &7(&fCrates&7) &f&l*       ",
                                    "",
                                    " &f&l* &4Authors&7: &f" + ArgonPlugin.getPlugin().getDescription().getAuthors(),
                                    " &f&l* &4Plugin Version&7: &f" + ArgonPlugin.getPlugin().getDescription().getVersion(),
                                    " &f&l* &4License Status&7: &aVALID",
                                    " &f&l* &4Thanks for using Argon crates <3",
                                    " &f&l* &4If u have support join to &nastro-operations.com/discord")
                            .forEach(s ->
                                    Bukkit.getConsoleSender().sendMessage(ColorUtil.colorize(s)));


                    new LanguageConfig(ArgonPlugin.getPlugin());
                    new MenusConfig(ArgonPlugin.getPlugin());
                    new SettingsConfig(ArgonPlugin.getPlugin());

                    ArgonPlugin.get().setCommandHandler(new BukkitCommandHandler(ArgonPlugin.getPlugin()));
                    ArgonPlugin.get().setPromptHandler(new PromptHandler(ArgonPlugin.getPlugin()));

                    ArgonPlugin.get().setCrateHandler(new CrateHandler(ArgonPlugin.getPlugin()));
                    ArgonPlugin.get().setProfileHandler(new ProfileHandler(ArgonPlugin.getPlugin()));
                    ArgonPlugin.get().setBoxHandler(new BoxHandler(ArgonPlugin.getPlugin()));

                    ArgonPlugin.get().getCommandHandler().register(new ArgonCommand());

                    Bukkit.getServer().getPluginManager().registerEvents(new MenuListener(ArgonPlugin.getPlugin()), ArgonPlugin.getPlugin());

                    ArgonPlugin.getPlugin().getServer().getScheduler().runTaskTimer(ArgonPlugin.getPlugin(), new MenuUpdateRunnable(), 10L, 10L);
                }
            } else {
                statusMsg = obj.getString("status_msg");

                Arrays.asList("      &f&l* &4&lArgon &7(&fCrates&7) &f&l*       ",
                                "",
                                " &f&l* &4Authors&7: &f" + ArgonPlugin.getPlugin().getDescription().getAuthors(),
                                " &f&l* &4Plugin Version&7: &f" + ArgonPlugin.getPlugin().getDescription().getVersion(),
                                " &f&l* &4License Status&7: &cINVALID",
                                " ",
                                " &f&l* &4If u have support join to &nastro-operations.com/discord")
                        .forEach(s ->
                                Bukkit.getConsoleSender().sendMessage(ColorUtil.colorize(s)));
            }
        }

        catch (IOException e) {
            throw new RuntimeException("Failed to connect to the license server.");
        }
    }
}