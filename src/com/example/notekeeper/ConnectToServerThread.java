//============================================================================
//Name        : ConnecToServer.java
//Author      : Carl Barbee
//Description : The Async thread for the settings activity to connect to the server.
//============================================================================

package com.example.notekeeper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.UserDataHandler;
import android.R.raw;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract.CommonDataKinds.Note;

public class ConnectToServerThread extends
		AsyncTask<String, Void, ArrayList<String>> {

	private SettingsActivity mySettingsActivity;

	public ConnectToServerThread(SettingsActivity myActivity) {
		// Creates a new main activity to update it.
		mySettingsActivity = myActivity;
	}

	protected void onPostExecute(ArrayList<String> msgFromServer) {
		// Display the contents from the server.
		Intent createNotesActivity = new Intent(mySettingsActivity,
				NotesActivity.class);
		mySettingsActivity.startActivity(createNotesActivity);
		mySettingsActivity.resetEnterButtonAndResetThread();
	};

	protected ArrayList<String> doHTTPost(String ipAndPortNumber)
			throws Exception {

		BufferedReader in = null;
		ArrayList<String> rawDataArray = new ArrayList<String>();

		try {
			// Setups the HTTP client.
			HttpClient client = new DefaultHttpClient();
			URI website = new URI("http://" + ipAndPortNumber + "/getnotes");
			// Request using HTTP GET Method.
			HttpPost request = new HttpPost(website);
			HttpResponse response = client.execute(request);
			// string using buffered reader
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			String l = "";
			StringBuffer line = new StringBuffer();
			String newline = System.getProperty("line.separator");

			while ((l = in.readLine()) != null) {
				line.append(l + newline);
			}

			String newData = line.toString();

			// Creates a note ID for each note.
			final String noteID = "Note ID: ";

			String data = "{\"Notes\": " + newData + "}";
			JSONObject obj = new JSONObject(data);
			JSONArray newNotesArray = new JSONArray();
			newNotesArray = obj.getJSONArray("Notes");
			String title, description, reminder;
			ArrayList<HashMap<String, String>> notes = new ArrayList<HashMap<String, String>>();
			;

			// Set the new note ID and get the data from the client.
			for (int i = 0; i < newNotesArray.length(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				String newNoteID = noteID + i;
				JSONObject myObj = newNotesArray.getJSONObject(i);
				String param = myObj.getString(newNoteID);
				title = param.substring(param.indexOf("Title:") + 7,
						param.indexOf(", Description:"));
				description = param.substring(param.indexOf("Description:") + 12,
						param.indexOf(", Reminder:"));
				reminder = param.substring(param.indexOf("Reminder:") + 10,
						param.indexOf("]"));
				map.put(NotesActivity.noteTitle, "Title: " + title);
				map.put(NotesActivity.noteDescription, "Description: " + description);
				map.put(NotesActivity.noteReminder, "Reminder: " + reminder);
				notes.add(map);
			}

			NotesActivity.myNotes = notes;
			
			in.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return rawDataArray;
	}
	
	@Override
	protected ArrayList<String> doInBackground(String... params) {
		try {
			// Check for connection to the server.
			return doHTTPost(params[0]);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
