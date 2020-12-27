package com.androidsecuritycookbook.chapter7.recipe1;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;


public class LocalKeyStoreSSLSocketFactory extends SSLSocketFactory {

	private SSLContext sslContext;

	public LocalKeyStoreSSLSocketFactory(SSLContext sslContext,
			KeyStore keyStore) throws Exception {
		super(null, null, null, null, null, null);

		sslContext.init(null,
				new TrustManager[] { new LocalTrustStoreTrustManager(keyStore) },
				null);
		this.sslContext = sslContext;

	}

	@Override
	public Socket createSocket(Socket socket, String host, int port,
			boolean autoClose) throws IOException {
		return sslContext.getSocketFactory().createSocket(socket, host, port,
				autoClose);
	}

	@Override
	public Socket createSocket() throws IOException {
		return sslContext.getSocketFactory().createSocket();
	}

}
