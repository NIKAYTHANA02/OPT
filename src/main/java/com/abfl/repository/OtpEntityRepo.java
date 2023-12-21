package com.abfl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.abfl.entity.OtpEntity;

public interface OtpEntityRepo extends JpaRepository<OtpEntity, Long> {

	// @Query(value = "from OtpEntity o where o.mobileNumber= ?1 order by
	// o.createdOn limit 1")

	// @Query(value = "select o from OtpEntity o where o.mobileNumber= ?1 order by
	// o.createdOn LIMIT 1",nativeQuery = true)
	@Query(value = "select *  from tbl_trn_otp  where mobile_number= ?1 and sms_ind=?2 order by created_On desc limit 1", nativeQuery = true)
	Optional<OtpEntity> getLatestOtpForMobileNumberSmsInd(String mobileNumber, String smsInd);

	@Query(value = "select *  from tbl_trn_otp  where mobile_number= ?1 order by created_On desc limit 1", nativeQuery = true)
	Optional<OtpEntity> getLatestOtpForMobileNumber(String mobileNumber);

	@Query(value = "select *  from tbl_trn_otp  where mobile_number= ?1 and sms_ind=?2 and created_by=?3 and created_On \\:\\: date = CURRENT_DATE order by created_On desc limit 5", nativeQuery = true)
	public List<OtpEntity> getOTPDetails(String mobileNumber, String smsInd, String custId);
}
