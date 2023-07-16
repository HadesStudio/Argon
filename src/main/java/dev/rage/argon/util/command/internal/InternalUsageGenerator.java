package dev.rage.argon.util.command.internal;

import dev.rage.argon.util.command.UsageGenerator;
import dev.rage.argon.util.command.provider.ProviderData;

import java.util.List;

public class InternalUsageGenerator extends UsageGenerator {

	public InternalUsageGenerator(List<ProviderData> parameters) {
		super(parameters);
	}

	@Override
	public String generate() {
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < parameters.size(); i++) {
			ProviderData parameter = parameters.get(i);
			
            boolean o = parameter.isOptional();
            builder.append(o ? "[" : "<")
                    .append(parameter.getName())
                    .append(parameter.isBooleanType() ? "?" : "")
                    .append(parameter.isWildcard() ? "..." : "")
                    .append(o ? "]" : ">");

            if (i != parameters.size() - 1) {
                builder.append(" ");
            }
        }
		
		return builder.toString();
	}
	
	@Override
	public String generate(String realLabel) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("/" + realLabel).append(generate());
		
		return builder.toString();
	}
}