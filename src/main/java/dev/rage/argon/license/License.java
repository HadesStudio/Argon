package dev.rage.argon.license;

import lombok.Getter;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

@Getter
public class License {

    private final String licenseKey;
    private final org.bukkit.plugin.Plugin pluginClass;

    private int statusCode;
    private String discordName = "N/A";
    private String discordID = "N/A";
    private String statusMsg;

    private final String product;
    private final String version;

    private boolean enabled;

    public License(String licenseKey, org.bukkit.plugin.Plugin pluginClass) {
        this.licenseKey = licenseKey;
        this.pluginClass = pluginClass;
        this.product = pluginClass.getName();
        this.version = pluginClass.getDescription().getVersion();

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"license\": \""+ licenseKey +"\",\n    \"product\": \""+ product +"\",\n    \"version\": \""+ version +"\"\n}");
            Request request = new Request.Builder()
                    .url("https://astro-operations.com/api/client")
                    .method("POST", body)
                    .addHeader("Authorization", "czJL6eALYaAyqzbmRX6hv3qVdQOnMdLJsDyacztANx5AFU81")
                    .build();

            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();

            if (responseBody != null) {
                String data = responseBody.string();
                JSONObject obj = new JSONObject(data);

                if (obj.has("status_msg") && obj.has("status_id")) {
                    statusCode = obj.getInt("status_code");
                    statusMsg = obj.getString("status_msg");

                    if (obj.has("status_overview")) {
                        discordName = obj.getString("discord_tag");
                        discordID = obj.getString("discord_id");

                        enabled = true;
                    }
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to connect to the license server.");
        }
    }
}