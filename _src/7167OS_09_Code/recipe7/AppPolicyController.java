package com.androidsecuritycookbook.chapter9.recipe7;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.androidsecuritycookbook.chapter7.R;

/**
 * Handles communication with the DevicePolicyManager with specific logic for
 * this app
 */
public class AppPolicyController {

	public boolean isDeviceAdminActive(Context context) {
		DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);

		ComponentName appPolicyReceiver = new ComponentName(context,
				AppPolicyReceiver.class);

		return devicePolicyManager.isAdminActive(appPolicyReceiver);
	}

	public Intent getEnableDeviceAdminIntent(Context context) {

		ComponentName appPolicyReceiver = new ComponentName(context,
				AppPolicyReceiver.class);

		Intent activateDeviceAdminIntent = new Intent(
				DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

		activateDeviceAdminIntent.putExtra(
				DevicePolicyManager.EXTRA_DEVICE_ADMIN, appPolicyReceiver);

		// include optional explanation message
		activateDeviceAdminIntent.putExtra(
				DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				context.getString(R.string.device_admin_activation_message));

		return activateDeviceAdminIntent;
	}

	// 3 mins
	private static final long MAX_TIME_TILL_LOCK = 3 * 60 * 1000;

	/**
	 * No prompt is required, so we can go ahead and enforce
	 * 
	 * @param context
	 */
	public void enforceTimeToLock(Context context) {
		DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);

		ComponentName appPolicyReceiver = new ComponentName(context,
				AppPolicyReceiver.class);

		devicePolicyManager.setMaximumTimeToLock(appPolicyReceiver,
				MAX_TIME_TILL_LOCK);
	}

	/**
	 * if device encryption supported but not active or in progress fire intest
	 * to open the device encrpytion settings screen
	 * 
	 * @param context
	 */
	@SuppressLint("NewApi")
	public boolean shouldPromptToEnableDeviceEncrpytion(Context context) {
		DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
		int currentStatus = devicePolicyManager.getStorageEncryptionStatus();
		if (currentStatus == DevicePolicyManager.ENCRYPTION_STATUS_INACTIVE) {
			return true;
		}
		return false;
	}

	public Intent getEnableDeviceEncrpytionIntent() {
		return new Intent(DevicePolicyManager.ACTION_START_ENCRYPTION);
	}

}
