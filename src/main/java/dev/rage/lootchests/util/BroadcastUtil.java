package dev.rage.lootchests.util;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BroadcastUtil {

	public void sendAll(String message) {
		Bukkit.getServer().getConsoleSender().sendMessage(ColorUtil.colorize(message));
		
		for(Player online : BukkitUtil.getOnlinePlayers()) {
			online.sendMessage(ColorUtil.colorize(message));
		}
	}
	
	public void sendAll(List<String> message) {
		for(String line : message) { 
			sendAll(line);
		}
	}
	
	public void sendAll(String... message) {
		sendAll(Arrays.asList(message));
	}
	
	public void sendPlayers(String message) {
		for(Player online : BukkitUtil.getOnlinePlayers()) {
			online.sendMessage(ColorUtil.colorize(message));
		}
	}
	
	public void sendPlayers(List<String> message) {
		for(String line : message) { 
			sendPlayers(line);
		}
	}
	
	public void sendPlayers(String... message) {
		sendPlayers(Arrays.asList(message));
	}
	
	public void sendConsole(String message) {
		Bukkit.getServer().getConsoleSender().sendMessage(ColorUtil.colorize(message));
	}
	
	public void sendConsole(List<String> message) {
		for(String line : message) { 
			sendConsole(line);
		}
	}
	
	public void sendConsole(String... message) {
		sendConsole(Arrays.asList(message));
	}
	
	public void sendWithPermission(String permission, String message) {
		sendConsole(message);
		
		for (Player online : BukkitUtil.getOnlinePlayers()) {
			if (TextUtil.isNull(permission)) {
				online.sendMessage(ColorUtil.colorize(message));
			} else {
				if(online.hasPermission(permission)) {
					online.sendMessage(ColorUtil.colorize(message));
				}
			}
		}
	}
	
	public void sendWithPermission(String permission, List<String> message) {
		for(String line : message) {
			sendWithPermission(permission, line);
		}
	}
	
	public void sendWithPermission(String permission, String... message) {
		sendWithPermission(permission, Arrays.asList(message));
	}
	
	public void sendIf(Function<Player, Boolean> function, String message) {
		for (Player online : BukkitUtil.getOnlinePlayers()) {
			if (function.apply(online)) {
				online.sendMessage(ColorUtil.colorize(message));
			}
		}
	}
	
	public void sendIf(Function<Player, Boolean> function, List<String> message) {
		for (String line : message) {
			sendIf(function, line);
		}
	}
	
	public void sendIf(Function<Player, Boolean> function, String... message) {
		sendIf(function, Arrays.asList(message));
	}
}