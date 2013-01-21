package net.iteach.api.report;

import net.iteach.core.report.MonthlyReport;

import org.joda.time.LocalDate;

public interface ReportService {

	MonthlyReport getMonthlyReport(LocalDate date);

}
