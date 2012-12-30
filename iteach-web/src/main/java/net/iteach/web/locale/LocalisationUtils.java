package net.iteach.web.locale;

import java.util.Locale;

public interface LocalisationUtils {

	long getLastGenerationTime(Locale locale);

	void regenerated(Locale locale);

}
