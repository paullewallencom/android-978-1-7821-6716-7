package com.androidsecuritycookbook.chapter9.recipe4;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;

/**
 * 
 * Static util to help creating PBE encryption keys and perform encryption and
 * decryption
 */
public class GeneratingPBEKeys {

	private static IvParameterSpec iv;

	public static IvParameterSpec getIV() {
		if (iv == null) {
			iv = new IvParameterSpec(generateRandomByteArray(32));
		}
		return iv;
	}

	private static byte[] salt;

	public static byte[] getSalt() {
		if (salt == null) {
			salt = generateRandomByteArray(32);
		}
		return salt;
	}

	public static byte[] generateRandomByteArray(int sizeInBytes) {
		byte[] randomNumberByteArray = new byte[sizeInBytes];
		// populate the array with random bytes using non seeded secure random
		new SecureRandom().nextBytes(randomNumberByteArray);
		return randomNumberByteArray;
	}

	public static SecretKey generatePBEKey(char[] password, byte[] salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		final int iterations = 10000;
		final int outputKeyLength = 256;

		SecretKeyFactory secretKeyFactory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");
		KeySpec keySpec = new PBEKeySpec(password, salt, iterations,
				outputKeyLength);
		SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
		return secretKey;
	}

	public static byte[] encrpytWithPBE(String painText, String userPassword)
			throws GeneralSecurityException, IOException {

		SecretKey secretKey = generatePBEKey(userPassword.toCharArray(),
				getSalt());

		final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, getIV());
		return cipher.doFinal(painText.getBytes("UTF-8"));
	}

	public static String decrpytWithPBE(byte[] cipherText, String userPassword)
			throws GeneralSecurityException, IOException {

		SecretKey secretKey = generatePBEKey(userPassword.toCharArray(),
				getSalt());

		final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, getIV());
		return cipher.doFinal(cipherText).toString();
	}

	private GeneratingPBEKeys() {
	}
}
