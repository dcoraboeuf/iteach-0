package net.iteach.core.report;

import java.util.List;

import org.joda.time.YearMonth;

import lombok.Data;

@Data
public class MonthlyReport {
	
	private final YearMonth yearMonth;
	private final List<SchoolHours> schools;

}
