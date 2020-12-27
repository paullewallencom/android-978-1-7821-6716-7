package com.androidsecuritycookbook.chapter9.recipe2;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * 
 * Static util to help creating symmetric AES encryption keys and perform
 * encryption and decryption
 * 
 */
public class GeneratingKeys {

	public static byte[] strongerEncrpyt(String painText, byte[] iv)
			throws GeneralSecurityException, IOException {

		final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "SC");
		cipher.init(Cipher.ENCRYPT_MODE, getKey(), new IvParameterSpec(iv));
		return cipher.doFinal(painText.getBytes("UTF-8"));
	}

	public static SecretKey generateAESKey(int keysize)
			throws NoSuchAlgorithmException {

		// don't seed via constructor or .setSeed()
		final SecureRandom random = new SecureRandom();

		final KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(keysize, random);

		return generator.generateKey();
	}

	private static IvParameterSpec iv;

	public static IvParameterSpec getIV() {
		if (iv == null) {
			byte[] ivByteArray = new byte[32];
			// populate the array with random bytes
			new SecureRandom().nextBytes(ivByteArray);
			iv = new IvParameterSpec(ivByteArray);
		}
		return iv;
	}

	public static byte[] encrpyt(String plainText)
			throws GeneralSecurityException, IOException {

		final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		// use a initialization vector for further strength
		cipher.init(Cipher.ENCRYPT_MODE, getKey(), getIV());
		return cipher.doFinal(plainText.getBytes("UTF-8"));
	}

	private static SecretKey key;

	public static SecretKey getKey() throws NoSuchAlgorithmException {
		if (key == null) {
			key = generateAESKey(256);
		}
		return key;
	}

	public static String decrpyt(byte[] cipherText)
			throws GeneralSecurityException, IOException {
		final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, getKey());
		return cipher.doFinal(cipherText).toString();
	}

	private GeneratingKeys() {
	}
}
