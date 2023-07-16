package dev.rage.argon.util.command.processor;

public interface Processor<T, R> {

	R process(T value);
}