package com.androidsecuritycookbook.chapter7.recipe3;

import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Java util to connect to URL and download the pins
 * 
 */
public class CalcPins {
	/*
	 * static { Security.insertProviderAt( new
	 * org.bouncycastle.jce.provider.BouncyCastleProvider(), 1); }
	 */
	private static final String HASH_ALGORTHM = "SHA1";
	private MessageDigest digest;

	public CalcPins() throws Exception {
		digest = MessageDigest.getInstance(HASH_ALGORTHM, "BC");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if ((args.length == 1) || (args.length == 2)) {
			String[] hostAndPort = args[0].split(":");
			String host = hostAndPort[0];
			// if port blank assume 443
			int port = (hostAndPort.length == 1) ? 443 : Integer
					.parseInt(hostAndPort[1]);

			try {
				CalcPins calc = new CalcPins();
				calc.fetchAndPrintPinHashs(host, port);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Usage: java CalcPins <host>[:port]");
			return;
		}
	}

	private void fetchAndPrintPinHashs(String host, int port) throws Exception {
		SSLContext context = SSLContext.getInstance("TLS");
		PublicKeyExtractingTrustManager tm = new PublicKeyExtractingTrustManager();
		context.init(null, new TrustManager[] { tm }, null);
		SSLSocketFactory factory = context.getSocketFactory();
		SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
		socket.setSoTimeout(10000);
		socket.startHandshake();
		socket.close();
	}

	public class PublicKeyExtractingTrustManager implements X509TrustManager {

		public X509Certificate[] getAcceptedIssuers() {
			throw new UnsupportedOperationException();
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			throw new UnsupportedOperationException();
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			for (X509Certificate cert : chain) {
				byte[] pubKey = cert.getPublicKey().getEncoded();
				final byte[] hash = digest.digest(pubKey);
				System.out.println(bytesToHex(hash));
			}
		}
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
