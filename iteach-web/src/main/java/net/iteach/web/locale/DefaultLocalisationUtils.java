package net.iteach.web.locale;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class DefaultLocalisationUtils implements LocalisationUtils {
	
	private final Map<Locale, Long> generations = new ConcurrentHashMap<>();

	@Override
	public long getLastGenerationTime(Locale locale) {
		Long time = generations.get(locale);
		return time != null ? time.longValue() : -1L;
	}

	@Override
	public void regenerated(Locale locale) {
		generations.put(locale, System.currentTimeMillis());
	}

}
