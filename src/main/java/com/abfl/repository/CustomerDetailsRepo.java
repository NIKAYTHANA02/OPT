package com.abfl.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.abfl.data_objects.CustomerDetails;

@Transactional
@Repository
public interface CustomerDetailsRepo extends JpaRepository<CustomerDetails, Long> {

	@Query(value = "SELECT m FROM CustomerDetails m WHERE  m.custMobile= ?1 and  m.custDob= ?2 and m.custDeviceId = ?3")

	CustomerDetails getCustomerDetails(String mobileNumber, String dob, String deviceId);

	@Query(value = "SELECT m FROM CustomerDetails m WHERE  m.custMobile= ?1 and  m.custPan= ?2 and m.custDeviceId = ?3")
	CustomerDetails getCustomerDetailsByPan(String mobileNumber, String pan, String deviceId);

	@Query(value = "SELECT m FROM CustomerDetails m WHERE  m.custMobile= ?1 and  m.custDob= ?2 and m.custDeviceId = ?3 and m.custId= ?4")

	CustomerDetails getCustomerDetails(String mobileNumber, String dob, String deviceId, Long custId);

	@Query(value = "SELECT m FROM CustomerDetails m WHERE  m.custDob= ?1 and m.custDeviceId = ?2 and m.custId= ?3")
	CustomerDetails getCustomerDetails2(String dob, String deviceId, Long custId);

	@Query(value = "SELECT m FROM CustomerDetails m WHERE  m.custMobile= ?1 and m.custDeviceId = ?2 and m.custId= ?3")
	CustomerDetails getCustomerDetails3(String mobileNumber, String deviceId, Long custId);

	@Query(value = "SELECT m FROM CustomerDetails m WHERE  m.custMobile= ?1 and  m.custDob= ?2 and m.custId= ?3")
	CustomerDetails getCustomerDetails4(String mobileNumber, String dob, Long custId);

	@Modifying
	@Query(value = "UPDATE tbl_mst_cust_details SET blocked_Ind=?1 WHERE cust_id=?2", nativeQuery = true)
	public void updateBlockedIndicator(String blockedInd, Long custId);
}
