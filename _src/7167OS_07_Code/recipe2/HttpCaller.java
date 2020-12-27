package com.androidsecuritycookbook.chapter7.recipe2;

import info.guardianproject.onionkit.trust.StrongHttpsClient;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;

public class HttpCaller {
	Context context;

	public HttpCaller(Context context) {
		this.context = context;
	}

	public HttpResponse sampleRequest() throws ClientProtocolException,
			IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("https://your_server_goes_here:443/");

		HttpResponse response = httpclient.execute(httpget);
		return response;
	}

	/**
	 * https request using Onoinkit
	 * 
	 * @return
	 * @throws ch.boye.httpclientandroidlib.client.ClientProtocolException
	 * @throws IOException
	 */
	public ch.boye.httpclientandroidlib.HttpResponse strongSampleRequest()
			throws ch.boye.httpclientandroidlib.client.ClientProtocolException,
			IOException {
		StrongHttpsClient httpclient = new StrongHttpsClient(context);
		ch.boye.httpclientandroidlib.client.methods.HttpGet httpget = new ch.boye.httpclientandroidlib.client.methods.HttpGet(
				"https://server.com/path?apikey=123");

		httpclient.getStrongTrustManager().setVerifyChain(true);

		ch.boye.httpclientandroidlib.HttpResponse response = httpclient
				.execute(httpget);
		return response;
	}

}
