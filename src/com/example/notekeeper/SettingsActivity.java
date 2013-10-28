//============================================================================
//Name        : SettingsActivity.java
//Author      : Carl Barbee
//Description : Requests for a valid port and IP number. If either a port or 
//							IP is not selected, then the phone vibrates and prompts the user
//							to enter a valid IP/port. Allows user to take a picture and save 
//							to the gallery.
//============================================================================

package com.example.notekeeper;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	private Button settingsEnterButton_;
	private Button pictureButton_;
	private Button settingsClearButton_;
	private EditText portNumberData_;
	private EditText ipNumData_;
	private ConnectToServerThread settingsThread_;
	private String portNumText_;
	private String ipNumText_;
	protected static String ipAndPortNumber_;

	/**
	 * Creates the activity for the settings.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Sets all of the buttons and layouts for the main activity.
		setContentView(R.layout.activity_settings);
		settingsEnterButton_ = (Button) findViewById(R.id.SettingsEnterButton);
		settingsClearButton_ = (Button) findViewById(R.id.SettingsClearButton);
		pictureButton_ = (Button) findViewById(R.id.pictureButton);
		portNumberData_ = (EditText) findViewById(R.id.PortNumberEditText);
		ipNumData_ = (EditText) findViewById(R.id.IPAddressEditText);

		// Hide the action bar.
		ActionBar actionBar = getActionBar();
		actionBar.hide();

		// Create new thread to get the data from the Web Server
		settingsThread_ = new ConnectToServerThread(this);

		// Start listening for the enter button to be pressed.
		EnterPressedByUser();
		ClearPressedByUser();
		PicturePressedByUser();
	}

	/**
	 * Sets up the enter button listener. Retrieves the data from the port and IP
	 * edit text fields and ping the server.
	 */
	public void EnterPressedByUser() {
		settingsEnterButton_.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!portNumberData_.getText().toString().equals("")
						&& !ipNumData_.getText().toString().equals("")) {
					// Retrieve port and IP number.
					portNumText_ = portNumberData_.getText().toString();
					ipNumText_ = ipNumData_.getText().toString();
					ipAndPortNumber_ = ipNumText_ + ":" + portNumText_;
					settingsEnterButton_.setEnabled(false);
					settingsThread_.execute(ipAndPortNumber_);
				}
				else {
					// Requests a valid IP/Port
					Toast.makeText(SettingsActivity.this, "Enter Valid Port/IP",
							Toast.LENGTH_SHORT).show();
					Vibrator vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					// Vibrate for 500 milliseconds
					vibrate.vibrate(500);
				}
			}
		});
	}

	/**
	 * Resets the enter button and cancels the thread.
	 */
	public void resetEnterButtonAndResetThread() {
		// Cancel thread and create a new one.
		settingsThread_.cancel(true);
		settingsThread_ = new ConnectToServerThread(this);
		EnterPressedByUser();
		settingsEnterButton_.setEnabled(true);
	}

	/**
	 * Starts listener for the cancel button. When cancel is pressed, both the
	 * port and IP fields are cleared.
	 */
	public void ClearPressedByUser() {
		settingsClearButton_.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Clears the port and IP text views
				portNumberData_.setText("");
				ipNumData_.setText("");
			}
		});
	}

	/**
	 * Listens for the picture button to be pressed.
	 */
	public void PicturePressedByUser() {

		// Starts looking for the camera button pressed.
		pictureButton_.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, 0);
			}
		});

	}

	/**
	 * Saves the picture to the Gallery.
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == RESULT_OK) {
			Bitmap image = (Bitmap) data.getExtras().get("data");
		}
	}

	/**
	 * Inflates the menu and adds items whenever the action bar is present.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}