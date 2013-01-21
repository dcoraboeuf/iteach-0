package net.iteach.core.report;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentMonthlyHours {

	private final int id;
	private final String name;
	private final BigDecimal monthlyHours;
	private final BigDecimal totalHours;

	public StudentMonthlyHours(int id, String name) {
		this(id, name, BigDecimal.ZERO, BigDecimal.ZERO);
	}

	public StudentMonthlyHours addHours(BigDecimal hours, boolean isMonthlyHours) {
		return new StudentMonthlyHours(id, name,
			isMonthlyHours? monthlyHours.add(hours) : monthlyHours,
			totalHours.add(hours));
	}

}
