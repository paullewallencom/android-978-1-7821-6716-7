package com.androidsecuritycookbook.chapter9.recipe3;

import android.app.Activity;
import android.content.SharedPreferences;

public class BaseActivity extends Activity {

	private SharedPreferences mPrefs;

	/**
	 * Exmaple of getting the same instance of the secure prefs from the
	 * application object
	 * 
	 * @return
	 */
	public final SharedPreferences getSharedPrefs() {
		if (null == mPrefs) {
			mPrefs = MyApp.getInstance().getSharedPrefs();
		}
		return mPrefs;
	}

}
