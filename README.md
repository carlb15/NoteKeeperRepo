NoteKeeperRepo
==============

Purpose:

The NoteKeeper App stores a list of notes created by the user. Each note contains a title, description, and reminder (date/time). The app allows for the user to enter the port and IP number on start-up in order to connect to the server to back-up the notes created locally. Additionally, the user can take a picture and store it in the Gallery section of the phone. In the future, this picture will be used for login purposes.

File Descriptions:

ConnectToServerThread: Connects the client to the server with the user-inputted IP/Port number. It uses a HTTP POST to retrieve the notes stored on the server. The returned array is in JSON format.

DatePickerFragment: Fragment that is created whenever a user clicks on the Date button in the NewNoteActivity. Allows the user to specify a date reminder for the new note, then returns the date to the NewNoteActivity.

NewNoteActivity: Is created once the user clicks the "+" in the action bar on the NotesActivity. Allows the user to add a title, description, and reminder (date/time) for the note. Once the new note is created, by pressing the "Add note" button, the note is displayed in the NotesActivity in the list view. Shaking the phone on any axis allows you to clear all text fields and resetting them to their default state.

NotesActivity: Contains a list view of the notes created by the client. On start-up, the notes are retrieved from the server and displayed to the user. The refresh button in the action bar allows the client to back-up the notes created locally onto the server. The new button (+) allows the user to create a new note. When the user clicks on a note in the list view, a dialog box allows the user to delete an item from the list.

SendNotesToServerThread: Once the refresh button is pressed in the NotesActivity, an async thread is started and sends the list of notes to the server to back-up the notes. HTTP POST is used to send the notes to the server.
SettingsActivity: Allows the user to enter the IP and Port number to access the server. The server is used to back-up the notes onto the server. A button for taking a picture is used to take a picture of the user which is stored locally in the Gallery section of the phone. In the future, this image will be a way to access the application using face-lock. If the user enters an incorrect a port or IP number, then the phone will vibrate and display a toast requesting you to enter a valid IP and port.

ShakeEventListener: The settings for the accelerometer to determine if the user is attempting to clear all fields in the NewNotesActivity. The settings allow for shakes in every axis with a minimum force of 2.

TimePickerFragment: A fragment displaying a time picker dialog box to select a time reminder. The time reminder will be displayed in the new note activity in 24-hour format.

Program Flow:

On start-up the user enters the IP and port number of the server. The user is allowed to take a picture on this activity to user for future face-lock login. If an incorrect IP or port number is entered, then a toast is displayed saying that a valid port/ip needs to be entered as well as the phone vibrates. A thread is started to ping the server and retrieve the notes stored on the server using HTTP Post. The notes from the server are displayed on the NotesActivity in a list view. The user can enter a new note using the action tool bar which goes to a sub-activity. Each note added will be listed in the list view on the NotesActivity. If the user wants to reset the fields in the NewNotesActivity, they can shake the phone. If the user wishes to back-up the notes stored locally on the phone, the client can press the refresh button in the action bar on the NotesActivity. If the client wishes to delete a note, the user clicks on the note in the list view and a dialog box will ask if they wish to delete it.

Next Version:

Use the picture taken by the camera for Face-Lock login.
Allow the user to batch add notes.
Allow each note to be color coordinated (Black, Blue, etc.)
Allow pictures be added to each of the notes.
