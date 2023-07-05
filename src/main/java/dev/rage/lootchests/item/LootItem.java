package dev.rage.lootchests.item;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class LootItem {

    private final ItemStack itemStack;

    private int position;

    private String announcement;
    private int chance;
    private List<String> commands;

    public LootItem(ItemStack itemStack) {
        this.itemStack = itemStack;

        position = 0;

        chance = 100;
        commands = new ArrayList<>();
    }
}
