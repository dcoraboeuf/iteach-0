package net.iteach.service.impl;

import net.iteach.core.model.PreferenceKey;
import net.iteach.core.security.SecurityUtils;
import net.iteach.service.dao.PreferenceDao;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PreferenceServiceTest {

    private SecurityUtils securityUtils;
    private PreferenceDao preferenceDao;
    private PreferenceServiceImpl service;

    @Before
    public void before() {
        securityUtils = mock(SecurityUtils.class);
        when(securityUtils.getCurrentUserId()).thenReturn(2);

        preferenceDao = mock(PreferenceDao.class);

        service = new PreferenceServiceImpl(
                securityUtils,
                preferenceDao
        );
    }

    @Test
    public void getPreference() {
        when(preferenceDao.getValue(2, "PLANNING_WEEKEND")).thenReturn("false");
        assertEquals("false", service.getPreference(PreferenceKey.PLANNING_WEEKEND));
    }

    @Test
    public void getPreference_not_found() {
        when(preferenceDao.getValue(2, "PLANNING_WEEKEND")).thenReturn(null);
        assertEquals("true", service.getPreference(PreferenceKey.PLANNING_WEEKEND));
    }

}
