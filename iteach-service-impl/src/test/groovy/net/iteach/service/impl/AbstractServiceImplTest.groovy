package net.iteach.service.impl

import static org.mockito.Mockito.mock

import javax.sql.DataSource

import org.joda.time.LocalTime

import org.junit.Test

class AbstractServiceImplTest {
	
	private final AbstractServiceImpl service = new AbstractServiceImpl (mock(DataSource.class), null)  {
		@Override
		public BigDecimal getHours(LocalTime from, LocalTime to) {
			return super.getHours(from, to);
		}
	};
	
	@Test
	void getHours() {
		assert 1 == service.getHours(new LocalTime(11,0), new LocalTime(12,0))
	}
	
	@Test
	void getHours_more() {
		assert 3 == service.getHours(new LocalTime(11,0), new LocalTime(14,0))
	}
	
	@Test
	void getHours_half() {
		assert 1.5 == service.getHours(new LocalTime(11,0), new LocalTime(12,30))
	}
	
	@Test
	void getHours_quarter() {
		assert 2.25 == service.getHours(new LocalTime(11,0), new LocalTime(13,15))
	}
	
	@Test
	void getHours_one() {
		assert 3.02 == service.getHours(new LocalTime(11,0), new LocalTime(14,1))
	}
	
	@Test
	void getHours_tenth() {
		assert 1.1 == service.getHours(new LocalTime(11,0), new LocalTime(12,6))
	}

}
