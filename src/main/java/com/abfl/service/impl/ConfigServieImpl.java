package com.abfl.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.abfl.data_objects.ConfigOutDO;
import com.abfl.repository.ConfigRepo;
import com.abfl.service.ConfigService;

@Component
public class ConfigServieImpl implements ConfigService {
	@Autowired
	ConfigRepo configRepo;

	@Override
	public ConfigOutDO getConfigDetailsById(int configId) {

		return configRepo.getConfigDetailsById(configId);
	}
	@Override
	public String getConfigValueById(int configId) {

		return configRepo.getConfigValueById(configId);
	}
	
	public List<ConfigOutDO> getConfigDetails() {
		return configRepo.getCongfigDetails();

	}
}
