package net.iteach.web.locale;

import lombok.Data;
import net.sf.jstring.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.StringEscapeUtils.escapeEcmaScript;
import static org.apache.commons.lang3.StringUtils.replace;

@Controller
public class LocalisationController {
	
	@Data
	private static class CachedContent {
		
		private final long timestamp;
		private final String content;
		
	}
	
	private final Logger logger = LoggerFactory.getLogger(LocalisationController.class);

	private final Strings strings;
	
	private final Map<Locale, CachedContent> contents = new ConcurrentHashMap<>();

	@Autowired
	public LocalisationController(Strings strings) {
		this.strings = strings;
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
		// Cache ?
		String content;
		CachedContent cachedContent = contents.get(locale);
		if (cachedContent != null && cachedContent.getTimestamp() >= strings.getLastUpdateTime()) {
			logger.debug("[localization] Reusing the content for locale {}", locale);
			content = cachedContent.getContent();
		} else {
			logger.info("[localization] Regeneraring the content for locale {}", locale);
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
			content = js.toString();
			// Cache entry
			contents.put(locale, new CachedContent(System.currentTimeMillis(), content));
		}
		// Returns the response as JS
		byte[] bytes = content.getBytes("UTF-8");
		response.setContentType("text/javascript");
		response.setContentLength(bytes.length);
		ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write(bytes);
		outputStream.flush();
	}

	protected String escape(String value) {
		return escapeEcmaScript(replace(value, "''", "'"));
	}

}
