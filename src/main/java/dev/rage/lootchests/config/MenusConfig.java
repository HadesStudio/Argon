package dev.rage.lootchests.config;

import dev.rage.lootchests.util.config.creator.ConfigCreator;
import dev.rage.lootchests.util.config.creator.annotation.Path;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class MenusConfig extends ConfigCreator {

    public static MenusConfig INSTANCE;

    // Back button
    @Path(id = "back.button-title")
    public static  String BACK_BUTTON_TITLE = "&c&lGo back!";

    @Path(id = "back.button-lore")
    public static List<String> BACK_BUTTON_LORE = Arrays.asList(
            "",
            " &eClick here to return",
            " &ethe prevoius menu",
            ""
    );

    // Question button
    @Path(id = "question.confirm-button-title")
    public static String CONFIRM_BUTTON_TITLE = "&a&lConfirm!";

    @Path(id = "question.cancel-button-title")
    public static String CANCEL_BUTTON_TITLE = "&c&lCancel!";

    // Paginated menu
    @Path(id = "pages.menu-title")
    public static String PAGES_MENU_TITLE = "Jump to a page";

    @Path(id = "pages.page-button-title")
    public static String PAGE_BUTTON_TITLE = "&aPage #<page>";

    @Path(id = "pages.current-page-button-lore")
    public static List<String> PAGE_BUTTON_CURRENT_PAGE = Arrays.asList(
            "",
            " &eCurrent page",
            ""
    );

    @Path(id = "pages.next-page-button-title")
    public static String NEXT_PAGE_BUTTON_TITLE = "&aNext page ->";

    @Path(id = "pages.previous-page-button-title")
    public static String PREVIOUS_PAGE_BUTTON_TITLE = "&c<- Previous page";

    @Path(id = "pages.jump-page-button-lore")
    public static List<String> JUMP_PAGE_BUTTON_LORE = Arrays.asList(
            "",
            " &eClick to jump",
            " &eto a page",
            ""
    );

    // Filterable menu
    @Path(id = "filter.button-title")
    public static String FILTERS_BUTTON_TITLE = "&9&lFilters (<filter-count>)";

    @Path(id = "filter.type-format")
    public static String FILTER_TYPE_FORMAT = "Type: <filter>";

    @Path(id = "filter.all-type")
    public static String FILTER_TYPE_ALL = "All";

    @Path(id = "filter.button-lore")
    public static List<String> FILTER_BUTTON_LORE = Arrays.asList(
            "",
            " &eLeft click to scroll",
            " &eRight click to toggle a filter",
            ""
    );

    public MenusConfig(JavaPlugin plugin) {
        super(plugin, "menus.yml");

        INSTANCE = this;
    }
}
