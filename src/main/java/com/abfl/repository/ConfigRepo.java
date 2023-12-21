package com.abfl.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.abfl.data_objects.ConfigOutDO;

@Repository
public interface ConfigRepo extends JpaRepository<ConfigOutDO, Integer> {

	@Transactional
	@Query(value = "select config_id,config_descripton,config_unit,config_value,status from  tbl_mst_config where config_id IN (3, 4, 5) and status='Y'", nativeQuery = true)
	public List<ConfigOutDO> getCongfigDetails();
	
	@Transactional
	@Query(value = "select config_id,config_descripton,config_unit,config_value,status from  tbl_mst_config where config_id =?1 and status='Y'", nativeQuery = true)
	public ConfigOutDO getConfigDetailsById(int configId);
	
	@Transactional
	@Query(value = "select config_value from  tbl_mst_config where config_id =?1 and status='Y'", nativeQuery = true)
	public String getConfigValueById(int configId);

}
