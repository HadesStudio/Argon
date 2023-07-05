package dev.rage.lootchests.util.menu.confirmation.button;

import dev.rage.lootchests.config.MenusConfig;
import dev.rage.lootchests.util.Callback;
import dev.rage.lootchests.util.ItemBuilder;
import dev.rage.lootchests.util.menu.Menu;
import dev.rage.lootchests.util.menu.button.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class ConfirmButton extends Button {
	
    private boolean confirm;
    private Callback<Boolean> callback;
    private boolean closeAfterResponse;
    
    @Override
    public ItemStack getButtonItem(Player player) {
    	ItemBuilder itemBuilder = new ItemBuilder(Material.WOOL);
    	itemBuilder.setName(confirm ? MenusConfig.CONFIRM_BUTTON_TITLE : MenusConfig.CANCEL_BUTTON_TITLE);
    	itemBuilder.setDurability(confirm ? 5 : 14);

        return itemBuilder.build();
    }
    
    @Override
    public void clicked(Player player, ClickType clickType) {
        if (confirm) {
            Button.playSuccess(player);
        } else {
            Button.playFail(player);
        }
        
        if (closeAfterResponse) {
            Menu menu = Menu.currentlyOpenedMenus.get(player.getName());
            if (menu != null) {
                menu.setClosedByMenu(true);
            }
            
            player.closeInventory();
        }
        
        callback.callback(this.confirm);
    }
}
