package net.iteach.web.locale;

import net.sf.jstring.Strings;
import net.sf.jstring.SupportedLocales;
import net.sf.jstring.support.DefaultSupportedLocales;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocaleInterceptorTest {

    private LocaleInterceptor interceptor;

    @Before
    public void before() {
        SupportedLocales supportedLocales = new DefaultSupportedLocales(Locale.ENGLISH, Locale.FRENCH);
        Strings strings = mock(Strings.class);
        when(strings.getSupportedLocales()).thenReturn(supportedLocales);
        interceptor = new LocaleInterceptor(strings);
    }

    @Test
    public void locale_null() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        LocaleResolver localeResolver = mock(LocaleResolver.class);
        when(localeResolver.resolveLocale(request)).thenReturn(null);
        assertEquals(Locale.ENGLISH, interceptor.getLocale(request, localeResolver));
    }

    @Test
    public void locale_default() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        LocaleResolver localeResolver = mock(LocaleResolver.class);
        when(localeResolver.resolveLocale(request)).thenReturn(Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, interceptor.getLocale(request, localeResolver));
    }

    @Test
    public void locale_supported() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        LocaleResolver localeResolver = mock(LocaleResolver.class);
        when(localeResolver.resolveLocale(request)).thenReturn(Locale.FRENCH);
        assertEquals(Locale.FRENCH, interceptor.getLocale(request, localeResolver));
    }

    @Test
    public void locale_extended() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        LocaleResolver localeResolver = mock(LocaleResolver.class);
        when(localeResolver.resolveLocale(request)).thenReturn(Locale.CANADA_FRENCH);
        assertEquals(Locale.FRENCH, interceptor.getLocale(request, localeResolver));
    }

    @Test
    public void locale_not_supported() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        LocaleResolver localeResolver = mock(LocaleResolver.class);
        when(localeResolver.resolveLocale(request)).thenReturn(Locale.ITALIAN);
        assertEquals(Locale.ENGLISH, interceptor.getLocale(request, localeResolver));
    }

}
