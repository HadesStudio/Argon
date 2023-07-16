package dev.rage.argon.util.command.provider;

import lombok.Getter;

@Getter
public class ProviderData {

	private String name;
	private Class<?> type;
    private String defaultValue;
    private boolean wildcard;
    
	public ProviderData(String name, Class<?> type, String defaultValue, boolean wildcard) {
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
		this.wildcard = wildcard;
	}
	
	public boolean isOptional() {
		return !defaultValue.isEmpty();
	}
	
	public boolean isBooleanType() {
        return type.isInstance(Boolean.class) || type.isInstance(boolean.class);
    }
}
