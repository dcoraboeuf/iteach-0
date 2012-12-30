package net.iteach.web.support;

import javax.servlet.http.HttpSession;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserSession implements UserSession {

	private static final String CURRENT_DATE = "currentDate";

	@Override
	public LocalDate getCurrentDate(HttpSession session) {
		LocalDate date = (LocalDate) session.getAttribute(CURRENT_DATE);
		if (date != null) {
			return date;
		} else {
			date = new LocalDate();
			session.setAttribute(CURRENT_DATE, date);
			return date;
		}
	}
	
	@Override
	public void setCurrentDate(HttpSession session, LocalDate date) {
		session.setAttribute(CURRENT_DATE, date);
	}

}
