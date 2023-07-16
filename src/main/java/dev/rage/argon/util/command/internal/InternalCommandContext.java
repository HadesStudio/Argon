package dev.rage.argon.util.command.internal;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import dev.rage.argon.util.BukkitUtil;
import dev.rage.argon.util.ColorUtil;
import dev.rage.argon.util.command.*;
import dev.rage.argon.util.command.provider.ProviderData;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class InternalCommandContext implements CommandContext {

	private String name;
    private Set<String> aliases;
    private String permission;
    private String description;
    private boolean async;
    private boolean hidden;
    private boolean root;
        
    public Method method;
    protected Class<?> owningClass;
    private Class<?> senderClass;
    private List<ProviderData> parameters;
    
    private Map<String, CommandContext> childrens;
    private CommandContext rootCommandContext;
    
	private final CommandHandler commandHandler;
	
	public InternalCommandContext(Class<?> senderClass, Class<?> owningClass, CommandHandler commandHandler) {
		this.aliases = Sets.newHashSet();
		this.parameters = Lists.newArrayList();
		this.childrens = Maps.newHashMap();
		
		this.senderClass = senderClass;
		this.owningClass = owningClass;
		this.commandHandler = commandHandler;
	}
	
	public InternalCommandContext(CommandHandler commandHandler) {
		this.aliases = Sets.newHashSet();
		this.parameters = Lists.newArrayList();
		this.childrens = Maps.newHashMap();
		
		this.commandHandler = commandHandler;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public Set<String> getAliases() {
		return aliases;
	}

	@Override
	public String getPermission() {
		return permission;
	}

	@Override
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	@Override
	public boolean hasPermission() {
		return (permission != null && !permission.isEmpty());
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public boolean hasDescription() {
		return (description != null && !description.isEmpty());
	}

	@Override
	public boolean isAsync() {
		return async;
	}

	@Override
	public void setAsync(boolean async) {
		this.async = async;
	}
	
	@Override
	public boolean isHidden() {
		return hidden;
	}
	
	@Override
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	@Override
	public boolean isRoot() {
		return root;
	}

	@Override
	public void setRoot(boolean root) {
		this.root = root;
	}
	
	@Override
	public Method getMethod() {
		return method;
	}

	@Override
	public void setMethod(Method method) {
		this.method = method;
	}
	
	@Override
	public Class<?> getOwningClass() {
		return owningClass;
	}

	@Override
	public Class<?> getSenderClass() {
		return senderClass;
	}
	
	@Override
	public List<ProviderData> getParameters() {
		return parameters;
	}

	@Override
	public void setParameters(List<ProviderData> parameters) {
		this.parameters = parameters;
	}
	
	@Override
	public Map<String, CommandContext> getChildrens() {
		return childrens;
	}

	@Override
	public CommandContext getRootContext() {
		return rootCommandContext;
	}

	@Override
	public void setRootContext(CommandContext context) {
		this.rootCommandContext = context;
	}
	
	@Override
	public Set<String> getRealAliases() {
		Set<String> aliases = this.getAliases();
        aliases.remove(this.getName());
        
        return aliases;
	}

	@Override
	public String getFullLabel() {
		List<String> labels = Lists.newArrayList();
        
        for(CommandContext context = this; context != null; context = context.getRootContext()) {
            String name = context.getName();
            if (name != null) {
                labels.add(name);
            }
        }
        
        Collections.reverse(labels);
        
        labels.remove(0);
        
        StringBuilder builder = new StringBuilder();
        labels.forEach(s -> builder.append(s).append(' '));
        
        return builder.toString().trim();
	}

	protected Comparator<CommandContext> contextComparator = Comparator.comparing(CommandContext::getName);
	
	@Override
	public List<TextComponent> getHelp(CommandSender sender, boolean print) {
		List<TextComponent> help = Lists.newArrayList();
    	
    	
    	List<CommandContext> contexts = this.getChildrens().values().stream().collect(Collectors.toList());
    	contexts.sort(contextComparator);
    	
    	for(CommandContext context : contexts) {
    		if(context.isRoot()) {
    			if(context.getCommandExecutor().canExecute(sender)) {
    				String label = this.commandHandler.getConfig().getCommandColor().toString() + '/' + context.getFullLabel() + (this.parameters != null ? " " + context.getUsageGenerator().generate() : "");
        			
        			if(label.endsWith(" ")) {
        				label = label.substring(0, label.length() - 1);
                	}
        			
                	String description = this.commandHandler.getConfig().getCommaColor().toString() + ',' + this.commandHandler.getConfig().getDescriptionColor()+ " (" + (context.getDescription() == null ? "Shows more sub commands" : context.getDescription()) + ")";
                	
                	String command = " " + label + description;
                	
                	TextComponent component = new TextComponent(ColorUtil.colorize(command));
        	        
        			component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ColorUtil.colorize(label + description))));
        	        component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, '/' + context.getFullLabel() + (this.parameters != null ? " " + context.getUsageGenerator().generate() : "")));
                	
                	help.add(component);
    			}
    		}
    	}
    	
    	if(print) {
        	if(help.isEmpty()) {
        		sender.sendMessage(this.commandHandler.getConfig().getNoPermissionMessage());
        	} else {
        		sender.sendMessage(this.commandHandler.getConfig().getLineColor() + BukkitUtil.STRAIGHT_LINE_DEFAULT);
        		for(TextComponent component : help) {
        			if(sender instanceof Player) {
        	        	((Player) sender).spigot().sendMessage(component);
        	        } else {
        	        	sender.sendMessage(TextComponent.toLegacyText(component));
        	        }
                }
        		sender.sendMessage(this.commandHandler.getConfig().getLineColor() + BukkitUtil.STRAIGHT_LINE_DEFAULT);
        	}
        } 
    	    	    	
    	return help;
	}

	@Override
	public void registerChildren(CommandContext context) {
		context.setRootContext(this);
		getChildrens().put(context.getName().toLowerCase(), context);
	}

	@Override
	public boolean hasChildren(String name) {
		return getChildrens().containsKey(name.toLowerCase());
	}

	@Override
	public CommandContext getChildren(String name) {
		return getChildrens().get(name.toLowerCase());
	}

	@Override
	public boolean hasChildrens() {
		return getChildrens().size() > 0;
	}

	@Override
	public CommandContext findChildren(ArgumentFormatter formatter) {
		if(formatter.getArguments().size() > 0) {
            String trySub = formatter.getArguments().get(0);
            if(hasChildren(trySub)) {
            	formatter.getArguments().remove(0);
            	
            	CommandContext context = this.getChildren(trySub);
                
                return context.findChildren(formatter);
            }
        }
        
        return this;
	}

	@Override
	public UsageGenerator getUsageGenerator() {
		return new InternalUsageGenerator(getParameters());
	}

	@Override
	public String getUsageForHelpTopic() {
		if (this.method != null && this.parameters != null) {
            return "/" + this.getFullLabel() + " " + getUsageGenerator().generate();
        }
        
        return "";
	}

	@Override
	public CommandExecutor getCommandExecutor() {
		InternalCommandExecutor commandExecutor = new InternalCommandExecutor(this, commandHandler);
		return commandExecutor;
	}
}
