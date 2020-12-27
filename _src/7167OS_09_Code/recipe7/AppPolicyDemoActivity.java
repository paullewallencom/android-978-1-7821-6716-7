package com.androidsecuritycookbook.chapter9.recipe7;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.androidsecuritycookbook.chapter7.R;

public class AppPolicyDemoActivity extends Activity {

	private static final int ENABLE_DEVICE_ADMIN_REQUEST_CODE = 11;
	private static final int ENABLE_DEVICE_ENCRYPT_REQUEST_CODE = 12;
	private AppPolicyController controller;
	private TextView mStatusTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_policy);
		mStatusTextView = (TextView) findViewById(R.id.deviceAdminStatus);

		controller = new AppPolicyController();

		if (!controller.isDeviceAdminActive(getApplicationContext())) {
			// Launch the activity to have the user enable our admin.
			startActivityForResult(
					controller
							.getEnableDeviceAdminIntent(getApplicationContext()),
					ENABLE_DEVICE_ADMIN_REQUEST_CODE);
		} else {
			mStatusTextView.setText("Device admin enabled, yay!");
			// admin is already activated so ensure policies are set
			controller.enforceTimeToLock(getApplicationContext());
			if (controller.shouldPromptToEnableDeviceEncrpytion(this)) {
				startActivityForResult(
						controller.getEnableDeviceEncrpytionIntent(),
						ENABLE_DEVICE_ENCRYPT_REQUEST_CODE);
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ENABLE_DEVICE_ADMIN_REQUEST_CODE) {
			if (resultCode != RESULT_OK) {
				handleDevicePolicyNotActive();
			} else {
				mStatusTextView.setText("Device admin enabled, yay!");
				if (controller.shouldPromptToEnableDeviceEncrpytion(this)) {
					startActivityForResult(
							controller.getEnableDeviceEncrpytionIntent(),
							ENABLE_DEVICE_ENCRYPT_REQUEST_CODE);
				}
			}

		} else if (requestCode == ENABLE_DEVICE_ENCRYPT_REQUEST_CODE
				&& resultCode != RESULT_OK) {
			handleDevicePolicyNotActive();
		}
	}

	/**
	 * App specific logic to handle users who haven't activated device admin
	 */
	private void handleDevicePolicyNotActive() {
		Toast.makeText(this, R.string.device_admin_policy_breach_message,
				Toast.LENGTH_SHORT).show();
	}
}