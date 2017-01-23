package andrew_huber.card.game.android;

import andrew_huber.card.player.GroupOfCardPlayers;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class GoFishMainScreen extends Activity 
{
	public static final String PLAYER_NAME_KEY = "PLAYER_NAME_KEY";
	public static final String PLAYER_NAMES_KEY = "PLAYER_NAMES_KEY";
	private final int PLAYER_LIMIT = 5;
	private ScrollView mainScreenScrollView;
	private EditText mainPlayerNameEditText;
	private RadioButton computerGameRadioButton;
	private RadioGroup mainScreenRadioGroup;
	private TableLayout mainScreenTableLayout2;
	private Button addPlayerButton;
	private Button startGameButton;
	private Throwable caughtException;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        
        mainScreenScrollView =
        	(ScrollView) super.findViewById(R.id.mainScreenScrollView);
        mainPlayerNameEditText =
        	(EditText) super.findViewById(R.id.mainPlayerNameEditText);
        mainScreenRadioGroup = 
        	(RadioGroup) super.findViewById(R.id.mainScreenRadioGroup);
        computerGameRadioButton = 
        	(RadioButton) mainScreenRadioGroup.getChildAt(0);
        mainScreenTableLayout2 =
        	(TableLayout) super.findViewById(R.id.mainScreenTableLayout2);
        addPlayerButton =
        	(Button) super.findViewById(R.id.addPlayerButton);
        startGameButton = 
        	(Button) super.findViewById(R.id.startGameButton);
        
        mainPlayerNameEditText.addTextChangedListener(typedListener);
        mainScreenRadioGroup.setOnCheckedChangeListener(radioGroupListener);
        addPlayerButton.setOnClickListener(addListener);
        startGameButton.setOnClickListener(startListener);
        updateStartButton();
        addPlayerButton.setEnabled(false);
    }
    
    /** Called when the app is exited 
     * @param outState The state of the app on time of exit
     */
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
    	super.onSaveInstanceState(outState);
    	outState.putAll(outState);
    }
    
    @Override
    public void onBackPressed()
    {
    	return;
    }
    
    private void addRow()
    {
    	LayoutInflater inflater = 
        	(LayoutInflater) 
        	super.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	TableRow playerNameTableRow = 
    		(TableRow) inflater.inflate(R.layout.player_name_row, null);
    	EditText playerNameEditText =
    		(EditText) playerNameTableRow.getChildAt(0);
    	Button deletePlayerButton =
    		(Button) playerNameTableRow.getChildAt(1);
    	playerNameEditText.addTextChangedListener(typedListener);
    	deletePlayerButton.setOnClickListener(deleteListener);  	
    	mainScreenTableLayout2.addView(playerNameTableRow);
    	updateStartButton();
    	updateDeleteButtonStatus();
    	mainScreenScrollView.post(scrollRunnable);
    }
    
    /**
	 * Builds an AlertDialog
	 * @param title The title to display in the AlertDialog
	 * @param content If one would like a custom View to be displayed in the 
	 * AlertDialog
	 * @param message If one would not like a custom View to be displayed in
	 * the AlertDialog, a message can be suppled
	 * @param positiveButtonLabel The label to put on the positive button
	 * @param negativeButtonLabel The label to put on the negative button
	 * @param positiveButtonListener The listener associated with the positive
	 * button
	 * @param negativeButtonListener The listener associated with the negative
	 * button
	 */
	private void buildAlertDialog(int iconResource, String title, 
    		View content, String message, String positiveButtonLabel, 
    		String negativeButtonLabel, 
    		Dialog.OnClickListener positiveButtonListener,
    		Dialog.OnClickListener negativeButtonListener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
    	if(iconResource != -1)
    		builder.setIcon(iconResource);
    	
    	if(title != null)
    		builder.setTitle(title);
    	
    	if(content != null)
    		builder.setView(content);
    	
    	if(message != null)
    		builder.setMessage(message);
    	
    	if(positiveButtonListener == null)
    		positiveButtonListener = nothingListener;
    	
    	if(negativeButtonListener == null)
    		negativeButtonListener = nothingListener;
    	
    	if(positiveButtonLabel != null)
    		builder.setPositiveButton(positiveButtonLabel, 
    				positiveButtonListener);
    	
    	if(negativeButtonLabel != null)
    		builder.setNegativeButton(negativeButtonLabel, 
    				negativeButtonListener);
    	
    	AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private String[] extractNames()
	{
		TableRow currentTableRow;
		EditText currentEditText;
		String names[] = 
			new String[mainScreenTableLayout2.getChildCount() + 1];
		names[0] = mainPlayerNameEditText.getText().toString();
		
		for(int i = 1; i < names.length; i++)
		{
			currentTableRow = 
				(TableRow) mainScreenTableLayout2.getChildAt(i - 1);
			currentEditText =
				(EditText) currentTableRow.getChildAt(0);
			names[i] = currentEditText.getText().toString();
		}
		
		return names;
	}
    
	public void handleException(Throwable t) 
	{
		caughtException = t;
		t.printStackTrace();
		String title = "Whopps! An error was encountered: " + 
			t.getClass().getName();
		String message = t.getMessage() + "\n\n" + 
			stackTraceToString(t.getStackTrace());
		String positiveButton = "Send to developer";
		buildAlertDialog(android.R.drawable.ic_dialog_alert, title, null, 
				message, positiveButton, null, new Dialog.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface arg0, int arg1) 
				{
					Intent emailIntent = 
						new Intent(android.content.Intent.ACTION_SEND);
					emailIntent.setType("text/plain");
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, 
							GoFishMainScreen.super.getResources().
							getString(R.string.app_name) + " - " + 
								caughtException.getClass().getName());
					emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
							caughtException.getMessage() + "\n\n" + 
							stackTraceToString(
									caughtException.getStackTrace()));
					emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, 
							GoFishMainScreen.super.getResources().
							getStringArray(R.array.email_addresses));
					startActivity(Intent.createChooser(emailIntent, 
							"Error: Send to Developer"));
					System.exit(0);
				}
				
			}, null);
	}
    
    private void removeAllRows()
    {
    	mainScreenTableLayout2.removeAllViews();
    }
    
    /**
	 * Gets the stack trace of a {@link Throwable}
	 * @param stackTrace The stack trace of a {@link Throwable}
	 * @return A String representation of the stack trace
	 */
	private String stackTraceToString(StackTraceElement[] stackTrace) 
	{
		// String that will contain a String representation of the stack trace
		String stack = "";
		
		// For each StackTraceElement
		for(int i = 0; i < stackTrace.length; i++)
			// Append the String representation of the stackTraceElement to 
			// stack in addition to two new line characters
			stack += stackTrace[i].toString() + "\n\n";
		
		// Return the String representation of the stack trace
		return stack;
	}
	
	private void updateAddRowButtonStatus()
	{
		if(mainScreenTableLayout2.getChildCount() >= PLAYER_LIMIT - 1)
			addPlayerButton.setEnabled(false);
		else
			addPlayerButton.setEnabled(true);
	}
	
	private void updateDeleteButtonStatus()
	{
		TableRow otherTableRow;
		Button otherButton;
		
		if(mainScreenTableLayout2.getChildCount() == 1)
		{
    		otherTableRow = (TableRow) mainScreenTableLayout2.getChildAt(0);
    		otherButton = (Button) otherTableRow.getChildAt(1);
    		otherButton.setEnabled(false);
		}
    	else if(mainScreenTableLayout2.getChildCount() > 1)
    	{    		
    		for(int i = 0; i < mainScreenTableLayout2.getChildCount(); i++)
    		{
    			otherTableRow = (TableRow) mainScreenTableLayout2.getChildAt(0);
    			otherButton = (Button) otherTableRow.getChildAt(1);
    			otherButton.setEnabled(true);
    		}
    	}
	}
    
    private void updateStartButton()
    {
    	if(computerGameRadioButton.isChecked())
    	{
			startGameButton.setEnabled(
					GroupOfCardPlayers.playerNameIsValid(
							mainPlayerNameEditText.getText().toString()));
    	}
		else
		{			
			if(!GroupOfCardPlayers.playerNameIsValid(
					mainPlayerNameEditText.getText().toString()))
			{
				startGameButton.setEnabled(false);
			}
			else if(!GroupOfCardPlayers.playerNamesAreValid(extractNames()))
				startGameButton.setEnabled(false);
			else
				startGameButton.setEnabled(true);
		}
    }
    
    private View.OnClickListener addListener = new View.OnClickListener()
    {	
		@Override
		public void onClick(View v) 
		{
			addRow();
			updateAddRowButtonStatus();
			mainScreenScrollView.post(scrollRunnable);
		}
	};
    
    private View.OnClickListener deleteListener = new View.OnClickListener()
    {
		@Override
		public void onClick(View v) 
		{
			TableRow currentTableRow;
			Button currentButton;
			
			for(int i = 0; i < mainScreenTableLayout2.getChildCount(); i++)
			{
				currentTableRow = 
					(TableRow) mainScreenTableLayout2.getChildAt(i);
				currentButton = (Button) currentTableRow.getChildAt(1);
				
				if(currentButton.isPressed())
				{
					mainScreenTableLayout2.removeViewAt(i);
					updateStartButton();
					
					if(mainScreenTableLayout2.getChildCount() == 0)
					{
						currentTableRow =
							(TableRow) mainScreenTableLayout2.getChildAt(0);
						currentButton = (Button) currentTableRow.getChildAt(1);
						currentButton.setEnabled(false);
						updateDeleteButtonStatus();
						break;
					}
				}
				else
					currentButton.setEnabled(true);
			}
			
			updateDeleteButtonStatus();
			updateAddRowButtonStatus();
			scrollRunnable.run();
		}
	};
    
    private RadioGroup.OnCheckedChangeListener radioGroupListener = 
    	new RadioGroup.OnCheckedChangeListener() 
    {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				if(computerGameRadioButton.isChecked())
				{
					removeAllRows();
					addPlayerButton.setEnabled(false);
				}
				else
				{
					addRow();
					addPlayerButton.setEnabled(true);
				}
				
				updateStartButton();
			}
		};
	
	private View.OnClickListener startListener = new View.OnClickListener()
	{
		
		@Override
		public void onClick(View v) 
		{
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mainPlayerNameEditText.getWindowToken(), 0);
			
			if(computerGameRadioButton.isChecked())
			{
				try
				{
					// Construct the Intent
					Intent startIntent = new Intent(GoFishMainScreen.this, 
							GoFishGameplayWindow.class);
					startIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startIntent.putExtra(PLAYER_NAME_KEY, 
							mainPlayerNameEditText.getText().toString());
					// Restart this Activity using the new GoFishGameplayWindow 
					// object
					GoFishMainScreen.super.startActivity(startIntent);
				}
				catch (Throwable t)
				{
					handleException(t);
				}
			}
			else
			{
				String names[] = extractNames();
				
				try
				{
					// Construct the Intent
					Intent startIntent = new Intent(GoFishMainScreen.this, 
							GoFishGameplayWindow.class);
					startIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startIntent.putExtra(PLAYER_NAMES_KEY, names);
					// Restart this Activity using the new GoFishGameplayWindow 
					// object
					GoFishMainScreen.super.startActivity(startIntent);
				}
				catch (Throwable t)
				{
					handleException(t);
				}
			}
		}
	};
    
    private TextWatcher typedListener = new TextWatcher()
    {

		@Override
		public void afterTextChanged(Editable s) 
		{
			// Do nothing
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) 
		{
			// Do nothing
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) 
		{
			updateStartButton();
		}
    	
	};
	
	Runnable scrollRunnable = new Runnable()
	{
		public void run()
		{
			mainScreenScrollView.fullScroll(View.FOCUS_DOWN);
		}
	};
	
	private Dialog.OnClickListener nothingListener = 
		new Dialog.OnClickListener()
	{

		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			// Do nothing
		}
		
	};
}