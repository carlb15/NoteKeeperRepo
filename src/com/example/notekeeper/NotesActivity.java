//============================================================================
//Name        : NoteActivity.java
//Author      : Carl Barbee
//Description : Creates a list view to display each of the notes. Contains an
//							action bar to add a new note and refresh the server to back-up
// 							the notes created locally.
//============================================================================

package com.example.notekeeper;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class NotesActivity extends Activity {

	private ListView notesView;
	private ListAdapter notesAdapter;
	private SendNotesToServerThread notesThread_;

	// All the parameters that will be added to element in the ArrayList.
	protected static final String noteTitle = "TITLE";
	protected static final String noteDescription = "DESCRIPTION";
	protected static final String noteReminder = "REMINDER";
	private static int currentNoteSize = 0;
	protected static ArrayList<HashMap<String, String>> myNotes = new ArrayList<HashMap<String, String>>();

	/**
	 * Creates a new activity to display a list of notes.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes);
		
		// Sets all of the buttons and layouts for the main activity.
		notesView = (ListView) findViewById(R.id.notesView);
		
		// Display the action bar for the main activity.
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Note Keeper");
		
		// Display the list of notes.
		notesAdapter = new SimpleAdapter(this, myNotes, R.layout.onenote,
				new String[] { noteTitle, noteDescription, noteReminder }, new int[] {
						R.id.TITLE, R.id.DESCRIPTION, R.id.REMINDER });
		notesView.setAdapter(notesAdapter);
		onListenForItemClick();
	}

	/**
	 * Starts listening for a click from the user on a particular item in the list
	 * view. When an item is clicked, a prompt displays to ask if the want to
	 * delete that item.
	 */
	public void onListenForItemClick() {
		notesView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// Prompts the user if they wish to delete the selected item in the
				// list.
				AlertDialog.Builder adb = new AlertDialog.Builder(NotesActivity.this);
				adb.setMessage("Remove Note?");
				// alert dialog options
				final int positionToRemove = position;
				adb.setNegativeButton("Cancel", null);
				adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Updates the list view and map of notes.
						((BaseAdapter) notesAdapter).notifyDataSetChanged();
						myNotes.remove(positionToRemove).toString();
					}
				});
				adb.show();
			}
		});
	}

	/**
	 * Inflates the menu whenever items are added to the action bar.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Checks if any of the following actions are selected then responds. New -
	 * Goes to a new activity to create a note. home - Goes back to the settings
	 * activity. refresh - Updates the server with the current list of items in
	 * the list view.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_new:
			Toast.makeText(this, "Add Note", Toast.LENGTH_SHORT).show();
			// Go to new note activity so that user can enter data.
			Intent GoToNewNote = new Intent(getApplicationContext(),
					NewNoteActivity.class);
			startActivity(GoToNewNote);
			break;
		case android.R.id.home:
			// Go back to settings activity to enter new port and IP.
			Intent GoBackToSettings = new Intent(getApplicationContext(),
					SettingsActivity.class);
			startActivity(GoBackToSettings);
			break;
		case R.id.action_refresh:
			if (currentNoteSize != myNotes.size()) {
				Toast.makeText(this, "Server Updated!", Toast.LENGTH_SHORT).show();
				// Update the server with the changes made locally.
				notesThread_ = new SendNotesToServerThread() {
				};
				notesThread_.execute(myNotes);
			}
			break;
		default:
			break;
		}
		currentNoteSize = myNotes.size();
		return true;
	}
}
