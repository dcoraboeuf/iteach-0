package net.iteach.core.report;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentMonthlyHours {

    private final int id;
    private final String name;
    private final boolean disabled;
    private final Money hourlyRate;
    private final BigDecimal monthlyHours;
    private final BigDecimal totalHours;

    public StudentMonthlyHours(int id, String name, boolean disabled, Money hourlyRate) {
        this(id, name, disabled, hourlyRate, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public StudentMonthlyHours addHours(BigDecimal hours, boolean isMonthlyHours) {
        return new StudentMonthlyHours(id, name, disabled, hourlyRate,
                isMonthlyHours ? monthlyHours.add(hours) : monthlyHours,
                totalHours.add(hours));
    }

    public Money getMonthlyAmount() {
        return hourlyRate.multipliedBy(getMonthlyHours(), RoundingMode.HALF_UP);
    }

    public Money getTotalAmount() {
        return hourlyRate.multipliedBy(getTotalHours(), RoundingMode.HALF_UP);
    }

}
