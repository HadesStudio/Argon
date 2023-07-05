package dev.rage.lootchests.crate.menu.location;

import com.google.common.collect.Maps;
import dev.rage.lootchests.config.SettingsConfig;
import dev.rage.lootchests.crate.Crate;
import dev.rage.lootchests.crate.menu.editor.CrateEditorMenu;
import dev.rage.lootchests.crate.menu.location.button.CrateLocationButton;
import dev.rage.lootchests.util.menu.button.Button;
import dev.rage.lootchests.util.menu.button.impl.BackButton;
import dev.rage.lootchests.util.menu.pagination.PaginatedMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Map;

@RequiredArgsConstructor
public class CrateLocationsMenu extends PaginatedMenu {

	private final Crate crate;
	
	@Override
	public String getPrePaginatedTitle(Player player) {
		return crate.getColor() + crate.getName() + SettingsConfig.SECONDARY_COLOR + " Locations";
	}

	@Override
	public Map<Integer, Button> getAllPagesButtons(Player player) {
		Map<Integer, Button> buttons = Maps.newHashMap();
		
		getButtons().put(0, new BackButton(new CrateEditorMenu(crate)));
		
		crate.getLocations().forEach(loc -> buttons.put(buttons.size(), new CrateLocationButton(buttons.size(), loc)));
		
		return buttons;
	}

}
