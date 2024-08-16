package dev.rage.argon.box.mystery.menu;

import dev.rage.argon.box.mystery.MysteryBox;
import dev.rage.argon.box.mystery.menu.button.MysteryLootButton;
import dev.rage.argon.util.menu.Menu;
import dev.rage.argon.util.menu.button.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class MysteryMenu extends Menu {

    private final MysteryBox mysteryBox;

    {
        setUpdateAfterClick(true);
    }

    @Override
    public String getTitle(Player player) {
        return "Mystery Box";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int i = 0; i < 54; i++) {
            buttons.put(i, getPlaceholderButton());
        }

        for (int i = 0; i < 9; i++) {
            if (i > 2 && i < 6) {
                buttons.put(18 + i, new MysteryLootButton(mysteryBox, mysteryBox.getRewards().get(i), false));
            } else if (i > 5) {
                buttons.put(24 + i, new MysteryLootButton(mysteryBox, mysteryBox.getRewards().get(i), false));
            } else {
                buttons.put(12 + i, new MysteryLootButton(mysteryBox, mysteryBox.getRewards().get(i), false));
            }
        }

        buttons.put(49, new MysteryLootButton(mysteryBox, mysteryBox.getOpReward(), true));

        return buttons;
    }

    @Override
    public void onClose(Player player) {
        mysteryBox.giveRewards(player);
        Button.playNeutral(player);
        player.updateInventory();
    }
}
