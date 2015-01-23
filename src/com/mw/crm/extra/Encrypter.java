package com.mw.crm.extra;

import java.security.MessageDigest;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.util.EncodingUtils;

public class Encrypter {
	private KeySpec keySpec;
	private SecretKey key;
	private IvParameterSpec iv;

	public Encrypter(){}
	
	public Encrypter(String keyString, String ivString) {
		try {

			keyString = hexToString("746172616e67746172616e67746172616e67746172616e67"); // old
			// keyString = hexToString("4d6f62216c6540313233"); //new
			final MessageDigest md = MessageDigest.getInstance("md5");
			// final byte[] digestOfPassword =
			// md.digest(Base64.decodeBase64(keyString.getBytes("utf-8")));
			final byte[] digestOfPassword = md.digest(EncodingUtils
					.getAsciiBytes(keyString));

			// byte[] digestOfPassword1={10,24,47,21,14,65,47,00,23,41,58,98};
			byte[] keyBytes = new byte[24];
			System.arraycopy(digestOfPassword, 0, keyBytes, 0,
					digestOfPassword.length);
			// System.out.println(keyBytes.length);

			// final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
			for (int j = 0, k = 16; j < 8;) {
				keyBytes[k++] = keyBytes[j++];
			}

			keySpec = new DESedeKeySpec(keyBytes);

			key = SecretKeyFactory.getInstance("DESede")
					.generateSecret(keySpec);

			// iv = new IvParameterSpec(ivString.getBytes());
			iv = new IvParameterSpec(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 });

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String encrypt(String value) {
		// System.out.println("Value in Encryptor Class"+value);
		try {
			/*
			 * Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			 * SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");
			 * IvParameterSpec ivspec = new IvParameterSpec(myIV);
			 * 
			 * c3des.init(Cipher.ENCRYPT_MODE, myKey, ivspec);
			 */
			Cipher ecipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			ecipher.init(Cipher.ENCRYPT_MODE, key, iv);

			if (value == null)
				return null;

			// Encode the string into bytes using utf-8
			// byte[] utf8 = value.getBytes("UTF8");
			byte[] utf8 = EncodingUtils.getAsciiBytes(value);

			// Encrypt
			byte[] enc = ecipher.doFinal(utf8);
			toHex(enc);
			// Encode bytes to base64 to get a string
			return new String(Base64.encodeBase64(enc));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String decrypt(String value) {
		try {
			Cipher dcipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			dcipher.init(Cipher.DECRYPT_MODE, key, iv);

			if (value == null)
				return "";

			// Decode base64 to get bytes
			byte[] dec = Base64.decodeBase64(value.getBytes());

			// Decrypt
			byte[] utf8 = dcipher.doFinal(dec);

			// Decode using utf-8
			return new String(utf8, "UTF8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	private final static String HEX = "0123456789abcdef";

	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}

	public String hexToString(String txtInHex) {
		byte[] txtInByte = new byte[txtInHex.length() / 2];
		int j = 0;
		for (int i = 0; i < txtInHex.length(); i += 2) {
			txtInByte[j++] = Byte.parseByte(txtInHex.substring(i, i + 2), 16);
		}
		return new String(txtInByte);
	}
}