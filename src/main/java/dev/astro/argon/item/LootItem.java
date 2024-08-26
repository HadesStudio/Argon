package dev.astro.argon.item;

import dev.astro.argon.util.ItemBuilder;
import dev.astro.argon.util.ReflectionUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class LootItem {

    public static ItemStack LUCKY_BLOCK_ITEM = new ItemBuilder(ReflectionUtil.getSkullItem())
            .setSkullTexture(
                    "ac4970ea91ab06ece59d45fce7604d255431f2e03a737b226082c4cce1aca1c4",
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM0OTcwZWE5MWFiMDZlY2U1OWQ0NWZjZTc2MDRkMjU1NDMxZjJlMDNhNzM3YjIyNjA4MmM0Y2NlMWFjYTFjNCJ9fX0="
            )
            .build();

    public static ItemStack MYSTERY_BOX_ITEM = new ItemBuilder(ReflectionUtil.getSkullItem())
            .setSkullTexture(
                    "cebf43ecd8d8ff0e2a533b15ea9a91e412ada2696af5f4a9368da24309b227de",
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2ViZjQzZWNkOGQ4ZmYwZTJhNTMzYjE1ZWE5YTkxZTQxMmFkYTI2OTZhZjVmNGE5MzY4ZGEyNDMwOWIyMjdkZSJ9fX0="
            )
            .build();

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
