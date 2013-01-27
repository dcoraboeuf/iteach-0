package net.iteach.core.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.joda.money.Money;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SchoolMonthlyHours {

	private final int id;
	private final String name;
	private final String color;
	private final Money hourlyRate;
	private final Map<Integer, StudentMonthlyHours> students;
	// private final Money amount;

	public SchoolMonthlyHours(int id, String name, String color, Money hourlyRate) {
		this(id, name, color, hourlyRate, Collections.<Integer, StudentMonthlyHours> emptyMap());
	}

	public SchoolMonthlyHours addStudent(StudentMonthlyHours studentHours) {
		Map<Integer, StudentMonthlyHours> map = new LinkedHashMap<>(students);
		map.put(studentHours.getId(), studentHours);
		return new SchoolMonthlyHours(id, name, color, hourlyRate, map);
	}
	
	public BigDecimal getMonthlyHours () {
		BigDecimal hours = BigDecimal.ZERO;
		for (StudentMonthlyHours studentHours: students.values()) {
			hours = hours.add(studentHours.getMonthlyHours());
		}
		return hours;
	}
	
	public BigDecimal getTotalHours () {
		BigDecimal hours = BigDecimal.ZERO;
		for (StudentMonthlyHours studentHours: students.values()) {
			hours = hours.add(studentHours.getTotalHours());
		}
		return hours;
	}
	
	public Money getMonthlyAmount () {
		return hourlyRate.multipliedBy(getMonthlyHours(), RoundingMode.HALF_UP);
	}
	
	public Money getTotalAmount () {
		return hourlyRate.multipliedBy(getTotalHours(), RoundingMode.HALF_UP);
	}

}
