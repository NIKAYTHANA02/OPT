package com.abfl.configurations;

import lombok.Getter;

@Getter
public enum GeneralParamEnum {

	OTP_Hash_Key(8,"OTP Hash Key"),
	Registration_OTP_SMS(9,"Registration OTP SMS"),
	MPIN_OTP_SMS(10,"MPIN OTP SMS"),
	Disbursement_OTP_SMS(11,"Disbursement  OTP SMS"),
	Loan_OTP_SMS(51,"Loan OTP SMS"),
	Mobile_Update_OTP_SMS(53,"Mobile Update OTP SMS"),
	
	PayU_Merchant_ID(12,"PayU Merchant ID"),
	PayU_Merchant_Key(13,"PayU Merchant Key"),
	PayU_Merchant_Salt(14,"PayU Merchant Salt"),
	PayU_Success_URL(15,"PayU Success URL"),
	PayU_Failure_URL(16,"PayU Failure URL "),
	SMS_Sender(17,"SMS Sender"),
	SMS_Msg_ID(18,"SMS Msg ID")
	;
	private GeneralParamEnum(int id, String desc) {
		this.id = id;
		this.desc = desc;
	}
	  private int id;
	  private String desc;
}
