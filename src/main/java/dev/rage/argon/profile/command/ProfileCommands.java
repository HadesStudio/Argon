package dev.rage.argon.profile.command;

import dev.rage.argon.profile.menu.CrateViewOwnMenu;
import dev.rage.argon.util.command.annotation.Command;
import org.bukkit.entity.Player;

public class ProfileCommands {

    @Command(names = "mykeys")
    public void mykeys(Player player) {
        new CrateViewOwnMenu().openMenu(player);
    }
}
