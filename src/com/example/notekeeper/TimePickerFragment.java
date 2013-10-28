//============================================================================
//Name        : TimePickerFragment.java
//Author      : Carl Barbee
//Description : Setups the time picker for user input. The time format is hh:mm (24-hour)
//============================================================================

package com.example.notekeeper;

import java.util.Calendar;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {

	/**
	 * Creates the dialog box which is displayed whenever the user
	 * clicks on the time reminder button.
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));		
	}
	
	/**
	 * Adds a time to the new note in 24-format (hh:mm).
	 */
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// Sets the time text edit on the new note activity.
		TextView activityButton = (TextView) getActivity().findViewById(
				R.id.time);
		String myTime = "  " + hourOfDay + ":" + minute;
		activityButton.setText(myTime);
	}
}