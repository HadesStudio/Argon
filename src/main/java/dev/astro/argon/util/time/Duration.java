package dev.astro.argon.util.time;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Duration {

	private final long value;

	public static Duration fromString(String source) {
		if (source.equalsIgnoreCase("oo") || source.equalsIgnoreCase("infinity") || source.equalsIgnoreCase("infinite") || source.equalsIgnoreCase("permanent") || source.equalsIgnoreCase("perm") || source.equalsIgnoreCase("forever")) {
			return new Duration(Integer.MAX_VALUE);
		}

		long totalTime = 0L;
		boolean found = false;
		Matcher matcher = Pattern.compile("\\d+\\D+").matcher(source);

		while (matcher.find()) {
			String s = matcher.group();
			Long value = Long.parseLong(s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0]);
			String type = s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1];

			switch (type) {
				case "s":
					totalTime += value;
					found = true;
					break;
				case "m":
					totalTime += value * 60;
					found = true;
					break;
				case "h":
					totalTime += value * 60 * 60;
					found = true;
					break;
				case "d":
					totalTime += value * 60 * 60 * 24;
					found = true;
					break;
				case "w":
					totalTime += value * 60 * 60 * 24 * 7;
					found = true;
					break;
				case "M":
					totalTime += value * 60 * 60 * 24 * 30;
					found = true;
					break;
				case "y":
					totalTime += value * 60 * 60 * 24 * 365;
					found = true;
					break;
			}
		}

		return new Duration(!found ? -1 : totalTime * 1000);
	}

}