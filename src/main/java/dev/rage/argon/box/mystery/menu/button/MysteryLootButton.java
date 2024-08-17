package dev.rage.argon.box.mystery.menu.button;

import dev.rage.argon.box.mystery.MysteryBox;
import dev.rage.argon.item.LootItem;
import dev.rage.argon.util.ItemBuilder;
import dev.rage.argon.util.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class MysteryLootButton extends Button {

    private final MysteryBox mysteryBox;
    private final MysteryBox.MysteryLoot mysteryLoot;
    private final boolean opLoot;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (mysteryLoot.isDiscovered()) {
            return mysteryLoot.getReward();
        }

        ItemBuilder builder = new ItemBuilder(opLoot ? LootItem.LUCKY_BLOCK_ITEM : LootItem.MYSTERY_BOX_ITEM);
        builder.setAmount(1);
        builder.setName((opLoot ? "&6&l" : "&c") + "???");
        return builder.build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (opLoot) {
            if (mysteryBox.isAllDiscovered()) {
                if (!mysteryLoot.isDiscovered()) {
                    mysteryLoot.setDiscovered(true);
                    Button.playSuccess(player);
                }
            } else {
                Button.playFail(player);
            }
        } else {
            if (!mysteryLoot.isDiscovered()) {
                mysteryLoot.setDiscovered(true);
                Button.playSuccess(player);
            }
        }
    }
}
