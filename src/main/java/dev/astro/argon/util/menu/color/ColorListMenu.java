package dev.astro.argon.util.menu.color;

import com.google.common.collect.Maps;
import dev.astro.argon.config.SettingsConfig;
import dev.astro.argon.util.Callback;
import dev.astro.argon.util.menu.Menu;
import dev.astro.argon.util.menu.button.Button;
import dev.astro.argon.util.menu.button.impl.BackButton;
import dev.astro.argon.util.menu.color.button.ColorSelectButton;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ColorListMenu extends Menu {

    private final Callback<ChatColor> callback;
	private final Menu backMenu;

	private List<ChatColor> getColors() {
		List<ChatColor> colors = new ArrayList<>();
		
		for(ChatColor color : ChatColor.values()) {
			if(!color.equals(ChatColor.UNDERLINE)
					&& !color.equals(ChatColor.STRIKETHROUGH)
					&& !color.equals(ChatColor.BOLD)
					&& !color.equals(ChatColor.RESET)
					&& !color.equals(ChatColor.MAGIC)
					&& !color.equals(ChatColor.ITALIC)) {
				colors.add(color);
			}
		}
		
		return colors;
	}
    
    @Override
    public String getTitle(Player player) {
        return SettingsConfig.PRIMARY_COLOR + "Colors (" + getColors().size() + ")";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

		if (backMenu != null) {
			buttons.put(0, new BackButton(backMenu));

			for (int i = 0; i < 9; i++) buttons.put(9 + i, getPlaceholderButton());
		}

        int i = (backMenu != null ? 18 : 0);
		for(ChatColor color : getColors()) {
			buttons.put(i, new ColorSelectButton(callback, color, backMenu));
			i++;
		}

        return buttons;
    }
}
