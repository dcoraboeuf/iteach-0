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

public class FnLocFormatDateTest  {
	
	private FnLocFormatDate format;
	
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
		
		format = new FnLocFormatDate(strings);
	}
	
	@Test
	public void test_en () throws TemplateModelException {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		assertEquals ("Friday, February 8, 2013", format());
	}
	
	@Test
	public void test_fr () throws TemplateModelException {
		LocaleContextHolder.setLocale(Locale.FRENCH);
		assertEquals ("vendredi 8 février 2013", format());
	}
	
	@Test
	public void test_de () throws TemplateModelException {
		LocaleContextHolder.setLocale(Locale.GERMAN);
		assertEquals ("Freitag, 8. Februar 2013", format());
	}

	protected String format() throws TemplateModelException {
		return (String) format.exec(Collections.singletonList("2013-02-08"));
	}

}
