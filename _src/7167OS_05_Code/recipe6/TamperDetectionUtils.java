package com.androidsecuritycookbook.chapter5.recipe6;

import android.content.Context;
import android.content.pm.ApplicationInfo;

public final class TamperDetectionUtils {

	/**
	 * checks to confirm the installer is the playstore
	 * 
	 * @param context
	 */
	public static boolean checkGooglePlayStore(Context context) {
		String installerPackageName = context.getPackageManager()
				.getInstallerPackageName(context.getPackageName());
		return installerPackageName != null
				&& installerPackageName.startsWith("com.google.android");
	}

	public static boolean isEmulator() {
		try {

			Class systemPropertyClazz = Class
					.forName("android.os.SystemProperties");

			boolean kernelQemu = getProperty(systemPropertyClazz,
					"ro.kernel.qemu").length() > 0;
			boolean hardwareGoldfish = getProperty(systemPropertyClazz,
					"ro.hardware").equals("goldfish");
			boolean modelSdk = getProperty(systemPropertyClazz,
					"ro.product.model").equals("sdk");

			if (kernelQemu || hardwareGoldfish || modelSdk) {
				return true;
			}
		} catch (Exception e) {
			// error assumes emulator
		}
		return false;
	}

	private static String getProperty(Class clazz, String propertyName)
			throws Exception {
		return (String) clazz.getMethod("get", new Class[] { String.class })
				.invoke(clazz, new Object[] { propertyName });
	}

	/**
	 * checks if the app is debuggable
	 * 
	 * @param context
	 * @return ture if debuggable | false if not
	 */
	public static boolean isDebuggable(Context context) {
		return (0 != (context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
	}
}
