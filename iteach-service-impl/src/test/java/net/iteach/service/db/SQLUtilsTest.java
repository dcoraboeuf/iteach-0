package net.iteach.service.db;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

public class SQLUtilsTest {
	
	@Test
	public void dateToDB() {
		assertEquals ("2012-12-28", SQLUtils.dateToDB(new LocalDate(2012, 12, 28)));
		assertEquals ("2013-01-02", SQLUtils.dateToDB(new LocalDate(2013, 1, 2)));
	}
	
	@Test
	public void dateFromDB() {
		assertEquals (new LocalDate(2012, 12, 28), SQLUtils.dateFromDB("2012-12-28"));
		assertEquals (new LocalDate(2013, 1, 2), SQLUtils.dateFromDB("2013-01-02"));
	}
	
	@Test
	public void timeToDB() {
		assertEquals ("09:08", SQLUtils.timeToDB(new LocalTime(9, 8)));
		assertEquals ("13:27", SQLUtils.timeToDB(new LocalTime(13, 27)));
	}
	
	@Test
	public void timeFromDB() {
		assertEquals (new LocalTime(9, 8), SQLUtils.timeFromDB("09:08"));
		assertEquals (new LocalTime(13, 27), SQLUtils.timeFromDB("13:27"));
	}

}
