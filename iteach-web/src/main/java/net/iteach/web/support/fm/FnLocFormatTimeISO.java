package net.iteach.web.support.fm;

import java.util.Locale;

import net.sf.jstring.Strings;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;

public class FnLocFormatTimeISO extends AbstractFnLocFormat<LocalTime> {
	
	@Autowired
	public FnLocFormatTimeISO(Strings strings) {
		super(strings);
	}
	
	@Override
	protected LocalTime parse(String value) {
		return LocalTime.parse(value);
	}
	
	@Override
	protected String format(LocalTime o, Locale locale) {
		return o.toString(DateTimeFormat.forPattern("HH:mm").print(o));
	}

}
