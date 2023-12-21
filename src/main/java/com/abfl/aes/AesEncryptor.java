package com.abfl.aes;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.context.annotation.Configuration;

import lombok.SneakyThrows;

@Configuration
public class AesEncryptor implements AttributeConverter<String, String> {

	private static final String password = "abfl@12345";
	private static String salt = "abflrpa256encryption";
	private static String ivSpec = "abflrpa256ivSpec";
	private static int pswdIterations = 10;
	private static int keySize = 256;

	private static String utfString = "UTF-8";
	private static String aesString = "AES/CBC/PKCS5Padding";

	@SneakyThrows
	@Override
	public String convertToDatabaseColumn(String attribute) {
		if (attribute == null)
			return null;
		byte[] saltBytes;
		byte[] passwordBytes;

		byte[] encryptedTextBytes = null;
		try {
			saltBytes = salt.getBytes(utfString);
			passwordBytes = password.getBytes(utfString);

			PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA512Digest());
			System.out.println("Salt bytes  : " + saltBytes);
			System.out.println("Password bytes : " + passwordBytes);
			gen.init(passwordBytes, saltBytes, pswdIterations);
			byte[] secret = ((KeyParameter) gen.generateDerivedParameters(keySize)).getKey();
			String hexKey = bytesToHex(secret);
			System.out.println(hexKey);
			byte[] key = Hex.decode(hexKey);

			SecretKey secretKey = new SecretKeySpec(key, "AES");
			System.out.println(secretKey);

			// encrypt the message
			IvParameterSpec iv = new IvParameterSpec(ivSpec.getBytes(utfString));
			Cipher cipher = Cipher.getInstance(aesString);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			encryptedTextBytes = cipher.doFinal(attribute.getBytes(utfString));
		} catch (Exception e) {
			throw e;
		}
		return DatatypeConverter.printBase64Binary(encryptedTextBytes);
	}

	@SneakyThrows
	@Override
	public String convertToEntityAttribute(String dbData) {
		if (dbData == null)
			return null;
		byte[] decryptedTextBytes = null;
		byte[] saltBytes;
		byte[] passwordBytes;
		try {
			byte[] encryptedTextBytes = DatatypeConverter.parseBase64Binary(dbData);
			// Derive the key
			saltBytes = salt.getBytes(utfString);
			passwordBytes = password.getBytes(utfString);

			PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA512Digest());
			System.out.println("Salt bytes  : " + saltBytes);
			System.out.println("Password bytes : " + passwordBytes);
			gen.init(passwordBytes, saltBytes, pswdIterations);
			byte[] secret = ((KeyParameter) gen.generateDerivedParameters(keySize)).getKey();
			String hexKey = bytesToHex(secret);
			System.out.println(hexKey);
			byte[] key = Hex.decode(hexKey);

			SecretKey secretKey = new SecretKeySpec(key, "AES");
			System.out.println(secretKey);

			// Decrypt the message
			IvParameterSpec iv = new IvParameterSpec(ivSpec.getBytes(utfString));
			Cipher cipher = Cipher.getInstance(aesString);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
			decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
		} catch (Exception e) {
			throw e;
		}
		return new String(decryptedTextBytes);
	}

	public static String bytesToHex(byte[] bytes) {

		final char[] hexArray = "0123456789abcdef".toCharArray();

		char[] hexChars = new char[bytes.length * 2];

		for (int j = 0; j < bytes.length; j++) {

			int v = bytes[j] & 0xFF;

			hexChars[j * 2] = hexArray[v >>> 4];

			hexChars[j * 2 + 1] = hexArray[v & 0x0F];

		}

		return new String(hexChars);

	}
}