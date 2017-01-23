package andrew_huber.card.game.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import andrew_huber.card.Card;
import andrew_huber.card.game.GoFishGame;
import andrew_huber.card.game.android.R;
import andrew_huber.card.game.computer_game.GoFishComputerGame;
import andrew_huber.card.game.multiplayer_game.GoFishMultiplayerGame;
import andrew_huber.card.player.CardPlayer;
import andrew_huber.exceptions.InvalidArgumentException;

public class GoFishGameplayWindow extends Activity 
	implements GoFishGame.GoFishInterface
{
	/**
	 * Array storing the color values used in the 
	 * {@linkplain #conversationThreadView conversation thread}
	 * @see #findColor(int)
	 */
	private final int COLOR_ARRAY[] = {Color.rgb(0, 201, 87), // Emerald Green
		    						   Color.rgb(65, 105, 225), // Blue
		    						   Color.rgb(148, 0, 211), // Royal Purple
		    						   Color.rgb(238, 64, 0), // Red
		    						   Color.rgb(218, 165, 37), // Gold
		    						   Color.rgb(255, 0, 255), // Fuchsia
		    						   Color.rgb(92, 172, 238), // Steel Blue
		    						   Color.rgb(139, 69, 19)}; // Choc. Brown	
	private final int GO_FISH_SOUND_ID = 1;
	private final int NEW_GAME_SOUND_ID = 2;
	private final int PAIRS_FOUND_SOUND_ID = 3;
	private final int GAME_WON_SOUND_ID = 4;
	private final int CONVERSATION_THREAD_ITEM_SOUND_ID = 5;
	private final int NOT_SELECTED = -1;
	private final String THE_GAME = "The Game";
	private String playerName = null;
	private String[] playerNames = null;
	private int volume;
	private int soundCounter = 0;
	private int otherPairsIndex = NOT_SELECTED;
	private int yourHandIndex = NOT_SELECTED;
	private int yourPairsIndex = NOT_SELECTED;
	private CardPlayer playerSelected = null;
	private HashMap<CardPlayer, Boolean> sortMap = 
		new HashMap<CardPlayer, Boolean>();
	private HashMap<Integer, Integer> soundMap =
		new HashMap<Integer, Integer>();
	private ArrayList<Card> hand = null;
	private int faceValueSelected = NOT_SELECTED;
	private ArrayList<AlertDialog> alertDialogs = 
		new ArrayList<AlertDialog>();
	private ArrayList<Integer> sounds = new ArrayList<Integer>();
	private Throwable caughtException;
	
	private GoFishGame game;
	
	private AssetManager assets;
	private AudioManager audioManager;
	private Button askButton;
	private InputStream stream;
	private LayoutInflater inflater;
	private LinearLayout gameplayWindowVerticalLinearLayout;
	private Menu menu = null;
	private ProgressDialog dialog;
	private RadioGroup otherPairsRadioGroup;
	private RadioGroup yourHandRadioGroup;
	private RadioGroup yourPairsRadioGroup;
	private ScrollView conversationThreadScrollView;
	private SoundPool soundPool;
	private TableLayout conversationThreadTableLayout;
	private boolean resumeFromExit;
	private boolean pauseDialogShowing;
	
	
	//************************************************************************
	// OVERRIDDEN METHODS FROM Activity										 *
	//************************************************************************
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay_window);
        
        System.out.println("In onCreate()");
        
        dialog = new ProgressDialog(this);        
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle("Processing...");
        dialog.setMessage("Processing game sounds...");
    	
    	try
    	{            
    		assets = super.getAssets();
            audioManager = 
            	(AudioManager) 
            	super.getSystemService(Context.AUDIO_SERVICE);
            askButton = (Button) super.findViewById(R.id.askButton);
            inflater = 
            	(LayoutInflater) 
            	super.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gameplayWindowVerticalLinearLayout =
            	(LinearLayout) 
            	super.findViewById(R.id.gameplayWindowVerticalLinearLayout);
            otherPairsRadioGroup = (RadioGroup) 
            	super.findViewById(R.id.otherPairsRadioGroup);
            yourHandRadioGroup = (RadioGroup)
            	super.findViewById(R.id.yourHandRadioGroup);
            yourPairsRadioGroup = (RadioGroup)
            	super.findViewById(R.id.yourPairsRadioGroup);
            conversationThreadScrollView = (ScrollView)
            	super.findViewById(R.id.conversationThreadScrollView);
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            conversationThreadTableLayout =
            	(TableLayout) 
            	super.findViewById(R.id.conversationThreadTableLayout);
            volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            
            super.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            
            soundMap.put(this.CONVERSATION_THREAD_ITEM_SOUND_ID, 
            		soundPool.load(this, 
            				R.raw.new_conversation_thread_item, 1));
            soundMap.put(this.GAME_WON_SOUND_ID, 
            		soundPool.load(this, R.raw.game_won, 1));
            soundMap.put(this.GO_FISH_SOUND_ID, 
            		soundPool.load(this, R.raw.go_fish, 1));
            soundMap.put(this.NEW_GAME_SOUND_ID, 
            		soundPool.load(this, R.raw.new_game, 1));
            soundMap.put(this.PAIRS_FOUND_SOUND_ID, 
            		soundPool.load(this, R.raw.new_pair, 1));
            
            dialog.setMax(soundMap.size());
            dialog.show();
            
            soundPool.setOnLoadCompleteListener(
            		new SoundPool.OnLoadCompleteListener() 
            {
				
				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId, 
						int status) 
				{
					if(soundCounter == soundMap.size() - 1)
					{
						dialog.dismiss();
						play(soundMap.get(NEW_GAME_SOUND_ID));
						
						try
						{
							game.playGame();
						}
						catch (Throwable t)
						{
							handleException(t);
						}
					}
					else
					{
						soundCounter++;
						dialog.setProgress(
								soundCounter / (soundMap.size() - 1));
					}
				}
			});
            
            Bundle extras = super.getIntent().getExtras();
            
            if(extras.containsKey(GoFishMainScreen.PLAYER_NAME_KEY))
            {
            	playerName = extras.getString(GoFishMainScreen.PLAYER_NAME_KEY);
    			game = new GoFishComputerGame(this, playerName);
            }
            else
            {
            	playerNames = 
            		extras.getStringArray(GoFishMainScreen.PLAYER_NAMES_KEY);
            	game = new GoFishMultiplayerGame(this, playerNames);
            }
            
            for(int i = 0; i < game.getNumberOfPlayers(); i++)
            	sortMap.put(game.getPlayer(i), false);
            
            hideAll();
            resumeFromExit = false;
    	}
    	catch (Throwable t)
    	{
    		handleException(t);
    	}
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
    	super.onSaveInstanceState(outState);
    	outState.putAll(outState);
    	resumeFromExit = true;
    }
    
    @Override
    protected void onResume()
    {
    	super.onResume();
    	
    	if(resumeFromExit && !pauseDialogShowing)
    	{
    		pauseGame();
    		resumeFromExit = false;
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	if(this.menu != null)
    		this.menu.clear();
    	
    	this.menu = menu;
    	super.onCreateOptionsMenu(menu);
    	MenuInflater menuInflater = super.getMenuInflater();
    	
    	if(sortMap.get(game.getCurrentPlayer()))
    		menuInflater.inflate(R.menu.gameplay_menu_undo, menu);
    	else
    		menuInflater.inflate(R.menu.gameplay_menu, menu);
    	
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	int itemId = item.getItemId();
    	
    	if(itemId == R.id.pauseItem)
    	{
    		pauseGame();
    	}
    	else if(itemId == R.id.sortItem)
    	{
    		sortMap.remove(game.getCurrentPlayer());
    		sortMap.put(game.getCurrentPlayer(), true);
    		
    		try 
    		{
				refreshGUIComponents();
			} 
    		catch (Throwable t)
    		{
    			handleException(t);
    		}
    	}
    	else if(itemId == R.id.undoSortItem)
    	{
    		sortMap.remove(game.getCurrentPlayer());
    		sortMap.put(game.getCurrentPlayer(), false);
    		
    		try 
    		{
				refreshGUIComponents();
			} 
    		catch (Throwable t)
    		{
    			handleException(t);
    		}
    	}
    	else if(itemId == R.id.restartItem)
    	{
    		confirmRestartGame();
    	}
    	else
    	{
    		confirmGoToMainMenu();
    	}
    	
    	return true;
    }
    
    @Override
    public void onBackPressed()
    {
    	confirmGoToMainMenu();
    }
    
    private void addConversationThreadItem(String sender, String message, int textColor)
    {
    	TableLayout item = 
    		(TableLayout) 
    		inflater.inflate(
    				R.layout.conversation_thread_table_layout_component, null);
    	TextView senderTextView = 
    		(TextView) 
    		item.findViewById(R.id.conversationThreadPlayerNameTextView);
    	TextView messageTextView =
    		(TextView)
    		item.findViewById(R.id.conversationThreadMessageTextView);
    	senderTextView.setText(sender);
    	messageTextView.setText(message);
    	senderTextView.setTextColor(textColor);
    	messageTextView.setTextColor(textColor);
    	conversationThreadTableLayout.addView(item);
    	conversationThreadScrollView.refreshDrawableState();
    	conversationThreadScrollView.post(new Runnable()
    	{

			@Override
			public void run() 
			{
				conversationThreadScrollView.fullScroll(View.FOCUS_DOWN);
			}
    		
    	});
    	
    	play(soundMap.get(CONVERSATION_THREAD_ITEM_SOUND_ID));
    }
    
    private void buildAlertDialog(int iconResource, int soundId, String title, 
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
    	
    	dialog.setOnShowListener(new DialogInterface.OnShowListener() 
    	{	
			@Override
			public void onShow(DialogInterface dialog) 
			{
				if(sounds.get(0) != -1)
					play(sounds.get(0));
				
				sounds.remove(0);
			}
		});
    	
    	dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
		{
			
			@Override
			public void onDismiss(DialogInterface dialog) 
			{
				alertDialogs.remove(0);
				
				System.out.println(alertDialogs.toString());
				System.out.println(sounds.toString());
				
				if(alertDialogs.size() != 0)
				{				
					alertDialogs.get(0).show();
				}
			}
		});
		
    	alertDialogs.add(dialog);
    	sounds.add(soundId);
    	
    	if(alertDialogs.size() == 1)
    	{    		
    		dialog.show();
    	}
    }
    
    private void confirmRestartGame()
    {
    	String title = "Are you sure?";
    	String message = "You will lose all your progress";
    	String positiveButton = "Yes";
    	String negativeButton = "No";
    	
    	buildAlertDialog(android.R.drawable.ic_dialog_alert, -1, title, null, 
    			message, positiveButton, negativeButton,
    			new AlertDialog.OnClickListener()
    			{

					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						restartGame();
					}
    		
    			}, null);
    }
    
    private void confirmGoToMainMenu()
    {
    	String title = "Are you sure?";
    	String message = "You will lose all your progress";
    	String positiveButton = "Yes";
    	String negativeButton = "No";
    	
    	buildAlertDialog(android.R.drawable.ic_dialog_alert, -1, title, null, 
    			message, positiveButton, negativeButton,
    			new AlertDialog.OnClickListener()
    			{

					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						goToMainMenu();
					}
    		
    			}, null);
    }
    
    private ImageView constructCardImageView(Card card)
    {
    	ImageView cardImageView = 
    		(ImageView) inflater.inflate(R.layout.card_image_view, null);
    	String fileName = card.toString();
    	fileName = fileName.replace(" ", "");
    	fileName = fileName.toLowerCase();
    	fileName = "card_" + fileName + ".jpeg";
    	
    	try 
    	{
			stream = assets.open(fileName);
		} 
    	catch (IOException e) 
    	{
			handleException(e);
		}
    	
    	Drawable cardDrawable = Drawable.createFromStream(stream, fileName);
    	cardImageView.setImageDrawable(cardDrawable);
    	return cardImageView;
    }
    
    private ImageView constructSeperatorImageView()
    {
    	ImageView seperator = 
    		(ImageView) inflater.inflate(R.layout.seperator_image_view, null);
    	seperator.getImageMatrix().setRotate(90f);
    	return seperator;
    }
    
    private LinearLayout constructCardLinearLayout(ArrayList<Card> cards)
    {
    	LinearLayout cardTableRow = 
    		(LinearLayout) inflater.inflate(R.layout.card_linear_layout, null);
    	RadioButton radio = (RadioButton) 
    		cardTableRow.findViewById(R.id.cardRadioButton);
    	radio.setOnClickListener(radioButtonListener);
    	
    	for(int i = 0; i < cards.size(); i++)
    		cardTableRow.addView(constructCardImageView(cards.get(i)));
    	
    	return cardTableRow;
    }
    
    private LinearLayout constructCardLinearLayout(Card card)
    {
    	ArrayList<Card> cards = new ArrayList<Card>();
    	cards.add(card);
    	return constructCardLinearLayout(cards);
    }
    
    private TableLayout constructOtherPlayerTableLayoutTableLayoutComponent(
    		CardPlayer player)
    {
    	TableLayout otherPlayerTableRow = 
    		(TableLayout) 
    		inflater.inflate(
    				R.layout.other_pairs_table_layout_component_table_layout, 
    				null);
    	TextView otherPairsPlayerNameTextView = 
    		(TextView) 
    		otherPlayerTableRow.findViewById(R.id.otherPairsPlayerNameTextView);
    	otherPairsPlayerNameTextView.setText(player.getName() + " (Player " + 
    			Integer.toString(player.getPlayerNumber()) + ")");
    	otherPlayerTableRow.addView(addOtherPlayerLinearLayout(player.getPairs()), 
    			new TableRow.LayoutParams(
    					TableRow.LayoutParams.WRAP_CONTENT, 
    					TableRow.LayoutParams.MATCH_PARENT));
    	return otherPlayerTableRow;
    }
    
    private int maintainRadioGroupness(int previouslySelectedRadioButtonPosition, 
    		RadioGroup groupToMaintain)
    {
    	View view;
    	RadioButton radio;
    	int indexFound = previouslySelectedRadioButtonPosition;
    	
    	for(int i = 0; i < groupToMaintain.getChildCount(); i++)
    	{
    		view = groupToMaintain.getChildAt(i);
    		radio = (RadioButton) view.findViewById(R.id.cardRadioButton);
    		
    		if(radio.isChecked() && i != previouslySelectedRadioButtonPosition)
    		{
    			indexFound = i;
    			break;
    		}
    	}
    	
    	deselectAll(groupToMaintain);
    	
    	if(indexFound != NOT_SELECTED)
    	{
    		view = groupToMaintain.getChildAt(indexFound);
    		radio = (RadioButton) view.findViewById(R.id.cardRadioButton);
    		radio.setChecked(true);
    	}
    	
    	return indexFound;
    }
    
    private void deselectAll(RadioGroup group)
    {
    	View view;
    	RadioButton radio;
    	
    	for(int i = 0; i < group.getChildCount(); i++)
    	{
    		view = group.getChildAt(i);
    		radio = (RadioButton) view.findViewById(R.id.cardRadioButton);
    		radio.setChecked(false);
    	}
    }
    
    private void play(int soundId)
    {
    	soundPool.play(soundId, volume, volume, 0, 0, 1f);
    }
    
    private LinearLayout addOtherPlayerLinearLayout(ArrayList<ArrayList<Card>> pairs)
    {
    	LinearLayout cardLinearLayout =
    		(LinearLayout) inflater.inflate(R.layout.card_linear_layout, null);
    	
    	if(pairs.size() == 0)
    	{
    		cardLinearLayout.addView(inflater.inflate(
    				R.layout.no_pairs_text_view, null));
    	}
    	else
    	{
    		for(int r = 0; r < pairs.size(); r++)
        	{
        		for(int c = 0; c < pairs.get(r).size(); c++)
        			cardLinearLayout.addView(constructCardImageView(pairs.get(r).get(c)));
        		
        		if(r != pairs.size() - 1)
        			cardLinearLayout.addView(constructSeperatorImageView());
        	}
    	}
    	
    	RadioButton radio = 
    		(RadioButton) cardLinearLayout.findViewById(R.id.cardRadioButton);
    	radio.setOnClickListener(radioButtonListener);
    	
    	if(game.getNumberOfPlayers() == 2)
    	{    		
    		try
    		{
    			System.out.println("Checking the one and only radio button");
    			radio.setChecked(true);
    			playerSelected = 
    				(game.getCurrentPlayer() == game.getPlayer(0) ? 
    						game.getPlayer(1) : game.getPlayer(0));
    			otherPairsIndex = 0;
    		}
    		catch (Throwable t)
    		{
    			handleException(t);
    		}
    	}
    	
    	return cardLinearLayout;
    }

	private Dialog.OnClickListener nothingListener = 
		new Dialog.OnClickListener()
	{

		@Override
		public void onClick(DialogInterface arg0, int arg1) 
		{
			// Do nothing
		}
		
	};
	
	private View.OnClickListener radioButtonListener = new View.OnClickListener() 
	{
		
		@Override
		public void onClick(View v) 
		{
			System.out.println("Entered radioButtonListener");
			RadioButton radio = (RadioButton) v;
			RadioButton otherRadio;
			TableLayout table;
			LinearLayout linear;
			
			for(int i = 0; i < otherPairsRadioGroup.getChildCount(); i++)
			{
				table = (TableLayout) otherPairsRadioGroup.getChildAt(i);
				otherRadio = (RadioButton) 
					table.findViewById(R.id.cardRadioButton);
				
				if(otherRadio == radio)
				{
					otherPairsIndex = maintainRadioGroupness(otherPairsIndex, 
							otherPairsRadioGroup);
					System.out.println(otherPairsIndex);
					View view = otherPairsRadioGroup.getChildAt(otherPairsIndex);
					TextView textView = 
						(TextView) view.findViewById(
								R.id.otherPairsPlayerNameTextView);
					String string = textView.getText().toString();
					string = string.substring(string.indexOf('('), 
							string.indexOf(')'));
					string = string.substring(string.indexOf(' '));
					string = string.trim();
					int playerNumber = Integer.parseInt(string);
								
					try
					{
						playerSelected = game.getPlayer(playerNumber - 1);
					}
					catch (Throwable t)
					{
						handleException(t);
					}
					
					updateAskButtonStatus();
					return;
				}
			}
			
			for(int i = 0; i < yourHandRadioGroup.getChildCount(); i++)
			{
				linear = (LinearLayout) yourHandRadioGroup.getChildAt(i);
				otherRadio = (RadioButton) linear.findViewById(R.id.cardRadioButton);
				
				if(otherRadio == radio)
				{
					System.out.println("Entered \"yourHandRadioGroup.indexOfChild(radio) != -1\"");
					yourHandIndex = maintainRadioGroupness(yourHandIndex,
						yourHandRadioGroup);
					deselectAll(yourPairsRadioGroup);
					ArrayList<Card> cardsSelected = new ArrayList<Card>();
					cardsSelected.add(hand.get(yourHandIndex));
					GoFishGameplayWindow.this.faceValueSelected = 
						cardsSelected.get(0).getFace();
					yourPairsIndex = NOT_SELECTED;
					updateAskButtonStatus();
					return;
				}
			}
			
			for(int i = 0; i < yourPairsRadioGroup.getChildCount(); i++)
			{
				linear = (LinearLayout) yourPairsRadioGroup.getChildAt(i);
				otherRadio = (RadioButton) linear.findViewById(R.id.cardRadioButton);
				
				if(otherRadio == radio)
				{
					System.out.println("yourPairsRadioGroup.indexOfChild(radio) != -1\"");
					yourPairsIndex = maintainRadioGroupness(yourPairsIndex, 
							yourPairsRadioGroup);
					deselectAll(yourHandRadioGroup);
					
					try
					{
						faceValueSelected = 
							game.getCurrentPlayer().getPair(
									yourPairsIndex).get(0).getFace();
					}
					catch (Throwable t)
					{
						handleException(t);
					}
								
					yourHandIndex = NOT_SELECTED;
					updateAskButtonStatus();
					return;
				}
			}
			
		}
	};    
	
	//************************************************************************
	// UTILITY METHODS														 *
	//************************************************************************
	
	private int findColor(int playerNumber)
	{
		return COLOR_ARRAY[playerNumber - 1];
	}
	
	private void refreshGUIComponents() throws InvalidArgumentException
	{
		otherPairsRadioGroup.removeAllViews();
		yourHandRadioGroup.removeAllViews();
		yourPairsRadioGroup.removeAllViews();
		
		RadioGroup.LayoutParams layoutParams = 
			new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, 
				RadioGroup.LayoutParams.WRAP_CONTENT);

		hand = (sortMap.get(game.getCurrentPlayer()) ? 
				sortHand(game.getCurrentPlayer().getHand()) : 
					game.getCurrentPlayer().getHand());
		
		for(int i = 0; i < game.getNumberOfPlayers(); i++)
		{
			if(game.getPlayer(i) != game.getCurrentPlayer())
			{
				otherPairsRadioGroup.addView(
						constructOtherPlayerTableLayoutTableLayoutComponent(
								game.getPlayer(i)), layoutParams);
			}
		}
		
		for(int i = 0; i < hand.size(); i++)
		{
			yourHandRadioGroup.addView(constructCardLinearLayout(hand.get(i)), 
					layoutParams);
		}
		
		for(int i = 0; i < game.getCurrentPlayer().getPairs().size(); i++)
		{
			yourPairsRadioGroup.addView(constructCardLinearLayout(
					game.getCurrentPlayer().getPairs().get(i)), layoutParams);
		}
		
		askButton.setText(R.string.ask_string);
		askButton.setEnabled(false);
		askButton.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				try
				{
					game.submitGuess(playerSelected, faceValueSelected);
				}
				catch (Throwable t)
				{
					handleException(t);
				}
			}
		});
		
		if(menu != null)
			onCreateOptionsMenu(menu);
	}
	
	private void updateAskButtonStatus()
	{
		String label = super.getString(R.string.ask_string);
		
		if(otherPairsIndex != NOT_SELECTED)
			label += " \"" + playerSelected.getName() + "\"";
		
		if(yourHandIndex != NOT_SELECTED || yourPairsIndex != NOT_SELECTED)
		{
			try
			{
				Card card = new Card(faceValueSelected, Card.SPADES);
				String cardString = card.toString();
				cardString = cardString.substring(0, cardString.indexOf(' ')).trim();
				String message = " for a" + 
					(card.getFace() == Card.ACE || card.getFace() == Card.EIGHT 
						? "n " : " ") + cardString;
				
				if(otherPairsIndex != NOT_SELECTED)
				{
					message = message.substring(1, message.length());
					message = "\n" + message;
				}
				
				label += message;
			}
			catch (Throwable t)
			{
				handleException(t);
			}
		}
		
		askButton.setText(label);
		askButton.setEnabled(otherPairsIndex != NOT_SELECTED && 
				(yourHandIndex != NOT_SELECTED || 
						yourPairsIndex != NOT_SELECTED));
		
		System.out.println("Label = " + label);
		System.out.println("otherPairsIndex = " + otherPairsIndex);
	}
	
	private void goToMainMenu()
	{
		Intent mainMenuIntent = new Intent(GoFishGameplayWindow.this, 
				GoFishMainScreen.class);
		mainMenuIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		super.startActivity(mainMenuIntent);
	}
	
	private void hideAll()
	{
		gameplayWindowVerticalLinearLayout.setVisibility(View.INVISIBLE);
	}
	
	private void pauseGame()
	{
		hideAll();
		String title = "Paused";
		String message = "Tap \"Resume\" to resume the game\n\"" + 
			game.getCurrentPlayer().getName() + "\" is the current player";
		String positiveButton = "Resume";
		pauseDialogShowing = true;
		buildAlertDialog(android.R.drawable.ic_dialog_alert, -1, title, null, 
				message, positiveButton, null, new Dialog.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface arg0, int arg1) 
			{
				showAll();
				pauseDialogShowing = false;
			}
			
		}, null);
	}
	
	private void restartGame()
	{
		// Intent to relaunch the Game
		Intent relaunchIntent;
		
		
		// If the current game is a computer game
		if(playerName != null) 
		{
			try 
			{
				// Construct the Intent
				relaunchIntent = new Intent(
						GoFishGameplayWindow.this, 
						GoFishGameplayWindow.class);
				relaunchIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				relaunchIntent.putExtra(GoFishMainScreen.PLAYER_NAME_KEY, 
						playerName);
				// Restart this Activity using the new GoFishGameplayWindow 
				// object
				super.startActivity(relaunchIntent);
			} 
			catch (Throwable t) 
			{
				handleException(t);
			} 
		}
		// If the current game is a multiplayer game
		else
		{
			try 
			{
				// Construct the Intent
				relaunchIntent = new Intent(
						GoFishGameplayWindow.this, 
						GoFishGameplayWindow.class);
				relaunchIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				relaunchIntent.putExtra(GoFishMainScreen.PLAYER_NAMES_KEY, 
						playerNames);
				// Restart this Activity using the new GoFishGameplayWindow 
				// object
				super.startActivity(relaunchIntent);
			} 
			catch (Throwable t) 
			{
				handleException(t);
			} 
		}
	}
	
	private void showAll()
	{
		gameplayWindowVerticalLinearLayout.setVisibility(View.VISIBLE);
	}

	private ArrayList<Card> sortHand(ArrayList<Card> origHand) 
	{
		ArrayList<Card> hand = new ArrayList<Card>(origHand);
		ArrayList<Card> sortedHand = new ArrayList<Card>();
		Card lowestCard;
		
		while(hand.size() > 0)
		{
			lowestCard = hand.get(0);
			
			for(int i = 0; i < hand.size(); i++)
			{
				if(hand.get(i).compareTo(lowestCard) < 0)
					lowestCard = hand.get(i);
			}
			
			sortedHand.add(lowestCard);
			hand.remove(lowestCard);
			
		}
		
		return sortedHand;
	}
	
	private String stackTraceToString(StackTraceElement[] stackTrace) 
	{
		String stack = "";
		
		for(int i = 0; i < stackTrace.length; i++)
			stack += stackTrace[i].toString() + "\n\n";

		return stack;
	}

	@Override
	public void promptPlayer(CardPlayer playerToBePrompted) 
	{		
		otherPairsIndex = yourHandIndex = yourPairsIndex = NOT_SELECTED;
		
		if(playerName == null)
		{
			hideAll();
			String title = "Switch";
			String message = "Switch to \"" + playerToBePrompted.getName() + 
				"\"";
			String positiveButton = "Done";
			System.out.println("Test");
			buildAlertDialog(android.R.drawable.ic_dialog_alert, -1, title, null, 
					message, positiveButton, null, new Dialog.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					showAll();
				}
			
			}, null);
		}
		else
			showAll();
		
		try 
		{
			refreshGUIComponents();
		} 
		catch (InvalidArgumentException e)
		{
			handleException(e);
		}
		
		String message = playerToBePrompted.getName() + ", it's your turn.";
		System.out.println("Was called");
		addConversationThreadItem(THE_GAME, message, Color.BLACK);
		updateAskButtonStatus();
	}

	@Override
	public void pairsFound(CardPlayer player, ArrayList<ArrayList<Card>> pairs) 
	{
		String title = "Pairs found for player \"" + player.getName() + "\"";
		LinearLayout view = addOtherPlayerLinearLayout(pairs);
		view.removeView(view.findViewById(R.id.cardRadioButton));
		view.setGravity(Gravity.CENTER);
		String positiveButton = "OK";
		buildAlertDialog(android.R.drawable.ic_dialog_info, 
				soundMap.get(this.PAIRS_FOUND_SOUND_ID), title, view, null, 
				positiveButton, null, null, null);
		addConversationThreadItem(THE_GAME, title, Color.BLACK);
	}

	@Override
	public void goFish(CardPlayer playerToFish) throws InvalidArgumentException 
	{
		String cardString = 
			new Card(game.getFailedFaceValue(), Card.SPADES).toString();
		cardString = cardString.substring(0, cardString.indexOf(' '));
		String title = "Go Fish!";
		String message = playerToFish.getName() + ", no " + cardString + 
			" was found. Go fish!";
		String positiveButton = "Go fish";
		buildAlertDialog(android.R.drawable.ic_dialog_info,
				soundMap.get(this.GO_FISH_SOUND_ID), title, null, 
				message, positiveButton, null, null, null);
		addConversationThreadItem(THE_GAME, message, Color.BLACK);
	}

	@Override
	public void declareWinner(CardPlayer winner) 
	{
		String title = "We have a winner!";
		String message = winner.getName() + ", you are the winner!";
		String positiveButton = "Play again?";
		String negativeButton = "Main menu?";
		buildAlertDialog(android.R.drawable.ic_dialog_info, 
				soundMap.get(this.GAME_WON_SOUND_ID), title, null, 
				message, positiveButton, negativeButton, 
				new Dialog.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				restartGame();
			}
			
		}, new Dialog.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				goToMainMenu();
			}
			
		});
	}

	@Override
	public void handleException(Throwable t) 
	{
		caughtException = t;
		t.printStackTrace();
		String title = "Whopps! An error was encountered: " + 
			t.getClass().getName();
		String message = t.getMessage() + "\n\n" + 
			stackTraceToString(t.getStackTrace());
		String positiveButton = "Send to developer";
		buildAlertDialog(android.R.drawable.ic_dialog_alert, 
				soundMap.get(GO_FISH_SOUND_ID), title, null, 
				message, positiveButton, null, new Dialog.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface arg0, int arg1) 
				{
					Intent emailIntent = 
						new Intent(android.content.Intent.ACTION_SEND);
					emailIntent.setType("text/plain");
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, 
							GoFishGameplayWindow.super.getResources().
							getString(R.string.app_name) + " - " + 
								caughtException.getClass().getName());
					emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
							caughtException.getMessage() + "\n\n" + 
							stackTraceToString(
									caughtException.getStackTrace()));
					emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, 
							GoFishGameplayWindow.super.getResources().
							getStringArray(R.array.email_addresses));
					startActivity(Intent.createChooser(emailIntent, 
							"Error: Send to Developer"));
					System.exit(0);
				}
				
			}, null);
	}

	@Override
	public void noMoreCardsInDeck() 
	{
		play(soundMap.get(GO_FISH_SOUND_ID));
		String title = "No more cards in deck!";
		String message = "There are no more cards in the deck!";
		String positiveButton = "Restart game?";
		String negativeButton = "Main menu?";
		buildAlertDialog(android.R.drawable.ic_dialog_alert, 
				soundMap.get(GO_FISH_SOUND_ID), 
				title, null, 
				message, positiveButton, negativeButton, 
				new Dialog.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				restartGame();
			}
			
		}, new Dialog.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				goToMainMenu();
			}
			
		});
	}

	@Override
	public void displayQuestion(CardPlayer playerToAsk, int faceValue)
			throws InvalidArgumentException 
	{
		String sender = game.getCurrentPlayer().getName();
		Card card = new Card(faceValue, Card.SPADES);
		String cardString = card.toString();
		cardString = cardString.substring(0, cardString.indexOf(' ')).trim();
		String message = playerToAsk.getName() + ", do you have a" + 
			(card.getFace() == Card.ACE || card.getFace() == Card.EIGHT ? 
					"n " : " ") + cardString + "?";
		addConversationThreadItem(sender, message, 
				findColor(game.getCurrentPlayer().getPlayerNumber()));
	}
}
