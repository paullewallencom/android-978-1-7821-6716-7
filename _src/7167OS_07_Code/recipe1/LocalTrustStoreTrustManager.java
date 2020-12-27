package com.androidsecuritycookbook.chapter7.recipe1;

import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class LocalTrustStoreTrustManager implements X509TrustManager {

	private X509TrustManager mTrustManager;

	public LocalTrustStoreTrustManager(KeyStore localTrustStore) {
		try {
			TrustManagerFactory tmf = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(localTrustStore);

			mTrustManager = findX509TrustManager(tmf);
			if (mTrustManager == null) {
				throw new IllegalStateException(
						"Couldn't find X509TrustManager");
			}
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		mTrustManager.checkClientTrusted(chain, authType);
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		mTrustManager.checkServerTrusted(chain, authType);
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return mTrustManager.getAcceptedIssuers();
	}

	private X509TrustManager findX509TrustManager(TrustManagerFactory tmf) {
		TrustManager trustManagers[] = tmf.getTrustManagers();
		for (int i = 0; i < trustManagers.length; i++) {
			if (trustManagers[i] instanceof X509TrustManager) {
				return (X509TrustManager) trustManagers[i];
			}
		}
		return null;
	}

}
