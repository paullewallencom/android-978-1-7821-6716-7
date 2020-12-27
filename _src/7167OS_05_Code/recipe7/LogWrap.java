package com.androidsecuritycookbook.chapter5.recipe7;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.Log;

/**
 * Wrapper for the Android Log to enable Proguard to identify all log statements
 * and exclude also saves some typing
 * 
 */
public class LogWrap {

	public static final String TAG = "MyAppTag";

	public static void e(final Object obj, final Throwable cause) {
		Log.e(TAG, String.valueOf(obj));
		Log.e(TAG, convertThrowableStackToString(cause));
	}

	public static void e(final Object obj) {
		Log.e(TAG, String.valueOf(obj));
	}

	public static void w(final Object obj, final Throwable cause) {
		Log.w(TAG, String.valueOf(obj));
		Log.w(TAG, convertThrowableStackToString(cause));
	}

	public static void w(final Object obj) {
		Log.w(TAG, String.valueOf(obj));
	}

	public static void i(final Object obj) {
		Log.i(TAG, String.valueOf(obj));
	}

	public static void d(final Object obj) {
		Log.d(TAG, String.valueOf(obj));
	}

	public static void v(final Object obj) {
		Log.v(TAG, String.valueOf(obj));
	}

	public static String convertThrowableStackToString(final Throwable thr) {
		StringWriter b = new StringWriter();
		thr.printStackTrace(new PrintWriter(b));
		return b.toString();
	}
}