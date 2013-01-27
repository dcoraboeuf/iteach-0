package net.iteach.core.report;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.joda.money.Money;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentMonthlyHours {

	private final int id;
	private final String name;
	private final Money hourlyRate;
	private final BigDecimal monthlyHours;
	private final BigDecimal totalHours;

	public StudentMonthlyHours(int id, String name, Money hourlyRate) {
		this(id, name, hourlyRate, BigDecimal.ZERO, BigDecimal.ZERO);
	}

	public StudentMonthlyHours addHours(BigDecimal hours, boolean isMonthlyHours) {
		return new StudentMonthlyHours(id, name, hourlyRate,
			isMonthlyHours? monthlyHours.add(hours) : monthlyHours,
			totalHours.add(hours));
	}
	
	public Money getMonthlyAmount () {
		return hourlyRate.multipliedBy(getMonthlyHours(), RoundingMode.HALF_UP);
	}
	
	public Money getTotalAmount () {
		return hourlyRate.multipliedBy(getTotalHours(), RoundingMode.HALF_UP);
	}

}
