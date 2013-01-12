package net.iteach.web.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.iteach.core.security.SecurityUtils;
import net.iteach.web.support.DefaultErrorHandler;
import net.sf.jstring.Strings;
import net.sf.jstring.support.CoreException;
import net.sf.jstring.support.StringsLoader;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class DefaultErrorHandlerTest {
	
	private Strings strings;
	private SecurityUtils securityUtils;
	private DefaultErrorHandler handler;

	@Before
	public void before() {
		strings = StringsLoader.auto(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN);
		securityUtils = mock(SecurityUtils.class);
		handler = new DefaultErrorHandler(strings, securityUtils);
	}
	
	@Test
	public void canHandleAccessDenied() {
		when(securityUtils.isLogged()).thenReturn(true);
		assertTrue(handler.canHandleAccessDenied());
	}
	
	@Test
	public void handleError_core_exception_fr() {
		MyCoreException ex = new MyCoreException(10);
		HttpServletRequest request = new MockHttpServletRequest();
		ErrorMessage error = handler.handleError(request, Locale.FRENCH, ex);
		assertNotNull(error);
		assertNotNull(error.getUuid());
		assertEquals("Erreur avec 10.", error.getMessage());
	}
	
	@Test
	public void handleError_core_exception_en() {
		MyCoreException ex = new MyCoreException(10);
		HttpServletRequest request = new MockHttpServletRequest();
		ErrorMessage error = handler.handleError(request, Locale.ENGLISH, ex);
		assertNotNull(error);
		assertNotNull(error.getUuid());
		assertEquals("Error with 10.", error.getMessage());
	}
	
	@Test
	public void handleError_core_exception_other() {
		MyCoreException ex = new MyCoreException(10);
		HttpServletRequest request = new MockHttpServletRequest();
		ErrorMessage error = handler.handleError(request, Locale.ITALIAN, ex);
		assertNotNull(error);
		assertNotNull(error.getUuid());
		assertEquals("Error with 10.", error.getMessage());
	}
	
	@Test
	public void handleError_other_exception_defined() {
		SQLException ex = new SQLException();
		HttpServletRequest request = new MockHttpServletRequest();
		ErrorMessage error = handler.handleError(request, Locale.FRENCH, ex);
		assertNotNull(error);
		assertNotNull(error.getUuid());
		assertEquals("Erreur d'accès à la base de données", error.getMessage());
	}
	
	@Test
	public void handleError_other_exception_not_defined() {
		NullPointerException ex = new NullPointerException();
		HttpServletRequest request = new MockHttpServletRequest();
		ErrorMessage error = handler.handleError(request, Locale.FRENCH, ex);
		assertNotNull(error);
		assertNotNull(error.getUuid());
		assertEquals("Erreur technique", error.getMessage());
	}
	
	@Test
	public void displayableError() {
		CoreException ex = mock(CoreException.class);
		when(ex.getLocalizedMessage(strings, Locale.FRENCH)).thenReturn("Mon message");
		String message = handler.displayableError(ex, Locale.FRENCH);
		assertEquals("Mon message", message);
	}

}
