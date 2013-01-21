package net.iteach.web.report;

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
	public String monthlyReport(Model model, HttpSession session) {
		// Gets the current date
		LocalDate date = userSession.getCurrentDate(session);
		// Gets the monthly report...
		MonthlyReport report = reportService.getMonthlyReport(date);
		// ...and puts it into the model
		model.addAttribute("report", report);
		// OK
		return "report/monthly";
	}

}
