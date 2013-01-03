package net.iteach.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.codehaus.jackson.annotate.JsonIgnore;

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
	
	@JsonIgnore
	public List<Coordinate> getList() {
		List<Coordinate> result = new ArrayList<>();
		for (Entry<CoordinateType, String> entry: entries()) {
			result.add(new Coordinate(entry.getKey(), entry.getValue()));
		}
		return result;
	}
}
