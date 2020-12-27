package com.androidsecuritycookbook.chapter7.recipe1;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import android.content.Context;

import com.androidsecuritycookbook.chapter7.R;

public class HttpCaller {

	Context context;

	public HttpCaller(Context context) {
		this.context = context;
	}

	private static final String STORE_PASSWORD = "androidcookbook";

	private KeyStore loadKeyStore() throws Exception {
		final KeyStore keyStore = KeyStore.getInstance("BKS");
		final InputStream inputStream = context.getResources().openRawResource(
				R.raw.mytruststore);
		try {
			keyStore.load(inputStream, STORE_PASSWORD.toCharArray());
			return keyStore;
		} finally {
			inputStream.close();
		}
	}

	/**
	 * https request to server with self signed SSL using httpclient
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse httpClientRequestUsingLocalKeystore(String url)
			throws ClientProtocolException, IOException {
		HttpClient httpClient = new LocalTrustStoreMyHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = httpClient.execute(httpGet);
		return response;
	}

	public class LocalTrustStoreMyHttpClient extends DefaultHttpClient {
		@Override
		protected ClientConnectionManager createClientConnectionManager() {
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			try {
				registry.register(new Scheme("https", new SSLSocketFactory(
						loadKeyStore()), 443));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new SingleClientConnManager(getParams(), registry);
		}
	}

	/**
	 * https request to server with self signed SSL using URLConnection API
	 * 
	 * @param targetUrl
	 * @return
	 * @throws Exception
	 */
	public InputStream uRLConnectionRequestLocalTruststore(String targetUrl)
			throws Exception {
		URL url = new URL(targetUrl);

		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, new TrustManager[] { new LocalTrustStoreTrustManager(
				loadKeyStore()) }, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		HttpsURLConnection urlHttpsConnection = (HttpsURLConnection) url
				.openConnection();
		urlHttpsConnection.setDefaultUseCaches(false);
		urlHttpsConnection.setUseCaches(false);
		urlHttpsConnection.setRequestMethod("GET");
		urlHttpsConnection.connect();
		if (urlHttpsConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return urlHttpsConnection.getInputStream();
		} else {
			throw new HttpResponseException(
					urlHttpsConnection.getResponseCode(), "failed");
		}
	}

}
