package dev.rage.argon.util.command.processor.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import dev.rage.argon.util.command.CommandContext;
import dev.rage.argon.util.command.CommandHandler;
import dev.rage.argon.util.command.annotation.Command;
import dev.rage.argon.util.command.annotation.Param;
import dev.rage.argon.util.command.internal.InternalCommandContext;
import dev.rage.argon.util.command.processor.Processor;
import dev.rage.argon.util.command.provider.ProviderData;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class MethodProcessor implements Processor<Method, Set<CommandContext>> {

	private CommandHandler commandHandler;
	
	@Override
	public Set<CommandContext> process(Method method) {
		if(method.isAnnotationPresent(Command.class) && method.getParameterCount() >= 1 && CommandSender.class.isAssignableFrom(method.getParameterTypes()[0])) {
            Command command = method.getAnnotation(Command.class);
            Class<?> owningClass = method.getDeclaringClass();
            List<ProviderData> parameters = Lists.newArrayList();
            
            if(method.getParameterCount() > 1) {
                for(int i = 1; i < method.getParameterCount(); ++i) {
                    Parameter parameter = method.getParameters()[i];
                    
                    if(parameter.isAnnotationPresent(Param.class)) {
                        Param param = parameter.getAnnotation(Param.class);
                        ProviderData paramData = new ProviderData(param.name(), parameter.getType(), param.defaultValue(), param.wildcard());
                        parameters.add(paramData);
                    }
                }
            }
            
            Set<CommandContext> registered = Sets.newHashSet();
            for(int index = 0; index < command.names().length; index++) {
            	String name = command.names()[index];
                boolean change = true;
                boolean hadChild = false;
                name = name.toLowerCase().trim();
                
                String[] cmdNames;
                if (name.contains(" ")) {
                    cmdNames = name.split(" ");
                } else {
                    cmdNames = new String[] { name };
                }
                
                String primary = cmdNames[0];
                CommandContext workingContext = new InternalCommandContext(method.getParameterTypes()[0], owningClass, commandHandler);
                if(commandHandler.getMainContext().hasChildren(primary)) {
                    workingContext = commandHandler.getMainContext().getChildren(primary);
                    change = false;
                }
                
                if(change) {
                    workingContext.setName(cmdNames[0]);
                } else {
                    workingContext.getAliases().add(cmdNames[0]);
                }
                
                CommandContext parentContext = new InternalCommandContext(method.getParameterTypes()[0], owningClass, commandHandler);
                if(workingContext.hasChildren(cmdNames[0])) {
                    parentContext = (InternalCommandContext) workingContext.getChildren(cmdNames[0]);
                } else {
                    parentContext.setName(cmdNames[0]);
                    parentContext.setPermission("");
                }
                
                if(cmdNames.length > 1) {
                    hadChild = true;
                    workingContext.registerChildren(parentContext);
                    CommandContext childContext = new InternalCommandContext(method.getParameterTypes()[0], owningClass, commandHandler);
                    
                    for (int j = 1; j < cmdNames.length; ++j) {
                        String subName = cmdNames[j];
                        childContext.setName(subName);
                                                
                        if(index == 1 && j == 1 && command.names().length > 1) {
                        	childContext.setRoot(false);
                    	} else {
                    		childContext.setRoot(true);
                    	}
                        
                        if(parentContext.hasChildren(subName)) {
                            childContext = parentContext.getChildren(subName);
                        }
                        
                        parentContext.registerChildren(childContext);
                        if(j == cmdNames.length - 1) {
                            childContext.setMethod(method);
                            childContext.setAsync(command.async());
                            childContext.setHidden(command.hidden());
                            childContext.setPermission(command.permission());
                            childContext.setDescription(command.description());
                            childContext.setParameters(parameters);
                        } else {
                            parentContext = childContext;
                            childContext = new InternalCommandContext(method.getParameterTypes()[0], owningClass, commandHandler);
                        }
                    }
                }
                
                if(!hadChild) {
                    parentContext.setMethod(method);
                    parentContext.setAsync(command.async());
                    parentContext.setHidden(command.hidden());
                    parentContext.setPermission(command.permission());
                    parentContext.setDescription(command.description());
                    parentContext.setParameters(parameters);
                    workingContext.registerChildren(parentContext);
                }
                
                commandHandler.getMainContext().registerChildren(workingContext);
                registered.add(workingContext);
            }
            
            return registered;
        }
		return null;
	}

}