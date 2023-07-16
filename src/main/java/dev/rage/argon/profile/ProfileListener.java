package dev.rage.argon.profile;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class ProfileListener implements Listener {

    private final ProfileHandler profileHandler;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (profileHandler.findProfile(player.getUniqueId()) == null) profileHandler.loadProfile(player.getUniqueId());
    }
}
