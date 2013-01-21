package net.iteach.core.report;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Wither;

@Data
public class StudentHours {

	private final int id;
	private final String name;
	@Wither(AccessLevel.PROTECTED)
	private final BigDecimal hours;
	
	public StudentHours addHours(BigDecimal v) {
		return withHours(hours.add(v));
	}

}
