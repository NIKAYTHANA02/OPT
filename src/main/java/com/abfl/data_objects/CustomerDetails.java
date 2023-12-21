package com.abfl.data_objects;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.abfl.aes.AesEncryptor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tbl_mst_cust_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CustomerDetails {
	//Hibernate: SELECT cust_id,custDeviceId,custDob,custMobile FROM CustomerDetails m WHERE m.custMobile= ?

	   @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    @Column(name = "cust_id")
	    private Long custId;

	    @Column(name = "cust_lan")
	    private String custLan;

	    @Convert(converter = AesEncryptor.class)
	    @Column(name = "cust_name")
	    private String custName;

	    @Convert(converter = AesEncryptor.class)
	    @Column(name = "cust_pan")
	    private String custPan;

	    @Convert(converter = AesEncryptor.class)
	    @Column(name = "cust_dob")
	   // private java.sql.Date custDob; //dbt string or TimeStapmp dd/mm/yyyy
	    private String custDob;
	    
	    @Convert(converter = AesEncryptor.class)
	    @Column(name = "cust_gender")
	    private String custGender;

	    @Convert(converter = AesEncryptor.class)
	    @Column(name = "cust_mobile")
	    private String custMobile;

	    @Convert(converter = AesEncryptor.class)
	    @Column(name = "cust_email")
	    private String custEmail;

	    @Column(name = "cust_device_id")
	    private String custDeviceId;

	    @Column(name = "cust_sim_no")
	    private String custSimNo;

	    @Column(name = "cust_image")
	    private String custImage;

	    @Column(name = "cust_mpin")
	    private String custMpin;

	    @Column(name = "cust_mobile_os_version")
	    private String custMobileOsVersion;

	    @Column(name = "tenant_id")
	    private Integer tenantId;

	    @Column(name = "branch_id")
	    private Integer branchId;

	    @Column(name = "user_code")
	    private String userCode;

	    @Column(name = "last_login_time")
	    private  java.sql.Date lastLoginTime;


	    @Column(name = "last_logoff_time")
	    private java.sql.Timestamp lastLogoffTime;

	    @Column(name = "status")
	    private String status;

	    @CreatedBy
	    @Column(name = "created_by")
	    private String createdBy;

	    @CreatedDate
	    @Column(name = "created_on")
	    private Timestamp createdOn;

	    @LastModifiedBy
	    @Column(name = "modified_by")
	    private String modifiedBy;

	    @LastModifiedDate
	    @Column(name = "modified_on")
	    private Timestamp modifiedOn;


	    @Column(name = "biometric")
	    private String biometric;


	    @Column(name = "type")
	    private String type;


	    @Column(name = "mobile_app_enabled" ) //Mobile App Enabled default Y
	    private String mobileAppEnable;

	    @Column(name = "disbursement_count" ) //Disbursement Count
	    private Integer disbursementCount;

	    @Column(name = "validate_indicator")
	    private String validateInd;
	    
	    @Column(name = "blocked_ind")
	    private String blockedInd;
}
