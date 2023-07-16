package dev.rage.argon.util.command.bukkit;

import dev.rage.argon.util.command.CommandContext;
import dev.rage.argon.util.command.CommandHandler;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;

import java.util.Set;

public class BukkitHelpTopic extends HelpTopic {

	private final CommandContext context;
    
    public BukkitHelpTopic(CommandHandler commandHandler, CommandContext context, Set<String> aliases) {
        this.context = context;
        
        this.name = "/" + context.getName();
        
        String description = context.getDescription();
        if (description.length() < 32) {
            this.shortText = description;
        } else {
            this.shortText = description.substring(0, 32);
        }
        
        StringBuilder builder = new StringBuilder();
        builder.append(commandHandler.getConfig().getCommandColor());
        builder.append("Description: ");
        builder.append(ChatColor.WHITE);
        builder.append(context.getDescription());
        builder.append("\n");
        builder.append(commandHandler.getConfig().getCommandColor());
        builder.append("Usage: ");
        builder.append(ChatColor.WHITE);
        builder.append(context.getUsageForHelpTopic());
        
        if (aliases != null && aliases.size() > 0) {
            builder.append("\n");
            builder.append(commandHandler.getConfig().getCommandColor());
            builder.append("Aliases: ");
            builder.append(ChatColor.WHITE);
            builder.append(StringUtils.join(aliases, ", "));
        }
        
        this.fullText = builder.toString();
    }
	
	@Override
	public boolean canSee(CommandSender sender) {
		return context.getCommandExecutor().canExecute(sender);
	}
}
