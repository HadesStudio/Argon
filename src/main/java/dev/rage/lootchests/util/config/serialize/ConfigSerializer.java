package dev.rage.lootchests.util.config.serialize;

public interface ConfigSerializer<T> {

	String toString(T object);
	
	T fromString(String source);
}