package dev.astro.argon.profile.command;

import dev.astro.argon.profile.menu.CrateViewOwnMenu;
import dev.astro.argon.util.command.annotation.Command;
import org.bukkit.entity.Player;

public class ProfileCommands {

    @Command(names = "mykeys")
    public void mykeys(Player player) {
        new CrateViewOwnMenu().openMenu(player);
    }
}
