//============================================================================
//Name        : SendNotesToServerThread.java
//Author      : Carl Barbee
//Description : The Async Thread for the Notes Activity. Sends the updated list
//							view items to the server in order to back up local list.
//============================================================================
package com.example.notekeeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.os.AsyncTask;

public abstract class SendNotesToServerThread extends
		AsyncTask<ArrayList<HashMap<String, String>>, Void, Void> {

	protected void doHTTPPost(ArrayList<HashMap<String, String>> myNotes)
			throws Exception {

		// instantiates HTTP client to make request
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String path = "http://" + SettingsActivity.ipAndPortNumber_
				+ "/updateserver";
		// URL with the post data
		HttpPost httpPost = new HttpPost(path);

		try {
			// The list of note pairs, where Note ID: is the Name and Title,
			// Description, and Reminder is the value.
			List<NameValuePair> noteIDs = new ArrayList<NameValuePair>();
			String noteID = "Note ID: ";

			for (int i = 0; i < myNotes.size(); i++) {
				// Create a new note.
				// The note: title, description, reminder.
				ArrayList<String> note = new ArrayList<String>();
				note.add(myNotes.get(i).get("TITLE"));
				note.add(myNotes.get(i).get("DESCRIPTION"));
				note.add(myNotes.get(i).get("REMINDER"));
				noteIDs.add(new BasicNameValuePair(noteID + Integer.toString(i), note
						.toString()));
			}

			// Serialize the list of user data to send to the server.
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(noteIDs);
			httpPost.setEntity(entity);

			// HTTP Post to the server.
			httpclient.execute(httpPost);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: Cannot Establish Connection");
		}
	}

	protected Void doInBackground(ArrayList<HashMap<String, String>>... params) {
		// Send notes to the server.
		try {
			doHTTPPost(params[0]);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}