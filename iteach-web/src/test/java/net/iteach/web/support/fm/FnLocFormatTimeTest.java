package net.iteach.web.support.fm;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Locale;

import net.sf.jstring.Strings;
import net.sf.jstring.SupportedLocales;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.i18n.LocaleContextHolder;

import freemarker.template.TemplateModelException;

public class FnLocFormatTimeTest  {
	
	private FnLocFormatTime format;
	
	@Before
	public void before () {
		
		SupportedLocales supportedLocales = mock (SupportedLocales.class);
		when(supportedLocales.filterForLookup(any(Locale.class))).thenAnswer(new Answer<Locale>() {

			@Override
			public Locale answer(InvocationOnMock invocation) throws Throwable {
				return (Locale) invocation.getArguments()[0];
			}
		});
		
		Strings strings = mock(Strings.class);
		when(strings.getSupportedLocales()).thenReturn(supportedLocales);
		
		format = new FnLocFormatTime(strings);
	}
	
	@Test
	public void test_en () throws TemplateModelException {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		assertEquals ("3:05 PM", format());
	}
	
	@Test
	public void test_fr () throws TemplateModelException {
		LocaleContextHolder.setLocale(Locale.FRENCH);
		assertEquals ("15:05", format());
	}
	
	@Test
	public void test_de () throws TemplateModelException {
		LocaleContextHolder.setLocale(Locale.GERMAN);
		assertEquals ("15:05", format());
	}

	protected String format() throws TemplateModelException {
		return (String) format.exec(Collections.singletonList("15:05"));
	}

}
