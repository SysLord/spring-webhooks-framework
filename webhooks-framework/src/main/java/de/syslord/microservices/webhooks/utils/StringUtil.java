package de.syslord.microservices.webhooks.utils;

public class StringUtil {

	public static boolean isEmptyTrimmed(String string) {
		return string == null || string.trim().isEmpty();
	}

}
