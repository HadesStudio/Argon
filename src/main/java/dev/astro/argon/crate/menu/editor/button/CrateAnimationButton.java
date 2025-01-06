package dev.astro.argon.crate.menu.editor.button;

import com.cryptomorin.xseries.XMaterial;
import dev.astro.argon.ArgonPlugin;
import dev.astro.argon.animation.IAnimation;
import dev.astro.argon.crate.Crate;
import dev.astro.argon.util.ItemBuilder;
import dev.astro.argon.util.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CrateAnimationButton extends Button {

    private final Crate crate;
    private final IAnimation animation;

    @Override
    public ItemStack getButtonItem(Player player) {
        XMaterial xMaterial = animation.getXMaterial();
        ItemBuilder builder = new ItemBuilder(xMaterial.parseMaterial(), 1, xMaterial.getData());

        builder.setName("&b" + animation.getName());

        List<String> lore = new ArrayList<>();

        lore.add(" ");
        lore.add(" &eClick to change to " + animation.getName());
        lore.add(" ");

        builder.setLore(lore);

        return builder.build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        crate.setAnimation(animation.getName());
        ArgonPlugin.get().getCrateHandler().save();
        player.closeInventory();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou have set the animation to " + animation.getName()));
    }
}
