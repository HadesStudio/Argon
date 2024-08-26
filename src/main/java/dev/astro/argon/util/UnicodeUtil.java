package dev.astro.argon.util;

import org.apache.commons.lang.StringEscapeUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UnicodeUtil {

	public final String MINI_STAR;
	public final String NORMAL_STAR;
	
	public final String ORIGIN_STAR;
	public final String STAR_CUTE;
	
	public final String VERTICAL_BAR;
	public final String CAUTION;
	public final String ARROW_LEFT;
	public final String ARROW_RIGHT;
	public final String ARROWS_LEFT;
	public final String ARROWS_RIGHT;
	public final String HEART;
	public final String COIN;
	
	public final String INCORRECT;
    public final String CORRECT;
    public final String SWORDS;
	
	public final String ICE_STAR;
	
	public final char SEPARATOR;
	
	static {
		MINI_STAR = StringEscapeUtils.unescapeJava("\u22c6");
		NORMAL_STAR = StringEscapeUtils.unescapeJava("\u2605");
		
		ORIGIN_STAR = StringEscapeUtils.unescapeJava("\u2726");
		STAR_CUTE = StringEscapeUtils.unescapeJava("\u2735");
		
		VERTICAL_BAR = StringEscapeUtils.unescapeJava("\u2503");
		CAUTION = StringEscapeUtils.unescapeJava("\u26a0");
		ARROW_LEFT = StringEscapeUtils.unescapeJava("\u25C0");
		ARROW_RIGHT = StringEscapeUtils.unescapeJava("\u25B6");
		ARROWS_LEFT = StringEscapeUtils.unescapeJava("\u00AB");
		ARROWS_RIGHT = StringEscapeUtils.unescapeJava("\u00BB");
		HEART = StringEscapeUtils.unescapeJava("\u2764");
		COIN = StringEscapeUtils.unescapeJava("\u26c3");
		
		INCORRECT = StringEscapeUtils.unescapeJava("\u2716");
		CORRECT = StringEscapeUtils.unescapeJava("\u2714");
		SWORDS = StringEscapeUtils.unescapeJava("\u2694");
		
		ICE_STAR = StringEscapeUtils.unescapeJava("\u2746");
		
		SEPARATOR = '\n';
	}
}
