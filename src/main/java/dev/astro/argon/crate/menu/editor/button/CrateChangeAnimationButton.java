package dev.astro.argon.crate.menu.editor.button;

import dev.astro.argon.crate.Crate;
import dev.astro.argon.crate.menu.editor.CrateAnimationsMenu;
import dev.astro.argon.crate.menu.editor.CrateEditorMenu;
import dev.astro.argon.util.ItemBuilder;
import dev.astro.argon.util.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CrateChangeAnimationButton extends Button {

    private final Crate crate;
    private final CrateEditorMenu menu;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(Material.BOOK);

        builder.setName("&bChange Animation");

        List<String> lore = new ArrayList<>();

        String animation = crate.getAnimation() == null || crate.getAnimation().isEmpty()
                ? "None" : crate.getAnimation();

        lore.add(" ");
        lore.add(" &eCurrent Animation: &f" + animation);
        lore.add(" ");
        lore.add(" &eClick to change the animation");
        lore.add(" ");

        builder.setLore(lore);

        return builder.build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        new CrateAnimationsMenu(crate).openMenu(player);
    }
}
