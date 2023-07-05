package dev.rage.lootchests.util.command;

import dev.rage.lootchests.util.command.provider.ProviderData;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public abstract class UsageGenerator {

	protected List<ProviderData> parameters;
	
	public abstract String generate();
	
	public abstract String generate(String realLabel);
}