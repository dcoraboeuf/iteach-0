package net.iteach.web.planning;

import net.iteach.core.model.PreferenceKey;

public enum PlanningBound {

    MIN(PreferenceKey.PLANNING_MIN_TIME),

    MAX(PreferenceKey.PLANNING_MAX_TIME);

    private final PreferenceKey preferenceKey;

    private PlanningBound(PreferenceKey preferenceKey) {
        this.preferenceKey = preferenceKey;
    }

    public PreferenceKey getPreferenceKey() {
        return preferenceKey;
    }

}
