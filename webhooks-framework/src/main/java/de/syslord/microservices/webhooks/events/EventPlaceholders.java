package de.syslord.microservices.webhooks.events;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.google.common.collect.Maps;

import de.syslord.microservices.webhooks.utils.JsonNoGetterAutodetect;

@JsonNoGetterAutodetect
public class EventPlaceholders {

	private Map<String, String> replacements = Maps.newHashMap();

	@JsonAnyGetter
	public Map<String, String> getReplacements() {
		return new HashMap<>(replacements);
	}

	public void add(String param, String value) {
		replacements.put(param, value);
	}

	public String apply(String remoteAdress) {
		if (remoteAdress == null) {
			return "";
		}

		String temp = remoteAdress;
		for (Entry<String, String> patternReplacement : replacements.entrySet()) {
			String pattern = patternReplacement.getKey();
			String replacement = patternReplacement.getValue();

			String regexPattern = Pattern.quote(pattern);
			temp = temp.replaceAll(regexPattern, replacement);
		}
		return temp;
	}

}
