package dev.rage.argon.util.command.provider.defaults.enums;

import dev.rage.argon.util.command.provider.Provider;
import dev.rage.argon.util.command.provider.exception.ProviderException;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class EnumProvider<E extends Enum<E>> implements Provider<E> {
	
    private final Class<E> type;
    
	@Override
	public E provide(CommandSender sender, String source) {
        for (E enumConstant : type.getEnumConstants()) {
            if (enumConstant.name().equalsIgnoreCase(source)) {
                return enumConstant;
            }
        }

        throw new ProviderException("No value named '<source>' found in the '<white>" + type.getSimpleName().toLowerCase() + "<red>' list.", source);
	}
	
	@Override
	public List<String> suggest(CommandSender sender, List<String> flags) {
		List<String> completions = new ArrayList<>();
		
    	for (E enumConstant : type.getEnumConstants()) {
    		completions.add(enumConstant.name().toLowerCase());
    	}
    	
    	return completions;
	}
}
