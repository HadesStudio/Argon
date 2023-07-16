package dev.rage.argon.util.config.serialize;

public interface ConfigSerializer<T> {

	String toString(T object);
	
	T fromString(String source);
}