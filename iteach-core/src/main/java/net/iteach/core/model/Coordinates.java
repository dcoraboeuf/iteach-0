package net.iteach.core.model;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Container for a list of coordinates.
 */
@Data
@AllArgsConstructor
public class Coordinates {

    public static Coordinates create () {
        return new Coordinates();
    }

    private final Map<CoordinateType,String> map;

    public Coordinates() {
        map = Collections.emptyMap();
    }

    public Coordinates add(CoordinateType type, String value) {
        Map<CoordinateType,String> newMap = new TreeMap<>(map);
        newMap.put(type, value);
        return new Coordinates(newMap);
    }

	public Iterable<Entry<CoordinateType, String>> entries() {
		return map.entrySet();
	}
}
