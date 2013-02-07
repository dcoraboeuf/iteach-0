package net.iteach.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Locale;

import net.sf.jstring.Localizable;
import net.sf.jstring.Strings;
import net.sf.jstring.support.StringsLoader;

import org.junit.Test;

public class PreferenceKeyTest {
	
	private static final Strings strings = StringsLoader.auto(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN);
	
	@Test
	public void min_time_valid_1() {
		assertNull(PreferenceKey.PLANNING_MIN_TIME.validate("1"));
	}
	
	@Test
	public void min_time_valid_6() {
		assertNull(PreferenceKey.PLANNING_MIN_TIME.validate("6"));
	}
	
	@Test
	public void min_time_valid_11() {
		assertNull(PreferenceKey.PLANNING_MIN_TIME.validate("11"));
	}
	
	@Test
	public void min_time_invalid_0() {
		Localizable message = PreferenceKey.PLANNING_MIN_TIME.validate("0");
		assertNotNull(message);
		assertEquals("L'heure minimale pour le planning doit être entre 1 et 11.", message.getLocalizedMessage(strings, Locale.FRENCH));
	}
	
	@Test
	public void min_time_invalid_12() {
		Localizable message = PreferenceKey.PLANNING_MIN_TIME.validate("12");
		assertNotNull(message);
		assertEquals("L'heure minimale pour le planning doit être entre 1 et 11.", message.getLocalizedMessage(strings, Locale.FRENCH));
	}
	
	@Test
	public void min_time_invalid_mm() {
		Localizable message = PreferenceKey.PLANNING_MIN_TIME.validate("mm");
		assertNotNull(message);
		assertEquals("L'heure minimale pour le planning doit être un entier.", message.getLocalizedMessage(strings, Locale.FRENCH));
	}
	
	@Test
	public void min_time_invalid_double() {
		Localizable message = PreferenceKey.PLANNING_MIN_TIME.validate("10.5");
		assertNotNull(message);
		assertEquals("L'heure minimale pour le planning doit être un entier.", message.getLocalizedMessage(strings, Locale.FRENCH));
	}
	
	@Test
	public void max_time_valid_1() {
		assertNull(PreferenceKey.PLANNING_MAX_TIME.validate("13"));
	}
	
	@Test
	public void max_time_valid_6() {
		assertNull(PreferenceKey.PLANNING_MAX_TIME.validate("20"));
	}
	
	@Test
	public void max_time_valid_11() {
		assertNull(PreferenceKey.PLANNING_MAX_TIME.validate("23"));
	}
	
	@Test
	public void max_time_invalid_12() {
		Localizable message = PreferenceKey.PLANNING_MAX_TIME.validate("12");
		assertNotNull(message);
		assertEquals("L'heure maximale pour le planning doit être entre 13 et 23.", message.getLocalizedMessage(strings, Locale.FRENCH));
	}
	
	@Test
	public void max_time_invalid_24() {
		Localizable message = PreferenceKey.PLANNING_MAX_TIME.validate("24");
		assertNotNull(message);
		assertEquals("L'heure maximale pour le planning doit être entre 13 et 23.", message.getLocalizedMessage(strings, Locale.FRENCH));
	}
	
	@Test
	public void max_time_invalid_mm() {
		Localizable message = PreferenceKey.PLANNING_MAX_TIME.validate("mm");
		assertNotNull(message);
		assertEquals("L'heure maximale pour le planning doit être un entier.", message.getLocalizedMessage(strings, Locale.FRENCH));
	}
	
	@Test
	public void max_time_invalid_double() {
		Localizable message = PreferenceKey.PLANNING_MAX_TIME.validate("14.5");
		assertNotNull(message);
		assertEquals("L'heure maximale pour le planning doit être un entier.", message.getLocalizedMessage(strings, Locale.FRENCH));
	}

}
