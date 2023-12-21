package com.abfl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonType;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tbl_trn_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@TypeDef(
	    name = "json",
	    typeClass = JsonType.class
	)
public class TrnLogEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "log_id")
    Long id;


    @Column(name = "cust_id")
    private Long custId;

    @Column(name = "login_id")
    public String loginId;

    @Column(name = "module_name")
    public String moduleName;

    @Column(name ="screen_id")
    public String screenId;

    @Column(name = "component_id")
    public String componentId;

    @Column(name = "access_time")
    public java.sql.Timestamp accessTime;

    @Column(name = "error_flag")
    public String errorFlag;

    @Column(name = "error_message")
    public String errorMessage;

    @Column(name = "request_api")
    public String requestApi;

    @Column(name = "request_time")
    public java.sql.Timestamp requestTime;

    @Column(name = "request_xml")
    public byte[] requestXml;

    @Column(name = "response_time")
    public java.sql.Timestamp responseTime;

    @Column(name = "response_xml")
    public byte[] responseXml;

    /***********new Column ***************/
    @Type(type = "json")
    @Column(name = "request_body")
    public String requestBody;

    @Type(type = "json")
    @Column(name = "response_body")
    public String responseBody;
}
