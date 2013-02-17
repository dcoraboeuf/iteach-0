package net.iteach.core.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Preferences {

    private final Map<String, String> map;

    public Preferences(Map<PreferenceKey, String> map) {
        Map<String, String> m = new HashMap<>();
        for (PreferenceKey key : map.keySet()) {
            String value = map.get(key);
            m.put(key.name(), value);
        }
        this.map = m;
    }

}
