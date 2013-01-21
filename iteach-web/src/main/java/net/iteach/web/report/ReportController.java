package net.iteach.web.report;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import net.iteach.api.report.ReportService;
import net.iteach.core.report.MonthlyReport;
import net.iteach.web.support.AbstractGUIController;
import net.iteach.web.support.ErrorHandler;
import net.iteach.web.support.UserSession;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gui/report")
public class ReportController extends AbstractGUIController {

	private final UserSession userSession;
	private final ReportService reportService;

	@Autowired
	public ReportController(ErrorHandler errorHandler, UserSession userSession, ReportService reportService) {
		super(errorHandler);
		this.userSession = userSession;
		this.reportService = reportService;
	}

	@RequestMapping(value = "/monthly", method = RequestMethod.GET)
	public String monthlyReport(Locale locale, Model model, HttpSession session) {
		// Gets the current date
		LocalDate date = userSession.getCurrentDate(session);
		// Gets the monthly report...
		MonthlyReport report = reportService.getMonthlyReport(date);
		// ...and puts it into the model
		model.addAttribute("report", report);
		// Display information
		model.addAttribute("yearMonth",
			String.format("%s %s",
					report.getYearMonth().year().getAsText(locale),
					report.getYearMonth().monthOfYear().getAsText(locale)));
		// OK
		return "report/monthly";
	}

	@RequestMapping(value = "/monthly/previous", method = RequestMethod.GET)
	public String monthlyReportPrevious(Locale locale, Model model, HttpSession session) {
		return monthlyReportDelta(locale, model, session, -1);
	}

	@RequestMapping(value = "/monthly/next", method = RequestMethod.GET)
	public String monthlyReportNext(Locale locale, Model model, HttpSession session) {
		return monthlyReportDelta(locale, model, session, 1);
	}

	protected String monthlyReportDelta(Locale locale, Model model, HttpSession session, int delta) {
		deltaMonth(session, delta);
		// OK
		return monthlyReport(locale, model, session);
	}

	protected void deltaMonth(HttpSession session, int delta) {
		// Gets the current date
		LocalDate date = userSession.getCurrentDate(session);
		// Resets to first day of the month
		date = date.withDayOfMonth(1);
		// Month + delta
		date = date.plusMonths(delta);
		// Sets back into the session
		userSession.setCurrentDate(session, date);
	}

}
