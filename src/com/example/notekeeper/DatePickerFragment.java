//============================================================================
//Name        : DatePickerFragment.java
//Author      : Carl Barbee
//Description : The date picker fragment for picking a date for the new note.
//============================================================================

package com.example.notekeeper;

import java.util.Calendar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		TextView activityButton = (TextView) getActivity().findViewById(R.id.date);
		String myDate = "  " + month + "/" + day + "/" + year;
		activityButton.setText(myDate);
	}
}