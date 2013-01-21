package net.iteach.core.report;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.YearMonth;

import lombok.Data;

@Data
public class MonthlyReport {
	
	private final YearMonth yearMonth;
	private final List<SchoolMonthlyHours> schools;
	
	public BigDecimal getMonthlyHours () {
		BigDecimal hours = BigDecimal.ZERO;
		for (SchoolMonthlyHours school: schools) {
			hours = hours.add(school.getMonthlyHours());
		}
		return hours;
	}
	
	public BigDecimal getTotalHours () {
		BigDecimal hours = BigDecimal.ZERO;
		for (SchoolMonthlyHours school: schools) {
			hours = hours.add(school.getTotalHours());
		}
		return hours;
	}

}
