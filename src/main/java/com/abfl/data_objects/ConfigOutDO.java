package com.abfl.data_objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ConfigOutDO {
	@Id
	@Column(name = "config_id")
	private Integer configId;
	
	@Column(name = "config_descripton")
	private String configDescription;
	
	@Column(name = "config_unit")
	private String configUnit;
	
	@Column(name = "config_value")
	private String configValue;
	
	@Column(name = "status")
	private String status;
}
