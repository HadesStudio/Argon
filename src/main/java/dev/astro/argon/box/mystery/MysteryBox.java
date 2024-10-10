package dev.astro.argon.box.mystery;

import dev.astro.argon.ArgonPlugin;
import dev.astro.argon.util.BroadcastUtil;
import dev.astro.argon.util.InventoryUtil;
import dev.astro.argon.util.TextUtil;
import dev.astro.argon.util.config.Replacement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class MysteryBox {

    private final ItemStack[] commonLoot, opLoot;

    private final List<MysteryLoot> rewards;
    private final MysteryLoot opReward;

    @Setter private String announcement;

    public MysteryBox(ItemStack[] commonLoot, ItemStack[] opLoot) {
        this.commonLoot = commonLoot;
        this.opLoot = opLoot;

        this.rewards = new ArrayList<>();
        this.opReward = selectReward(opLoot);

        this.announcement = ArgonPlugin.get().getBoxHandler().getMysteryBoxAnnounce();

        for (int i = 0; i < 9; i++) {
            rewards.add(selectReward(commonLoot));
        }
    }

    private MysteryLoot selectReward(ItemStack[] loot) {
        return new MysteryLoot(loot[ThreadLocalRandom.current().nextInt(loot.length)]);
    }

    public void giveRewards(Player player) {
        if (InventoryUtil.isFull(player)) {
            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), opReward.reward);
        } else {
            player.getInventory().addItem(opReward.reward);
        }

        if (!TextUtil.isNull(announcement)) {
            Replacement replacement = new Replacement(announcement);
            replacement.add("<player>", player.getName());
            replacement.add("<item-name>", opReward.reward.getItemMeta().getDisplayName() == null ? opReward.reward.getType().name() : opReward.reward.getItemMeta().getDisplayName());
            replacement.add("<item-amount>", opReward.reward.getAmount());

            BroadcastUtil.sendAll(replacement.toString());
        }

        for (MysteryLoot loot : rewards) {
            if (InventoryUtil.isFull(player)) {
                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), loot.reward);
            } else {
                player.getInventory().addItem(loot.reward);
            }
        }
    }

    public boolean isAllDiscovered() {
        return rewards.stream().filter(MysteryLoot::isDiscovered).count() == 9;
    }

    @Getter @Setter
    @RequiredArgsConstructor
    public static class MysteryLoot {

        private final ItemStack reward;
        private boolean discovered = false;
    }
}
