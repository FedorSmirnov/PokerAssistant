package com.FedorApps.pokerassistant;

import com.FedorApps.pokerassistant.cardCombos.CombinationCalculator;
import com.FedorApps.pokerassistant.cardCombos.MonteCarloOddCalculator;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;

public class ProbCalc extends Activity {

	// Variablen für den Zugriff auf den Service

	// Messenger, um dem Service was zu sagen.
	Messenger messenger = null;
	boolean serviceBound = false;
	private static final String oddKey = "Key_For_The_Odds";

	// Messenger, um vom Service angesprochen zu werden
	Messenger messengerToActivity = new Messenger(new IncomingHandler());

	static final int MSG_RESULT = 10000;

	// Keys der Activity
	private static final int CARD_REQUEST_H1 = 10;
	private static final int CARD_REQUEST_H2 = 20;
	private static final int CARD_REQUEST_F1 = 30;
	private static final int CARD_REQUEST_F2 = 40;
	private static final int CARD_REQUEST_F3 = 50;
	private static final int CARD_REQUEST_T = 60;
	private static final int CARD_REQUEST_R = 70;

	// Zustandsvariablen der Klasse

	// 1=> keine Haand; 2=>flop; 3 => turn; 4=> river
	private int state = 1;

	// Objekte der Activity
	private HandCards hand;
	private Flop flop;
	private Turn turn;
	private River river;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prob_calc);
		// Show the Up button in the action bar.
		setupActionBar();
		hand = new HandCards();
		flop = new Flop();
		turn = new Turn();
		river = new River();

		resetCards();

		Button hand1 = (Button) findViewById(R.id.probcalc_button_hand1);
		hand1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getCardFromUser(CARD_REQUEST_H1, 1);

			}
		});

		Button hand2 = (Button) findViewById(R.id.probcalc_button_hand2);
		hand2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getCardFromUser(CARD_REQUEST_H2, 2);

			}
		});

		Button flop1 = (Button) findViewById(R.id.probcalc_button_flop1);
		flop1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getCardFromUser(CARD_REQUEST_F3, 5);

			}
		});

		Button flop2 = (Button) findViewById(R.id.probcalc_button_flop2);
		flop2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getCardFromUser(CARD_REQUEST_F2, 4);

			}
		});

		Button flop3 = (Button) findViewById(R.id.probcalc_button_flop3);
		flop3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getCardFromUser(CARD_REQUEST_F1, 3);

			}
		});

		Button turn = (Button) findViewById(R.id.probcalc_button_turn1);
		turn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getCardFromUser(CARD_REQUEST_T, 6);

			}
		});

		Button river = (Button) findViewById(R.id.probcalc_button_river1);
		river.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getCardFromUser(CARD_REQUEST_R, 7);

			}
		});
	}

	@Override
	protected void onStart() {

		super.onStart();

		// Für das Binden des Services an die Activity

		Intent intent = new Intent(this, OddCalculatorService.class);
		bindService(intent, calcConnection, Context.BIND_AUTO_CREATE);
		Toast.makeText(this, "OnStart", Toast.LENGTH_SHORT).show();

	}

	@Override
	protected void onStop() {

		super.onStop();

		if (serviceBound) {

			unbindService(calcConnection);
			serviceBound = false;

		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		if (state == 1) {
			getMenuInflater().inflate(R.menu.prob_calc, menu);
		} else if (state == 2) {
			getMenuInflater().inflate(R.menu.prob_calc_flop, menu);
		} else if (state == 3) {
			getMenuInflater().inflate(R.menu.prob_calc_turn, menu);
		} else if (state == 4) {
			getMenuInflater().inflate(R.menu.prob_calc_river, menu);
		} else if (state == 5) {
			getMenuInflater().inflate(R.menu.prob_calc_finish, menu);
		}
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayShowTitleEnabled(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {

			NavUtils.navigateUpFromSameTask(this);
			return true;
		} else if (item.getItemId() == R.id.newCalc_menu) {

			hand = new HandCards();
			dealHand();
			state = 2;
			invalidateOptionsMenu();
			return true;

		} else if (item.getItemId() == R.id.flop) {

			dealFlop();
			state = 3;
			invalidateOptionsMenu();

			return true;
		} else if (item.getItemId() == R.id.refresh) {

			resetCards();
			state = 1;
			invalidateOptionsMenu();
			return true;
		} else if (item.getItemId() == R.id.turn) {

			dealTurn();
			state = 4;
			invalidateOptionsMenu();

			return true;
		} else if (item.getItemId() == R.id.river) {

			dealRiver();
			state = 5;
			invalidateOptionsMenu();
			return true;

		} else if (item.getItemId() == R.id.question) {

			CombinationCalculator combCalc1 = new CombinationCalculator(hand,
					flop, turn, river);

			Toast.makeText(getApplicationContext(), combCalc1.toString(),
					Toast.LENGTH_SHORT).show();

			return true;

		}

		else if (item.getItemId() == R.id.monte_carlo) {

			EditText oppoNumtext = (EditText) findViewById(R.id.probcalc_edittext_oppo_num);

			String input = oppoNumtext.getText().toString();

			int oppoNum = 0;

			if (input.equals("")) {
				Toast.makeText(getApplicationContext(),
						"Please enter the number of the opponents.",
						Toast.LENGTH_SHORT).show();
				return false;
			} else {

				oppoNum = Integer.valueOf(input);

				if (oppoNum > 11) {
					Toast.makeText(getApplicationContext(),
							"Simulation with 11 opponents.", Toast.LENGTH_SHORT)
							.show();
					oppoNum = 11;
				}

			}

			MonteCarloOddCalculator monty = new MonteCarloOddCalculator(hand,
					oppoNum, flop, turn, river, 5000);

			String text = "Monte Carlo: " + monty.getOdd();

			Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
					.show();

			return true;

		} else {
			return super.onOptionsItemSelected(item);
		}

	}

	private void dealHand() {

		Button hand1 = (Button) findViewById(R.id.probcalc_button_hand1);
		hand1.setEnabled(true);

		Button hand2 = (Button) findViewById(R.id.probcalc_button_hand2);
		hand2.setEnabled(true);

		getCardFromUser(CARD_REQUEST_H1, 1);

		getCardFromUser(CARD_REQUEST_H2, 2);

	}

	private void dealFlop() {

		Button flop1 = (Button) findViewById(R.id.probcalc_button_flop1);
		flop1.setEnabled(true);

		Button flop2 = (Button) findViewById(R.id.probcalc_button_flop2);
		flop2.setEnabled(true);

		Button flop3 = (Button) findViewById(R.id.probcalc_button_flop3);
		flop3.setEnabled(true);

		getCardFromUser(CARD_REQUEST_F1, 5);

		getCardFromUser(CARD_REQUEST_F2, 4);

		getCardFromUser(CARD_REQUEST_F3, 3);

	}

	private void dealTurn() {

		Button turn1 = (Button) findViewById(R.id.probcalc_button_turn1);
		turn1.setEnabled(true);

		getCardFromUser(CARD_REQUEST_T, 6);

	}

	private void dealRiver() {

		Button river1 = (Button) findViewById(R.id.probcalc_button_river1);
		river1.setEnabled(true);

		getCardFromUser(CARD_REQUEST_R, 7);

	}

	private void getCardFromUser(int request, int setCardNum) {

		int cardNum;

		switch (request) {
		case CARD_REQUEST_H1:
			cardNum = 1;
			break;
		case CARD_REQUEST_H2:
			cardNum = 2;
			break;
		case CARD_REQUEST_F1:
			cardNum = 3;
			break;
		case CARD_REQUEST_F2:
			cardNum = 3;
			break;
		case CARD_REQUEST_F3:
			cardNum = 3;
			break;
		case CARD_REQUEST_T:
			cardNum = 4;
			break;
		case CARD_REQUEST_R:
			cardNum = 5;
			break;
		default:
			cardNum = 0;

		}

		Intent intent = new Intent(this, CardPick.class);
		intent.putExtra(PreferenceContainer.keyCardNum, cardNum);

		intent.putExtra(PreferenceContainer.keySetCardNum, setCardNum);

		startActivityForResult(intent, request);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == CARD_REQUEST_H1 && resultCode == RESULT_OK) {

			int color = data.getExtras().getInt(PreferenceContainer.keyColor);
			int value = data.getExtras().getInt(PreferenceContainer.keyValue);

			hand.setCard1(new Card(color, value, this));

			updateDisplay();
		}

		if (requestCode == CARD_REQUEST_H2 && resultCode == RESULT_OK) {

			int color = data.getExtras().getInt(PreferenceContainer.keyColor);
			int value = data.getExtras().getInt(PreferenceContainer.keyValue);

			hand.setCard2(new Card(color, value, this));

			updateDisplay();
		}

		if (requestCode == CARD_REQUEST_F1 && resultCode == RESULT_OK) {

			int color = data.getExtras().getInt(PreferenceContainer.keyColor);
			int value = data.getExtras().getInt(PreferenceContainer.keyValue);

			flop.setCard1(new Card(color, value, this));

			updateDisplay();
		}

		if (requestCode == CARD_REQUEST_F2 && resultCode == RESULT_OK) {

			int color = data.getExtras().getInt(PreferenceContainer.keyColor);
			int value = data.getExtras().getInt(PreferenceContainer.keyValue);

			flop.setCard2(new Card(color, value, this));

			updateDisplay();
		}

		if (requestCode == CARD_REQUEST_F3 && resultCode == RESULT_OK) {

			int color = data.getExtras().getInt(PreferenceContainer.keyColor);
			int value = data.getExtras().getInt(PreferenceContainer.keyValue);

			flop.setCard3(new Card(color, value, this));

			updateDisplay();
		}

		if (requestCode == CARD_REQUEST_T && resultCode == RESULT_OK) {

			int color = data.getExtras().getInt(PreferenceContainer.keyColor);
			int value = data.getExtras().getInt(PreferenceContainer.keyValue);

			turn.dealCard(new Card(color, value, this));

			updateDisplay();
		}

		if (requestCode == CARD_REQUEST_R && resultCode == RESULT_OK) {

			int color = data.getExtras().getInt(PreferenceContainer.keyColor);
			int value = data.getExtras().getInt(PreferenceContainer.keyValue);

			river.dealCard(new Card(color, value, this));

			updateDisplay();
		}

		callCalc();

	}

	// Hier noch das restliche Updaten ergänzen
	public void updateDisplay() {

		// Hand
		if (hand.getCard1() != null) {

			Button hand1 = (Button) findViewById(R.id.probcalc_button_hand1);
			setButtonColor(hand1, hand.getCard1().getColor());
			setButtonText(hand1, hand.getCard1());

		} else if (hand.getCard2() == null && hand.getCard1() == null) {

			// Reset aller Eingaben

			Button hand1 = (Button) findViewById(R.id.probcalc_button_hand1);
			hand1.setText("");
			hand1.setBackgroundResource(R.drawable.button_card_in_game_back);

			hand1.setEnabled(false);

			Button hand2 = (Button) findViewById(R.id.probcalc_button_hand2);
			hand2.setText("");
			hand2.setBackgroundResource(R.drawable.button_card_in_game_back);

			hand2.setEnabled(false);

			Button flop1 = (Button) findViewById(R.id.probcalc_button_flop1);
			flop1.setText("");
			flop1.setBackgroundResource(R.drawable.button_card_in_game_back);
			flop1.setEnabled(false);

			Button flop2 = (Button) findViewById(R.id.probcalc_button_flop2);
			flop2.setText("");
			flop2.setBackgroundResource(R.drawable.button_card_in_game_back);
			flop2.setEnabled(false);

			Button flop3 = (Button) findViewById(R.id.probcalc_button_flop3);
			flop3.setText("");
			flop3.setBackgroundResource(R.drawable.button_card_in_game_back);
			flop3.setEnabled(false);

			Button turn1 = (Button) findViewById(R.id.probcalc_button_turn1);
			turn1.setText("");
			turn1.setBackgroundResource(R.drawable.button_card_in_game_back);
			turn1.setEnabled(false);

			Button river1 = (Button) findViewById(R.id.probcalc_button_river1);
			river1.setText("");
			river1.setBackgroundResource(R.drawable.button_card_in_game_back);
			river1.setEnabled(false);

			return;

		}

		if (hand.getCard2() != null) {

			Button hand2 = (Button) findViewById(R.id.probcalc_button_hand2);
			setButtonColor(hand2, hand.getCard2().getColor());
			setButtonText(hand2, hand.getCard2());

		}

		if (flop.getCard1() != null) {
			Button flop1 = (Button) findViewById(R.id.probcalc_button_flop3);
			setButtonColor(flop1, flop.getCard1().getColor());
			setButtonText(flop1, flop.getCard1());
		}

		if (flop.getCard2() != null) {
			Button flop2 = (Button) findViewById(R.id.probcalc_button_flop2);
			setButtonColor(flop2, flop.getCard2().getColor());
			setButtonText(flop2, flop.getCard2());
		}

		if (flop.getCard3() != null) {
			Button flop3 = (Button) findViewById(R.id.probcalc_button_flop1);
			setButtonColor(flop3, flop.getCard3().getColor());
			setButtonText(flop3, flop.getCard3());
		}

		if (turn.getCard() != null) {
			Button turn1 = (Button) findViewById(R.id.probcalc_button_turn1);
			setButtonColor(turn1, turn.getCard().getColor());
			setButtonText(turn1, turn.getCard());
		}

		if (river.getCard() != null) {
			Button river1 = (Button) findViewById(R.id.probcalc_button_river1);
			setButtonColor(river1, river.getCard().getColor());
			setButtonText(river1, river.getCard());
		}

	}

	private void resetCards() {
		hand.setCard1(null);
		hand.setCard2(null);
		flop.setCard1(null);
		flop.setCard2(null);
		flop.setCard3(null);
		turn.dealCard(null);
		river.dealCard(null);

		SharedPreferences pref = getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = pref.edit();

		for (int i = 1; i < 8; i++) {

			String keyColor = getKeyStringColor(i);
			String keyValue = getKeyStringValue(i);

			editor.putInt(keyColor, 0);
			editor.putInt(keyValue, 0);
		}

		editor.commit();

		updateDisplay();
	}

	private void setButtonColor(Button button, int color) {

		switch (color) {

		case 1:
			button.setBackgroundResource(R.drawable.button_card_in_game_spade);

			break;

		case 2:
			button.setBackgroundResource(R.drawable.button_card_in_game_heart);
			break;

		case 3:
			button.setBackgroundResource(R.drawable.button_card_in_game_diamond);
			break;

		default:
			button.setBackgroundResource(R.drawable.button_card_in_game_club);

		}

	}

	private void setButtonText(Button button, Card card) {

		button.setTextColor(getResources().getColor(R.color.WHITE));

		button.setText(card.getShortValueString());
	}

	private String getKeyStringColor(int i) {

		switch (i) {
		case (1):
			return PreferenceContainer.keyColorFirst;

		case (2):
			return PreferenceContainer.keyColorSecond;

		case (3):
			return PreferenceContainer.keyColorThird;

		case (4):
			return PreferenceContainer.keyColorFourth;

		case (5):
			return PreferenceContainer.keyColorFifth;
		case (6):
			return PreferenceContainer.keyColorSixth;
		case (7):
			return PreferenceContainer.keyColorSeventh;
		default:
			return PreferenceContainer.keyColorEighth;
		}

	}

	private String getKeyStringValue(int i) {

		switch (i) {
		case (1):
			return PreferenceContainer.keyValueFirst;

		case (2):
			return PreferenceContainer.keyValueSecond;

		case (3):
			return PreferenceContainer.keyValueThird;

		case (4):
			return PreferenceContainer.keyValueFourth;

		case (5):
			return PreferenceContainer.keyValueFifth;
		case (6):
			return PreferenceContainer.keyValueSixth;
		case (7):
			return PreferenceContainer.keyValueSeventh;
		default:
			return PreferenceContainer.keyValueEighth;
		}

	}

	private int loadInt(String key) {

		SharedPreferences pref = this.getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);

		int result = pref.getInt(key, 0);
		return result;

	}

	@Override
	public void finish() {
		resetCards();
		super.finish();
	}

	// WICHTIG: Interface für die ServiceConnection

	private ServiceConnection calcConnection = new ServiceConnection() {

		// Wird ausgeführt, wenn der Service wieder abgetrennt wird
		@Override
		public void onServiceDisconnected(ComponentName name) {
			serviceBound = false;
			messenger = null;

		}

		// Wenn der Service verbunden ist, bekommt man einen Binder, über den
		// man mit dem Service kommunizieren kann
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {

			messenger = new Messenger(service);
			Toast.makeText(getApplicationContext(), "Connected",
					Toast.LENGTH_SHORT).show();

			try {
				Message msg = Message.obtain(null,
						OddCalculatorService.MSG_ADD_MESSENGER);
				msg.replyTo = messengerToActivity;
				messenger.send(msg);

			} catch (RemoteException e) {
				e.printStackTrace();
			}

			serviceBound = true;
		}
	};

	class IncomingHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case MSG_RESULT:
				int odd = msg.getData().getInt(oddKey);

				TextView text = (TextView) findViewById(R.id.textViewTimeTest);
				text.setText(String.valueOf(odd));

				Message response = Message.obtain(null,
						OddCalculatorService.MSG_KEEP_CALC);
				try {
					messenger.send(response);
				} catch (RemoteException e) {
					e.printStackTrace();
				}

				break;

			default:
				super.handleMessage(msg);
			}
		}

	}

	private void callCalc() {
		if (!serviceBound) {
			return;
		}

		Message msg = Message.obtain(null, OddCalculatorService.MSG_START_CALC,
				0, 0);
		Bundle data = new Bundle();

		// Alle 7 KARTEN werden als Integer ins Bündel gepackt.

		for (int i = 1; i < 8; i++) {

			String colorKey = getKeyStringColor(i);
			String valueKey = getKeyStringValue(i);

			int color = loadInt(colorKey);
			int value = loadInt(valueKey);

			if (color != 0) {
				data.putInt(colorKey, color);
				data.putInt(valueKey, value);
			} else {
				data.putInt(colorKey, 0);
				data.putInt(valueKey, 0);
			}

		}

		EditText oppo = (EditText) findViewById(R.id.probcalc_edittext_oppo_num);
		String text = oppo.getText().toString();

		int oppoNum = Integer.valueOf(text);

		data.putInt(PreferenceContainer.keyOppoNum, oppoNum);

		msg.setData(data);

		try {
			messenger.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
