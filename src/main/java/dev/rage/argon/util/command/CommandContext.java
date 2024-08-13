package dev.rage.argon.util.command;

import dev.rage.argon.util.command.data.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@RequiredArgsConstructor
public abstract class CommandContext {
	
	private final CommandHandler commandHandler;
    private final Class<?> senderClass;

	protected Method method;
    protected Object object;
    
    private String label = "";
    private List<String> aliases = new ArrayList<>();
    private String permission;
    private String description = "No description!";
    private boolean async;
    private boolean hidden;

    private CommandContext parent;
    
    protected List<CommandContext> childs = new ArrayList<>();
    protected List<Data> parameters = new ArrayList<>();
    protected List<String> flags = new ArrayList<>();

    public void registerChild(CommandContext child) {
    	child.setParent(this);
        childs.add(child);
    }
    
    public CommandContext getChild(String name) {
    	for (CommandContext child : childs) {
            if (child.getLabel().equalsIgnoreCase(name)
                    || child.getAliases().contains(name))
                return child;
        }

        return null;
    }
    
    public CommandContext findContext(List<String> args) {
    	if (args.size() > 0) {
            String s = args.get(0);
            if (getChild(s) != null) {
                args.remove(s);
                return getChild(s).findContext(args);
            }
        }
        return this;
    }
    
    public boolean canUse(CommandSender sender) {
        if (permission != null && !permission.isEmpty()) {
            switch (permission.toLowerCase()) {
                case "console":
                    return (sender instanceof ConsoleCommandSender);
                case "op":
                    return sender.isOp();
                default:
                    return sender.hasPermission(permission);
            }
        }

        return true;
    }
    
    public abstract String getUsage(String realLabel);
    
    public abstract boolean execute(CommandSender sender, List<String> args, List<String> flags);
    
    public String getFullLabel() {
        return parent != null ? parent.getFullLabel() + " " + label : label;
    }
}
