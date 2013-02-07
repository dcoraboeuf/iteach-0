package net.iteach.core.model;

import org.apache.commons.lang3.StringUtils;

import net.sf.jstring.Localizable;
import net.sf.jstring.LocalizableMessage;

public enum PreferenceKey {
	
	PLANNING_MIN_TIME {

		@Override
		public Localizable validate(String value) {
			return validateRange(PREFERENCE_KEY_MIN_TIME, value, 1, 11);
		}
		
	},
	
	PLANNING_MAX_TIME {

		@Override
		public Localizable validate(String value) {
			return validateRange(PREFERENCE_KEY_MAX_TIME, value, 13, 23);
		}
		
	};
	
	private static final String PREFERENCE_KEY_MIN_TIME = "preference.key.min_time";
	private static final String PREFERENCE_KEY_MAX_TIME = "preference.key.max_time";
	private static final String PREFERENCE_VALIDATION_RANGE = "preference.validation.range";
	private static final String PREFERENCE_VALIDATION_INTEGER = "preference.validation.integer";

	public abstract Localizable validate(String value);

	protected Localizable validateRange(String key, String value, int min, int max) {
		if (StringUtils.isNumeric(value)) {
			int n = Integer.parseInt(value, 10);
			if (n >= min && n <= max) {
				return null;
			} else {
				return new LocalizableMessage(
						PREFERENCE_VALIDATION_RANGE,
						new LocalizableMessage(key),
						min, max);
			}
		} else {
			return new LocalizableMessage(
					PREFERENCE_VALIDATION_INTEGER,
					new LocalizableMessage(key));
		}
	}

}
