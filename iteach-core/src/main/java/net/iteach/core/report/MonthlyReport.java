package net.iteach.core.report;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

import org.joda.money.Money;
import org.joda.time.YearMonth;

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
	
	public Money getMonthlyAmount () {
		Money amount = null;
		boolean ok = true;
		for (SchoolMonthlyHours school : schools) {
			if (ok) {
				Money totalAmount = school.getMonthlyAmount();
				if (amount == null) {
					amount = totalAmount;
				} else if (amount.getCurrencyUnit().equals(totalAmount.getCurrencyUnit())) {
					amount = amount.plus(totalAmount.getAmount());
				} else {
					ok = false;
				}
			}
		}
		return ok ? amount : null;
	}
	
	public Money getTotalAmount () {
		Money amount = null;
		boolean ok = true;
		for (SchoolMonthlyHours school : schools) {
			if (ok) {
				Money schoolAmount = school.getTotalAmount();
				if (amount == null) {
					amount = schoolAmount;
				} else if (amount.getCurrencyUnit().equals(schoolAmount.getCurrencyUnit())) {
					amount = amount.plus(schoolAmount.getAmount());
				} else {
					ok = false;
				}
			}
		}
		return ok ? amount : null;
	}

}
