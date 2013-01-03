package net.iteach.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class CoordinatesTest {

	@Test
	public void create() {
		Coordinates coordinates = Coordinates.create();
		assertNotNull(coordinates);
		assertFalse(coordinates.entries().iterator().hasNext());
		List<Coordinate> list = coordinates.getList();
		assertNotNull(list);
		assertTrue(list.isEmpty());
	}

	@Test
	public void add() {
		Coordinates coordinates = Coordinates.create()
				.add(CoordinateType.EMAIL, "info@test.com")
				.add(CoordinateType.PHONE, "01 02 03 04 05");
		assertNotNull(coordinates);
		List<Coordinate> list = coordinates.getList();
		assertNotNull(list);
		assertEquals(2, list.size());
		assertEquals(CoordinateType.PHONE, list.get(0).getType());
		assertEquals(CoordinateType.EMAIL, list.get(1).getType());
		assertEquals("01 02 03 04 05", list.get(0).getValue());
		assertEquals("info@test.com", list.get(1).getValue());
	}

}
