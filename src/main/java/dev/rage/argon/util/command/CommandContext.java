package dev.rage.argon.util.command;

import dev.rage.argon.util.command.provider.ProviderData;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CommandContext {

	String getName();

	void setName(String name);
	
    Set<String> getAliases();

    String getPermission();
	
    void setPermission(String permission);
    
    boolean hasPermission();
    
    String getDescription();
    
    void setDescription(String description);

    boolean hasDescription();
    
    boolean isAsync();
    
    void setAsync(boolean async);
    
    boolean isHidden();
    
    void setHidden(boolean hidden);
    
    boolean isRoot();
    
    void setRoot(boolean root);
    
    Method getMethod();
    
    void setMethod(Method method);
    
    Class<?> getOwningClass();
    
    Class<?> getSenderClass();
    
    List<ProviderData> getParameters();
    
    void setParameters(List<ProviderData> parameters);
    
    Map<String, CommandContext> getChildrens();
    
    CommandContext getRootContext();
    
    void setRootContext(CommandContext commandContext);
    
	Set<String> getRealAliases();
	
	String getFullLabel();
	
	List<TextComponent> getHelp(CommandSender sender, boolean print);
	
	void registerChildren(CommandContext commandContext);
	
	boolean hasChildren(String name);
	
	CommandContext getChildren(String name);
	
	boolean hasChildrens();
	
	CommandContext findChildren(ArgumentFormatter formatter);
		
	UsageGenerator getUsageGenerator();
	
	String getUsageForHelpTopic();
	
	CommandExecutor getCommandExecutor();
}
