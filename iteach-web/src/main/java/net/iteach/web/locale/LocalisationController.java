package net.iteach.web.locale;

import static org.apache.commons.lang3.StringEscapeUtils.escapeEcmaScript;
import static org.apache.commons.lang3.StringUtils.replace;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jstring.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LocalisationController {
	
	private final Logger logger = LoggerFactory.getLogger(LocalisationController.class);

	private final Strings strings;
	private final LocalisationUtils localisationUtils;

	@Autowired
	public LocalisationController(Strings strings, LocalisationUtils localisationUtils) {
		this.strings = strings;
		this.localisationUtils = localisationUtils;
	}

	@RequestMapping(value = "/localization", method = RequestMethod.GET)
	public void localisation(Locale locale, HttpServletResponse response)
			throws IOException {
		
		// Locale
		if (locale == null) {
			locale = Locale.ENGLISH;
		}
		
		// Restricts the locale
		locale = strings.getSupportedLocales().filterForLookup(locale);
		
		// Last generation time for this locale
		long lastGenerationTime = localisationUtils.getLastGenerationTime(locale);
	
		if (lastGenerationTime >= strings.getLastUpdateTime()) {
			logger.debug("[localization] No need for regeneration (not modified)");
			response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
		} else {
			logger.debug("[localization] Regeneration needed");
			// Gets the list of key/values
			Map<String, String> map = strings.getKeyValues(locale);
			// Output
			StringBuilder js = new StringBuilder();
			js.append("// ").append(new Date()).append("\n");
			js.append("var l = {\n");
			int i = 0;
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (i > 0) {
					js.append(",\n");
				}
				js.append(String.format("'%s': '%s'", key, escape(value)));
				i++;
			}
			js.append("\n};\n");
			// Content
			String content = js.toString();
			// Returns the response as JS
			byte[] bytes = content.getBytes("UTF-8");
			response.setContentType("text/javascript");
			response.setContentLength(bytes.length);
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(bytes);
			outputStream.flush();
			// Regenerated
			localisationUtils.regenerated(locale);
		}
	}

	protected String escape(String value) {
		return escapeEcmaScript(replace(value, "''", "'"));
	}

}
