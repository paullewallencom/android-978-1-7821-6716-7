package com.androidsecuritycookbook.chapter9.recipe7;

import android.annotation.SuppressLint;
import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import com.androidsecuritycookbook.chapter7.R;

/**
 * handles events from the
 * 
 */
public class AppPolicyReceiver extends DeviceAdminReceiver {

	// Called when the app is about to be deactivated as a device administrator.
	@SuppressLint("NewApi")
	@Override
	public void onDisabled(Context context, Intent intent) {
		// depending on your requirements, you may want to disable the app or
		// wipe stored data e.g clear prefs
		context.getSharedPreferences(context.getPackageName(),
				Context.MODE_PRIVATE).edit().clear().apply();
		super.onDisabled(context, intent);
	}

	@Override
	public void onEnabled(Context context, Intent intent) {
		super.onEnabled(context, intent);

		// once enabled enforce
		AppPolicyController controller = new AppPolicyController();
		controller.enforceTimeToLock(context);

		controller.shouldPromptToEnableDeviceEncrpytion(context);
	}

	@Override
	public CharSequence onDisableRequested(Context context, Intent intent) {
		// issue warning to the user before disable e.g. app prefs will be wiped
		return context.getText(R.string.device_admin_disable_policy);
	}
}
