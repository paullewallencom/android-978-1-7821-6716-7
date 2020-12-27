package com.androidsecuritycookbook.chapter9.recipe1;

import java.security.Security;

import android.app.Application;

/**
 * 
 * Ideal place for inserting the spongycastle provider is the Application object
 * 
 */
public class MyApp extends Application {

	static {
		Security.insertProviderAt(
				new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

}
