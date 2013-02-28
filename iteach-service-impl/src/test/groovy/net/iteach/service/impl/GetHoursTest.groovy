package net.iteach.service.impl

import org.joda.time.LocalTime
import org.junit.Test

import static net.iteach.service.db.SQLUtils.getHours

class GetHoursTest {

    @Test
    void getHours() {
        assert 1 == getHours(new LocalTime(11, 0), new LocalTime(12, 0))
    }

    @Test
    void getHours_more() {
        assert 3 == getHours(new LocalTime(11, 0), new LocalTime(14, 0))
    }

    @Test
    void getHours_half() {
        assert 1.5 == getHours(new LocalTime(11, 0), new LocalTime(12, 30))
    }

    @Test
    void getHours_quarter() {
        assert 2.25 == getHours(new LocalTime(11, 0), new LocalTime(13, 15))
    }

    @Test
    void getHours_one() {
        assert 3.02 == getHours(new LocalTime(11, 0), new LocalTime(14, 1))
    }

    @Test
    void getHours_tenth() {
        assert 1.1 == getHours(new LocalTime(11, 0), new LocalTime(12, 6))
    }

}
