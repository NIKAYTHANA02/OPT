package com.abfl.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.abfl.data_objects.ConfigOutDO;

@Repository
public interface ConfigService {

	public ConfigOutDO getConfigDetailsById(int configId);
	public List<ConfigOutDO> getConfigDetails();
	public String getConfigValueById(int configId);

}
