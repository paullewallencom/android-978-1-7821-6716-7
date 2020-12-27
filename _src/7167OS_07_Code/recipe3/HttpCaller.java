package com.androidsecuritycookbook.chapter7.recipe3;

import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import android.content.Context;

public class HttpCaller {
	Context context;

	public HttpCaller(Context context) {
		this.context = context;
	}

	/**
	 * pins from Android.com
	 */
	private static String[] pins = new String[] {
			"B3A3B5195E7C0D39B8FA68D41A64780F79FD4EE9",
			"43DAD630EE53F8A980CA6EFD85F46AA37990E0EA",
			"C07A98688D89FBAB05640C117DAA7D65B8CACC4E" };

	/**
	 * Validates the Public keys of the server SSL certs, if ok connection
	 * established
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public InputStream makePinnedRequest(URL url) throws Exception {
		TrustManager[] trustManagers = new TrustManager[] { new PubKeyPinningTrustManager(
				pins) };
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, trustManagers, null);
		HttpsURLConnection urlConnection = (HttpsURLConnection) url
				.openConnection();
		urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());
		urlConnection.connect();
		return urlConnection.getInputStream();
	}

}
