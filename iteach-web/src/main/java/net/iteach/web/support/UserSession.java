package net.iteach.web.support;

import javax.servlet.http.HttpSession;

import org.joda.time.LocalDate;

public interface UserSession {

	LocalDate getCurrentDate(HttpSession session);

	void setCurrentDate(HttpSession session, LocalDate date);

}
