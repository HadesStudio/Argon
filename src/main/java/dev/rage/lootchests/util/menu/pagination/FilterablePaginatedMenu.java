package dev.rage.lootchests.util.menu.pagination;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.rage.lootchests.util.menu.button.Button;
import dev.rage.lootchests.util.menu.pagination.button.PageFilterButton;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

@Getter 
public abstract class FilterablePaginatedMenu<T> extends PaginatedMenu {

	private final List<PageFilter<T>> filters;
	@Setter private int scrollIndex = 0;

	{
		filters = (generateFilters() == null ? Lists.newArrayList() : generateFilters());
	}

	@Override
	public Map<Integer, Button> getGlobalButtons(Player player) {
		Map<Integer, Button> buttons = Maps.newHashMap();
		buttons.put(40, new PageFilterButton<T>(this));
		return buttons;
	}

	@Override
	public Map<Integer, Button> getAllPagesButtons(Player player) {
		return getFilteredButtons(player);
	}

	public abstract Map<Integer, Button> getFilteredButtons(Player player);

	public abstract List<PageFilter<T>> generateFilters();
}
