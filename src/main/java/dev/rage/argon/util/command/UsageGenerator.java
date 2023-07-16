package dev.rage.argon.util.command;

import dev.rage.argon.util.command.provider.ProviderData;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public abstract class UsageGenerator {

	protected List<ProviderData> parameters;
	
	public abstract String generate();
	
	public abstract String generate(String realLabel);
}