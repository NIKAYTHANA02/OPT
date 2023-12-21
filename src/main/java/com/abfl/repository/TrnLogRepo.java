package com.abfl.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.abfl.entity.TrnLogEntity;

@Transactional
@Repository
public interface TrnLogRepo extends JpaRepository<TrnLogEntity, Long> {

     @Modifying
	 @Query(value = "UPDATE tbl_trn_log SET cust_id=?1 , login_id=?2, module_name =?3, screen_id =?4 WHERE log_id=?5" , nativeQuery = true)
	 public void updateLogDetails(Long custId,Long loginId, String moduleName, String screenId, Long logId);
}
