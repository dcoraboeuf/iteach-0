package net.iteach.core.report;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SchoolMonthlyHours {

	private final int id;
	private final String name;
	private final String color;
	private final Map<Integer, StudentMonthlyHours> students;
	// private final Money amount;

	public SchoolMonthlyHours(int id, String name, String color) {
		this(id, name, color, Collections.<Integer, StudentMonthlyHours> emptyMap());
	}

	public SchoolMonthlyHours addStudent(StudentMonthlyHours studentHours) {
		Map<Integer, StudentMonthlyHours> map = new LinkedHashMap<>(students);
		map.put(studentHours.getId(), studentHours);
		return new SchoolMonthlyHours(id, name, color, map);
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

}
