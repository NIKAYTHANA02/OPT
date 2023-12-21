package com.abfl.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import com.abfl.configurations.GeneralParamEnum;
import com.abfl.data_objects.SmsApiResponse;
import com.abfl.data_objects.abfl.OtpSmsObj;
import com.abfl.exception_handling.CustomException;
import com.abfl.service.ConfigService;
import com.abfl.service.OtpService;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OtpServiceImpl implements OtpService {

	@Autowired
	RestTemplate restTemplate;

	@Value("${otpSmsUrl}")
	private String otpSmsUrl;

	@Value("${apiKey}")
	private String apiKey;

	@Value("${method}")
	private String method;

	@Value("${proxyInd}")
	private String proxyInd;

	@Value("${proxyHost}")
	private String proxyHost;

	@Value("${proxyPort}")
	private int proxyPort;

	@Autowired
	ConfigService configService;

	// -------------------------------------Sms Send
	// Api--------------------------------------------

	@Override
	public ResponseEntity<?> sendSms(String otp, String mobileNumber, String smsInd) throws CustomException {
		try {
			System.out.println("Inside send sms method ");
			OtpSmsObj.Sms sms = new OtpSmsObj.Sms();
			sms.setMsgid(configService.getConfigValueById(GeneralParamEnum.SMS_Msg_ID.getId()));
			sms.setTo(mobileNumber);
			String hashKey = configService.getConfigValueById(GeneralParamEnum.OTP_Hash_Key.getId());
			String msgTemplate = null;
			String textMessage = null;

			if (smsInd.equals("R")) {
				msgTemplate = configService.getConfigValueById(GeneralParamEnum.Registration_OTP_SMS.getId());

			}

			else if (smsInd.equals("M")) {
				msgTemplate = configService.getConfigValueById(GeneralParamEnum.MPIN_OTP_SMS.getId());

			} else if (smsInd.equals("D")) {
				msgTemplate = configService.getConfigValueById(GeneralParamEnum.Disbursement_OTP_SMS.getId());

			} else if (smsInd.equals("L1") || smsInd.equals("L2") || smsInd.equals("L3") || smsInd.equals("L4")) {
				msgTemplate = configService.getConfigValueById(GeneralParamEnum.Loan_OTP_SMS.getId());
			} else if (smsInd.equals("MU")) {
				msgTemplate = configService.getConfigValueById(GeneralParamEnum.Mobile_Update_OTP_SMS.getId());
			}

			if (msgTemplate != null && !msgTemplate.isEmpty())
				textMessage = msgTemplate.replace("{#var1#}", otp).replace("{#var2#}", hashKey);

			OtpSmsObj otpSmsObj = new OtpSmsObj().builder().message(textMessage)
					.sender(configService.getConfigValueById(GeneralParamEnum.SMS_Sender.getId())).sms(sms).build();

			return callSmsSendApi(otpSmsObj);
		} catch (Exception e) {
			System.out.println("Exception in sendSms :" + e.toString());
			throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST)
					.message("server error.Please try again sometime").build();

		}

	}

//    public ResponseEntity<?> callSmsSendApi_old1(OtpSmsObj otpSmsObj) {
//
//        String responseBody = null;
//        CloseableHttpClient client = HttpClients.createDefault();
//        HttpPost httpPost = new HttpPost(otpSmsUrl);
//        System.out.println("otpSmsUrl:" + otpSmsUrl);
//
//        String smsJsonString = new JSONObject(otpSmsObj).toString();
//        System.out.println("smsJsonString is:" + smsJsonString);
//
//        String encodedSms = "";
//        encodedSms = UriUtils.encodeQuery(smsJsonString, "UTF-8");
//
//
//        final List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("api_key", apiKey));
//        params.add(new BasicNameValuePair("method", method));
//        params.add(new BasicNameValuePair("json", encodedSms));
//
//
//        try {
//            httpPost.setEntity(new UrlEncodedFormEntity(params));
//
//            /**************************Proxy config***************************************/
//            String proxyValues = "";
//
//
//            String proxy[] = proxyValues.split("@");
//            String proxyInd = proxy[0];
//            String host = "10.100.9.55";
//            String port = "3128";
//
//            System.out.println("before calling proxi");
//            if (host != null && port != null) {
//                System.setProperty("http.proxyHost", host);
//                System.setProperty("http.proxyPort", port);
//
//                System.out.println("Inside System property ");
//
//            }
//
//            /******************************************************************************/
//
//            new SSLCertification().configSSL();
//            System.out.println("after the CongiSSL:");
//            HttpResponse httpResponse = client.execute(httpPost);
//
//
//            HttpEntity entity = httpResponse.getEntity();
//            String strBody = entity != null ? EntityUtils.toString(entity) : null;
//            int status = httpResponse.getStatusLine().getStatusCode();
//
//            Gson gson = new Gson();
//            SmsApiResponse smsApiResponse = gson.fromJson(strBody, SmsApiResponse.class);
//
//
//            return ResponseEntity.status(status).body(smsApiResponse);
//        } catch (IOException e) {
//            System.out.println("Exception occured in callSmsSendApi" + e.toString());
//            throw new RuntimeException(e);
//        } finally {
//            System.getProperties().remove("https.proxyHost");
//            System.getProperties().remove("https.proxyPort");
//        }
//    }

	@Override
	public ResponseEntity<SmsApiResponse> callSmsSendApi(OtpSmsObj otpSmsObj) {
		try {
			System.out.println("***** Send sms api calling *****");
			HttpURLConnection con;
			String smsJsonString = new JSONObject(otpSmsObj).toString();
			System.out.println("smsJsonString is:" + smsJsonString);

			String encodedSms = "";
			encodedSms = UriUtils.encodeQuery(smsJsonString, "UTF-8");

			String strUrl = otpSmsUrl + "?" + "api_key=" + apiKey + "&method=" + method + "&json=" + smsJsonString;
			System.out.println("strUrl:" + strUrl);

			// URL url = new
			// URL("https://api-alerts.solutionsinfini.com/v4/?api_key=Ade9047a16dea2f5e22924641d6851c27&method=sms.json&json={\"message\":
			// \"Dear Applicant, we regret to inform you that we cannot process your ABFL
			// Loan application number 124 online. Our executive will connect with you
			// shortly to help you with the same. Regards, -Aditya Birla Finance Limited\",
			// \"sender\": \"ABCABF\", \"sms\": {\"to\": \"9994959426\", \"msgid\":
			// \"1\"}}");
			URL url = new URL(strUrl);

			// InetSocketAddress proxyInet = new InetSocketAddress("10.100.1.99", 3128);
			InetSocketAddress proxyInet = new InetSocketAddress(proxyHost, proxyPort);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyInet);
			// HttpURLConnection con = (HttpURLConnection) url.openConnection(proxy);

			if (proxyInd.equalsIgnoreCase("Y")) {
				con = (HttpURLConnection) url.openConnection(proxy);
			} else {
				con = (HttpURLConnection) url.openConnection();
			}

			// optional default is GET
			con.setRequestMethod("GET");

			con.setConnectTimeout(5000);

			// add request header
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			String strRespose = "";

			try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));) {
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				System.out.println("Response is:****************************");
				System.out.println(response);
				System.out.println("****************************");
				strRespose = response.toString();
			}
			System.out.println("out of try block");
//            httpPost.setEntity(new UrlEncodedFormEntity(params));
//            HttpResponse httpResponse = client.execute(httpPost);
//
//            HttpEntity entity = httpResponse.getEntity();
//            String strBody = entity != null ? EntityUtils.toString(entity) : null;
//            int status = httpResponse.getStatusLine().getStatusCode();

			Gson gson = new Gson();
			SmsApiResponse smsApiResponse = gson.fromJson(strRespose, SmsApiResponse.class);

			return ResponseEntity.status(200).body(smsApiResponse);
		} catch (IOException e) {
			System.out.println("Exception occured in callSmsSendApi" + e.toString());
			throw new RuntimeException(e);
		} catch (Exception e) {
			System.out.println("Exception occured in callSmsSendApi" + e.toString());
			throw new RuntimeException(e);
		}

	}

	public static void main(String[] args) {
		String str = "{\"message\": \"Dear Applicant, we regret to inform you that we cannot process your ABFL Loan application number 124 online. Our executive will connect with you shortly to help you with the same. Regards, -Aditya Birla Finance Limited\", \"sender\": \"ABCABF\", \"sms\": {\"to\": \"9666020760\", \"msgid\": \"1\"}}";
		System.out.println("input" + str);
		try {
			String encodedUrl1 = UriUtils.encodeQuery(str, "UTF-8");// not change
			String encodedUrl2 = URLEncoder.encode(str, "UTF-8");// changed
			String encodedUrl3 = URLEncoder.encode(str, StandardCharsets.UTF_8.displayName());// changed

			System.out.println("url1 " + encodedUrl1 + "\n" + "url2=" + encodedUrl2 + "\n" + "url3=" + encodedUrl3);

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public String sendsmsTest() {
		try {
			URL url = new URL(
					"https://api-alerts.solutionsinfini.com/v4/?api_key=Ade9047a16dea2f5e22924641d6851c27&method=sms.json&json={\"message\": \"Dear Applicant, we regret to inform you that we cannot process your ABFL Loan application number 124 online. Our executive will connect with you shortly to help you with the same. Regards, -Aditya Birla Finance Limited\", \"sender\": \"ABCABF\", \"sms\": {\"to\": \"9994959426\", \"msgid\": \"1\"}}");

			InetSocketAddress proxyInet = new InetSocketAddress("10.100.1.99", 3128);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyInet);
			HttpURLConnection con = (HttpURLConnection) url.openConnection(proxy);

			// optional default is GET
			con.setRequestMethod("GET");

			con.setConnectTimeout(5);
			// add request header
			con.setRequestProperty("User-Agent", "Mozilla/5.0");

			try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));) {
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				System.out.println("Response is:****************************");
				System.out.println(response);
				System.out.println("****************************");
				return response.toString();
			}
		} catch (Exception e) {
			log.info("Exception in sneding sms " + e.toString());
			return "";
		}
	}
}
