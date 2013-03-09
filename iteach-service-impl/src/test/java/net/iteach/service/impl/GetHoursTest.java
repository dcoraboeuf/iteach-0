package net.iteach.service.impl;

import org.joda.time.LocalTime;
import org.junit.Test;

import java.math.BigDecimal;

import static net.iteach.service.db.SQLUtils.getHours;
import static org.junit.Assert.assertEquals;

public class GetHoursTest {

    @Test
    public void getHours_ok() {
        assertEquals(new BigDecimal("1.00"), getHours(new LocalTime(11, 0), new LocalTime(12, 0)));
    }

    @Test
    public void getHours_more() {
        assertEquals(new BigDecimal("3.00"), getHours(new LocalTime(11, 0), new LocalTime(14, 0)));
    }

    @Test
    public void getHours_half() {
        assertEquals(new BigDecimal("1.50"), getHours(new LocalTime(11, 0), new LocalTime(12, 30)));
    }

    @Test
    public void getHours_quarter() {
        assertEquals(new BigDecimal("2.25"), getHours(new LocalTime(11, 0), new LocalTime(13, 15)));
    }

    @Test
    public void getHours_one() {
        assertEquals(new BigDecimal("3.02"), getHours(new LocalTime(11, 0), new LocalTime(14, 1)));
    }

    @Test
    public void getHours_tenth() {
        assertEquals(new BigDecimal("1.10"), getHours(new LocalTime(11, 0), new LocalTime(12, 6)));
    }

}
