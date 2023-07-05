package dev.rage.lootchests.util.command.processor;

public interface Processor<T, R> {

	R process(T value);
}