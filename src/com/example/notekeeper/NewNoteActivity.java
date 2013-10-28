//============================================================================
//Name        : NewNoteActivity.java
//Author      : Carl Barbee
//Description : Creates a new note. Includes title, description, 
//							a reminder (Date/Time).
//============================================================================

package com.example.notekeeper;

import java.util.HashMap;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewNoteActivity extends NotesActivity {

	private SensorManager mSensorManager;
	private ShakeEventListener mSensorListener;
	protected EditText titleTextEdit;
	protected EditText descriptionTextEdit;
	protected TextView timeReminderView;
	protected TextView dateReminderView;
	protected Button newNoteButton;
	private static final String noteTitle = "TITLE";
	private static final String noteDescription = "DESCRIPTION";
	private static final String noteReminder = "REMINDER";

	/**
	 * Starts the new note activity and setups all buttons and edit texts.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_note);
		// Setups each of the layouts.
		titleTextEdit = (EditText) findViewById(R.id.TITLE);
		descriptionTextEdit = (EditText) findViewById(R.id.DESCRIPTION);
		timeReminderView = (TextView) findViewById(R.id.time);
		dateReminderView = (TextView) findViewById(R.id.date);
		newNoteButton = (Button) findViewById(R.id.addNewNote);
		// Starts the listeners.
		StartShakeListener();
		AddNewNoteButton();
	}

	/**
	 * Starts the listener to listen for shakes on every axis.
	 */
	public void StartShakeListener() {
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
		mSensorListener = new ShakeEventListener();

		mSensorListener
				.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

					public void onShake() {
						ResetViews();
						Toast.makeText(NewNoteActivity.this, "Shake Away!",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	/**
	 * Resets the views to their default.
	 */
	public void ResetViews() {
		titleTextEdit.setText("");
		descriptionTextEdit.setText("");
		timeReminderView.setText("Time Reminder...");
		dateReminderView.setText("Date Reminder...");
	}

	/**
	 * Retrieves the data from each of the views. Adds the text to a map to update
	 * the listview and server.
	 */
	public void GetNewNoteData() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(noteTitle, "Title: " + titleTextEdit.getText().toString());
		map.put(noteDescription, "Description: "
				+ descriptionTextEdit.getText().toString());
		map.put(noteReminder, "Reminder: " + timeReminderView.getText().toString()
				+ dateReminderView.getText().toString());
		myNotes.add(map);
	}

	/**
	 * Starts listening for the add new note button.
	 */
	public void AddNewNoteButton() {

		newNoteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Extract the data from each of the views.
				GetNewNoteData();
				// Add the new note to the main activity's list view.
				Intent moveToNotes = new Intent(getApplicationContext(),
						NotesActivity.class);
				startActivity(moveToNotes);
			}
		});
	}

	/**
	 * Inflates action bar is present.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_note, menu);
		return true;
	}

	/**
	 * Override for the accelerometer.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		// Override for the accelerometer.
		mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
	}

	/**
	 * Checks for home button pressed.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// Goes back to previous activity.
			Intent GoBackToMainActivty = new Intent(getApplicationContext(),
					NotesActivity.class);
			startActivity(GoBackToMainActivty);
			break;
		default:
			break;
		}

		return true;
	}

	/**
	 * Override for the accelerometer.
	 */
	@Override
	protected void onStop() {
		// Override of onStop for the accelerometer.
		mSensorManager.unregisterListener(mSensorListener);
		super.onStop();
	}

	/**
	 * Creates new fragment to chose a new time.
	 */
	public void showTimePickerDialog(View v) {
		// Starts a new fragment to select a new time.
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getFragmentManager(), "timePicker");
	}

	/**
	 * Creates a new fragment to chose a new date.
	 */
	public void showDatePickerDialog(View v) {
		// Starts new fragment to select a date.
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}
}
