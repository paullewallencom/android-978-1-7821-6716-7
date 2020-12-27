package com.androidsecuritycookbook.chapter9.recipe6;

import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Calendar;

import javax.security.auth.x500.X500Principal;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.util.Log;

/**
 * Sample of using the AndroidKeyStore Note this is for API levle 18+
 * 
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class AndroidKeystoreUtil {

	KeyStore keyStore;

	private static final String KEY_ALIAS = "MY_KEY";
	private static final String TAG = "AndroidKeystoreUtil";

	public static final String ANDROID_KEYSTORE = "AndroidKeyStore";

	public void loadKeyStore() {
		try {
			keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
			keyStore.load(null);
		} catch (Exception e) {
			// TODO: Handle this appropriately in your app
			e.printStackTrace();
		}
	}

	/**
	 * Generate and save new RSA keypair
	 * 
	 * @param alais
	 *            used to retrieve the keypair
	 * @throws Exception
	 */
	public void generateNewKeyPair(String alais, Context context)
			throws Exception {

		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		// expires 1 year from today
		end.add(1, Calendar.YEAR);

		KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
				.setAlias(alais).setSubject(new X500Principal("CN=" + alais))
				.setSerialNumber(BigInteger.TEN).setStartDate(start.getTime())
				.setEndDate(end.getTime()).build();

		// use the Android keystore
		KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA",
				ANDROID_KEYSTORE);
		gen.initialize(spec);

		// generates the keypair
		gen.generateKeyPair();
	}

	public PrivateKey loadPrivteKey(String alais) throws Exception {

		if (keyStore.isKeyEntry(alais)) {
			Log.e(TAG, "Could not find key alais: " + alais);
			return null;
		}

		KeyStore.Entry entry = keyStore.getEntry(KEY_ALIAS, null);

		if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
			Log.e(TAG, "Alais: " + alais + " is not a PrivateKey");
			return null;
		}

		return ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
	}

}
