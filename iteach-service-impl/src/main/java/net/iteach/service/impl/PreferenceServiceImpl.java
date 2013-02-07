package net.iteach.service.impl;

import javax.sql.DataSource;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.iteach.api.PreferenceService;
import net.iteach.core.model.PreferenceKey;

@Service
public class PreferenceServiceImpl extends AbstractServiceImpl implements PreferenceService {

	@Autowired
	public PreferenceServiceImpl(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
	}

	@Override
	public String getPreference(PreferenceKey key) {
		// FIXME Implement PreferenceService.getPreference
		return null;
	}

	@Override
	public void setPreference(PreferenceKey key, String value) {
		// FIXME Implement PreferenceService.setPreference
		
	}
	
	

}
