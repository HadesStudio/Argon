package dev.rage.lootchests.util.command.processor.impl;

import com.google.common.collect.Lists;
import dev.rage.lootchests.util.command.ArgumentFormatter;
import dev.rage.lootchests.util.command.processor.Processor;

import java.util.List;

public class ArgumentFormatterProcessor implements Processor<String[], ArgumentFormatter> {

	@Override
	public ArgumentFormatter process(String[] strings) {
		List<String> arguments = Lists.newArrayList();
		
		for (String argument : strings) {
            if (!argument.isEmpty()) {
            	arguments.add(argument);
            }
        }
		
		return new ArgumentFormatter(arguments);
	}

}
