package net.iteach.service.impl;

import net.iteach.api.PreferenceService;
import net.iteach.core.model.PreferenceKey;
import net.iteach.core.model.Preferences;
import net.iteach.core.security.SecurityUtils;
import net.iteach.service.dao.PreferenceDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class PreferenceServiceImpl implements PreferenceService {

    private final SecurityUtils securityUtils;
    private final PreferenceDao preferenceDao;

    @Autowired
    public PreferenceServiceImpl(SecurityUtils securityUtils, PreferenceDao preferenceDao) {
        this.securityUtils = securityUtils;
        this.preferenceDao = preferenceDao;
    }

    @Override
    @Transactional(readOnly = true)
    public String getPreference(PreferenceKey key) {
        // Gets the current user
        int userId = securityUtils.getCurrentUserId();
        // Gets the stored value
        String value = preferenceDao.getValue(userId, key.name());
        // If not found, returns the default value
        if (value == null) {
            return key.getDefaultValue();
        } else {
            return value;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public int getPreferenceAsInt(PreferenceKey key) {
        return Integer.parseInt(getPreference(key), 10);
    }

    @Override
    @Transactional
    public void setPreference(PreferenceKey key, String value) {
        // Gets the current user
        int userId = securityUtils.getCurrentUserId();
        // In any case, trims the value
        String valueToStore = StringUtils.trim(value);
        // Validates and format the value
        valueToStore = key.validateAndFormat(valueToStore);
        // Stores the value
        preferenceDao.storeValue(userId, key.name(), valueToStore);
    }

    @Override
    @Transactional
    public void setPreference(PreferenceKey key, int value) {
        setPreference(key, String.valueOf(value));
    }

    @Override
    @Transactional(readOnly = true)
    public Preferences getPreferences() {
        Map<PreferenceKey, String> map = new HashMap<>();
        for (PreferenceKey key : PreferenceKey.values()) {
            String value = getPreference(key);
            map.put(key, value);
        }
        return new Preferences(map);
    }
}
