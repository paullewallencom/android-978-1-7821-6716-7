package com.androidsecuritycookbook.chapter9.recipe3;

import android.app.Application;
import android.content.SharedPreferences;

import com.securepreferences.SecurePreferences;

/**
 * 
 * Ideal place for inserting the spongycastle provider is the Application object
 * 
 * Remember to reference https://github.com/scottyab/secure-preferences as
 * library project
 * 
 */
public class MyApp extends Application {

	private SharedPreferences mPrefs;

	private static MyApp SELF;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	/**
	 * Create the secure preferences and the rest of the app should access here
	 * 
	 * @return
	 */
	public final SharedPreferences getSharedPrefs() {
		if (null == mPrefs) {
			mPrefs = new SecurePreferences(MyApp.this);
		}
		return mPrefs;
	}

	public static MyApp getInstance() {
		return SELF;
	}

}
