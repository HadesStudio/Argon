package dev.rage.lootchests.util.config;

import java.util.Map;

import com.avaje.ebean.validation.NotNull;
import com.google.common.collect.Maps;

import lombok.Getter;

@Getter
public class Replacement {
	
    private Map<Object, Object> replacements = Maps.newHashMap();
    
    @NotNull
    private String message;

    public Replacement(String message) {
		this.message = message;
	}
    
    public Replacement add(Object placeholder, Object replacement) {
        replacements.put(placeholder, replacement);
        return this;
    }

    public String toString() {
        for(Object placeholder : replacements.keySet()) {
        	message = message.replace(String.valueOf(placeholder), String.valueOf(replacements.get(placeholder)));
        }
        
        return message;
    }
}