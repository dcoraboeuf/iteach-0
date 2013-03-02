package net.iteach.service.impl;

import net.iteach.api.ConfigurationService;
import net.iteach.api.model.ConfigurationKey;
import net.iteach.service.dao.ConfigurationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigurationDao configurationDao;

    @Autowired
    public ConfigurationServiceImpl(ConfigurationDao configurationDao) {
        this.configurationDao = configurationDao;
    }

    @Override
    @Transactional(readOnly = true)
    public String getConfigurationValue(ConfigurationKey key) {
        String value = configurationDao.getValue(key.name());
        if (value == null) {
            return key.getDefaultValue();
        } else {
            return value;
        }
    }

}
