package net.iteach.web.locale;

import static org.apache.commons.lang3.StringEscapeUtils.escapeEcmaScript;
import static org.apache.commons.lang3.StringUtils.replace;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.iteach.core.model.Ack;
import net.sf.jstring.Strings;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LocalisationController {

	private final Strings strings;

	@Autowired
	public LocalisationController(Strings strings) {
		this.strings = strings;
	}

	@RequestMapping(value = "/language/{language}", method = RequestMethod.POST)
	public @ResponseBody Ack setLanguage(@PathVariable String language) {
		if (StringUtils.isNotBlank(language)) {
			// Sets the locale
			Locale locale = new Locale(language);
			// FIXME Checks if the language is recognized (needs jstring 2.2)
			// Changes the locale
			LocaleContextHolder.setLocale(locale, true);
			// OK
			return Ack.OK;
		} else {
			return Ack.NOK;
		}
	}

	@RequestMapping(value = "/localization", method = RequestMethod.GET)
	public void localisation(Locale locale, HttpServletResponse response)
			throws IOException {
		// Locale
		if (locale == null) {
			locale = Locale.ENGLISH;
		}
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
		// Returns the response as JS
		byte[] bytes = js.toString().getBytes("UTF-8");
		response.setContentType("text/javascript");
		response.setContentLength(bytes.length);
		ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write(bytes);
		outputStream.flush();
		// TODO Cache management
	}

	protected String escape(String value) {
		return escapeEcmaScript(replace(value, "''", "'"));
	}

}
