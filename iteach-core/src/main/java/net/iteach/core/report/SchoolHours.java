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
public class SchoolHours {

	private final int id;
	private final String name;
	private final String color;
	private final Map<Integer, StudentHours> students;

	public SchoolHours(int id, String name, String color) {
		this(id, name, color, Collections.<Integer, StudentHours> emptyMap());
	}

	public SchoolHours addStudent(StudentHours studentHours) {
		Map<Integer, StudentHours> map = new LinkedHashMap<>(students);
		map.put(studentHours.getId(), studentHours);
		return new SchoolHours(id, name, color, map);
	}
	
	public BigDecimal getHours () {
		BigDecimal hours = BigDecimal.ZERO;
		for (StudentHours studentHours: students.values()) {
			hours = hours.add(studentHours.getHours());
		}
		return hours;
	}

}
