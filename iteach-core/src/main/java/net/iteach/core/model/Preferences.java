package net.iteach.core.model;

import lombok.Data;

import java.util.Map;

@Data
public class Preferences {

    private final Map<PreferenceKey, String> map;

}
