package com.abfl.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abfl.entity.TrnLogEntity;
import com.abfl.repository.TrnLogRepo;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrnLogService {

	@Autowired
	TrnLogRepo trnLogRepo;

	public Long saveApiRequest(String url, Object obj) {
		String strJsonBody = "";
		try {
			strJsonBody = new Gson().toJson(obj);
		} catch (Exception e) {
			log.info(e.toString());
		}
		return saveApiRequest(url, strJsonBody);

	}

	public Long saveApiRequest(String url, String jsonBody) {
		try {
			byte[] reqBytes = jsonBody.getBytes(StandardCharsets.UTF_8); // Java 7+ only

			TrnLogEntity trnLogEntity = new TrnLogEntity();
			trnLogEntity.setRequestApi(url);
			trnLogEntity.setRequestTime(new java.sql.Timestamp(new Date().getTime()));
			trnLogEntity.setRequestXml(reqBytes);
			trnLogEntity.setRequestBody(jsonBody);

			trnLogRepo.save(trnLogEntity);

			return trnLogEntity.getId();
		} catch (Exception e) {
			log.info("Exception Occurd in saveApiRequest:" + e.toString());
			return null;
		}
	}

	public Long saveApiRequest(String url, String jsonBody, TrnLogEntity trnLogEntity) {
		try {
			byte[] reqBytes = jsonBody.getBytes(StandardCharsets.UTF_8); // Java 7+ only
			trnLogEntity.setRequestApi(url);
			trnLogEntity.setRequestTime(new java.sql.Timestamp(new Date().getTime()));
			trnLogEntity.setRequestXml(reqBytes);
			trnLogEntity.setRequestBody(jsonBody);
			trnLogRepo.save(trnLogEntity);
			return trnLogEntity.getId();
		} catch (Exception e) {
			log.info("Exception Occured in saveApiRequest:" + e.toString());
			return null;
		}
	}

	public boolean saveApiResponse(Long reqId, Object obj) {
		String strJsonBody = "";
		try {
			strJsonBody = new Gson().toJson(obj);
		} catch (Exception e) {
			log.info(e.toString());

		}
		return saveApiResponse(reqId, strJsonBody);

	}

	public boolean saveApiResponse(Long reqId, String jsonBody) {
		try {
			if (reqId == null) {
				log.info("saveApiResponse reqId is NULL");
				return false;
			}
			byte[] reqBytes = jsonBody.getBytes(StandardCharsets.UTF_8); // Java 7+ only

			Optional<TrnLogEntity> optTrnLogEntity = trnLogRepo.findById(reqId);

			TrnLogEntity trnLogEntity = null;
			if (optTrnLogEntity.isPresent()) {
				trnLogEntity = optTrnLogEntity.get();
			} else {
				log.info("Data is not there is DB");
				return false;
			}

			trnLogEntity.setId(reqId);
			trnLogEntity.setResponseXml(reqBytes);
			trnLogEntity.setResponseTime(new java.sql.Timestamp(new Date().getTime()));
			trnLogEntity.setResponseBody(jsonBody);
			trnLogEntity.setAccessTime(new java.sql.Timestamp(new Date().getTime()));
			trnLogRepo.save(trnLogEntity);

			return true;
		} catch (Exception e) {
			log.info("Exception Occurd in saveApiResponse:" + e.toString());
			return false;
		}
	}

	public boolean saveApiErrorResponse(Long reqId, String jsonBody) {
		try {
			if (reqId == null) {
				System.out.println("saveApiResponse reqId is NULL");
				return false;
			}
			Optional<TrnLogEntity> optTrnLogEntity = trnLogRepo.findById(reqId);

			TrnLogEntity trnLogEntity = null;
			if (optTrnLogEntity.isPresent()) {
				trnLogEntity = optTrnLogEntity.get();
			} else {
				log.info("Data is not there is DB");
				return false;
			}
			trnLogEntity.setId(reqId);
			trnLogEntity.setResponseTime(new java.sql.Timestamp(new Date().getTime()));
			trnLogEntity.setErrorMessage(jsonBody);
			trnLogRepo.save(trnLogEntity);

			return true;
		} catch (Exception e) {
			log.info("Exception Occurd in saveApiResponse:" + e.toString());
			return false;
		}
	}

	public boolean updateLogDetails(Long custId, Long loginId, String moduleName, String screenId, Long reqId) {
		try {
			if (reqId == null) {
				log.info("reqId is NULL");
				return false;
			}
			Optional<TrnLogEntity> optTrnLogEntity = trnLogRepo.findById(reqId);

			TrnLogEntity trnLogEntity = null;
			if (optTrnLogEntity.isPresent()) {
				trnLogEntity = optTrnLogEntity.get();
			} else {
				log.info("Data is not there is DB");
				return false;
			}

			trnLogRepo.updateLogDetails(custId, loginId, moduleName, screenId, reqId);

			return true;
		} catch (Exception e) {
		log.info("Exception Occurd in saveApiResponse:" + e.toString());
			return false;
		}
	}
}
