/*
 * Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.androidsecuritycookbook.chapter7.recipe3;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Validates the server cert chain for matching mPins
 * 
 * @author scottab
 * 
 */
public class PubKeyPinningTrustManager implements X509TrustManager {

	private final String[] mPins;
	private final MessageDigest mDigest;

	public PubKeyPinningTrustManager(String[] pins)
			throws GeneralSecurityException {
		this.mPins = pins;
		mDigest = MessageDigest.getInstance("SHA1");
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// validate all the pins
		for (X509Certificate cert : chain) {
			final boolean expected = validateCertificatePin(cert);
			if (!expected) {
				throw new CertificateException("could not find a valid pin");
			}
		}
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// we are validated the server and so this is not implemented.
		throw new CertificateException("Cilent valdation not implemented");
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

	private boolean validateCertificatePin(X509Certificate certificate)
			throws CertificateException {
		final byte[] pubKeyInfo = certificate.getPublicKey().getEncoded();
		final byte[] pin = mDigest.digest(pubKeyInfo);
		final String pinAsHex = bytesToHex(pin);
		for (String validPin : mPins) {
			if (validPin.equalsIgnoreCase(pinAsHex)) {
				return true;
			}
		}
		return false;
	}

	public static String bytesToHex(byte[] bytes) {
		final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

}