package com.FedorApps.pokerassistant.pokerSession;

import com.FedorApps.pokerassistant.Card;
import com.FedorApps.pokerassistant.CardPick;
import com.FedorApps.pokerassistant.Flop;
import com.FedorApps.pokerassistant.HandCards;
import com.FedorApps.pokerassistant.OddCalculatorService;
import com.FedorApps.pokerassistant.PreferenceContainer;
import com.FedorApps.pokerassistant.R;
import com.FedorApps.pokerassistant.River;
import com.FedorApps.pokerassistant.Turn;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PokerSession extends Activity {

	// Key für die "Neuschaffungvariable"

	public static final String KEY_NEW_BOOL = "new_poker_session";

	// Requests für die Kommunikation mit den anderen Activities
	private static final int CARD_REQUEST_H1 = 10;
	private static final int CARD_REQUEST_H2 = 20;
	private static final int CARD_REQUEST_F1 = 30;
	private static final int CARD_REQUEST_F2 = 40;
	private static final int CARD_REQUEST_F3 = 50;
	private static final int CARD_REQUEST_T = 60;
	private static final int CARD_REQUEST_R = 70;

	private static final int BET_REQUEST = 100;

	private static final int MENU_REQUEST = 200;

	private static final int MENU_PLAYER_OUT_REQUEST = 300;

	private static final int MENU_PLAYER_IN_REQUEST = 400;

	private static final int BLIND_ADJUST_REQUEST = 2000;

	// Variablen für den Zugriff auf den Service

	// Messenger, um dem Service was zu sagen.
	Messenger messenger = null;
	boolean serviceBound = false;

	// Messenger, um vom Service angesprochen zu werden
	Messenger messengerToActivity = new Messenger(new IncomingHandler());

	static final int MSG_RESULT = 10000;

	// Kontrollelemente

	private Button setCards;
	private Button callCheck;
	private Button raiseBet;
	private Button fold;
	private Button menu;

	// Variablen, die den Zustand des Spiels beschreiben

	// Allgemein

	private boolean isNewSession;

	// Gesamtanzahl der Spieler
	private int playerNum;
	// Startkapital
	private int startAmount;
	private int smallBlind;
	private int bigBlind;
	private int ante;

	private boolean isBlindLevels;
	private int blindLevelTime;

	private long blindIncreaseTime;

	private int[] madeBets = new int[9];

	// Zugriff auf den User Spieler
	private You you;

	// ----------------------------------------

	// Momentan
	private Player dealerNum;

	// Neuer Dealer (ab nächste Runde)
	private Player newDealer = null;

	// Variablen, um zu wissen, welche Tasten zu aktivieren sind

	private boolean activateSet = false;
	private boolean activateFold = false;
	private boolean activateCall = false;
	private boolean activateRaise = false;

	private int pot;
	private int odd = 0;
	private boolean calcOddDone = false;

	// Spieler, der gerade dran ist (die Nummer bezieht sich auf die Liste der
	// aktiven Spieler)
	private Player onTurnNum;

	// Bet, den man mindestens aufbringen muss.

	private int currentBet = 0;

	private TextView potBox;
	private int potWithBets = 0;
	private TextView potWithBetsBox;

	// Flop
	private Flop flop = new Flop();
	private Button flopBox1;
	private Button flopBox2;
	private Button flopBox3;
	private TextView flopText;

	// Turn
	private Turn turn = new Turn();
	private Button turnBox;
	private TextView turnText;

	// River
	private River river = new River();
	private Button riverBox;
	private TextView riverText;

	// Zustandsvariablen

	// Spielphase (0=>preFlop; 1=> nachFlop; 2=> nachTurn; 3=> nach River)
	private int phase = 0;
	// Karten des Users gesetzt?
	private boolean cardsSet = false;

	// Setzt man gerade einen neuen Dealer?
	private boolean settingDealer = false;

	// Für die SiegerAussuchphase
	private boolean choosingWinner = false;
	private boolean splittingPot = false;
	private CircleList splitList = new CircleList();

	// Alle Spieler, die noch nicht draussen sind
	private CircleList sessionPlayers = new CircleList();

	// Spieler, die bei der aktuellen Runde noch mitmachen
	private CircleList playingPlayers = new CircleList();

	// Spieler, die bei der Runde noch setzen müssen

	private CircleList activePlayers = new CircleList();

	// Liste, um einen Überblick über die Buttons zu haben

	private CircleList playerButtons = new CircleList();

	// ------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		isNewSession = intent.getBooleanExtra(KEY_NEW_BOOL, true);

		if (isNewSession) {
			// Die Daten, die in dem Konfi Menü festgelegt wurden, anwenden

			loadStartValues();

			// Setzen des Layouts (später unterscheiden, je nachdem, wie viele
			// Spieler gesetzt sind)
			setContentView(getLayoutId(playerNum));

			// Zuweisen des Pots
			potBox = (TextView) findViewById(R.id.ps_textViewPot);
			potWithBetsBox = (TextView) findViewById(R.id.ps_textViewPotWithBets);

			// Zuweisen der CommCard Elemente
			flopBox1 = (Button) findViewById(R.id.ps_button_flop1);
			flopBox2 = (Button) findViewById(R.id.ps_button_flop2);
			flopBox3 = (Button) findViewById(R.id.ps_button_flop3);
			flopText = (TextView) findViewById(R.id.ps_textView_flop);

			turnBox = (Button) findViewById(R.id.ps_button_turn);
			turnText = (TextView) findViewById(R.id.ps_textView_turn);

			riverBox = (Button) findViewById(R.id.ps_button_river);
			riverText = (TextView) findViewById(R.id.ps_textView_river);

			// Zuweisen der Kontrollelemente

			setCards = (Button) findViewById(R.id.ps_button_set_cards);
			callCheck = (Button) findViewById(R.id.ps_button_call);
			raiseBet = (Button) findViewById(R.id.ps_button_raise);
			fold = (Button) findViewById(R.id.ps_button_check_fold);
			menu = (Button) findViewById(R.id.ps_button_settings);

			// Erschaffen des User Spielers

			You you = new You(startAmount, this);
			this.you = you;
			sessionPlayers.add(you);
			playerButtons.add(you);
			you.getOptions().setKeepScreenOn(true);

			// Erschaffen der anderen Spieler
			for (int i = 1; i < playerNum; i++) {

				String name = "Player " + i;

				OtherPlayer player = new OtherPlayer(startAmount, name, i, this);
				sessionPlayers.add(player);
				playerButtons.add(player);

			}
		}

		setSessionOnClickListeners();

		// Setzen des Pots
		setPot(0);

		// Initialisieren des CommCards Zustands

		flopBox1.setBackgroundResource(R.drawable.ps_card_not_dealt);
		flopBox2.setBackgroundResource(R.drawable.ps_card_not_dealt);
		flopBox3.setBackgroundResource(R.drawable.ps_card_not_dealt);
		flopText.setTextColor(getResources().getColor(R.color.poker_table));

		turnBox.setBackgroundResource(R.drawable.ps_card_not_dealt);
		turnText.setTextColor(getResources().getColor(R.color.poker_table));

		riverBox.setBackgroundResource(R.drawable.ps_card_not_dealt);
		riverText.setTextColor(getResources().getColor(R.color.poker_table));

		flopBox1.setEnabled(false);
		flopBox2.setEnabled(false);
		flopBox3.setEnabled(false);

		turnBox.setEnabled(false);

		riverBox.setEnabled(false);

		for (int i = 0; i < madeBets.length; i++) {
			madeBets[i] = 0;
		}

		madeBets[0] = bigBlind;

		// Bauen der Liste der Spieler, die bei der Runde dabei sind (initial
		// alle aktiv)
		for (int i = 0; i < sessionPlayers.size(); i++) {

			playingPlayers.add(sessionPlayers.get(i));
			activePlayers.add(sessionPlayers.get(i));
			sessionPlayers.get(i).setDealer(false);

		}

		startDealerSetting();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.poker_session, menu);
		return true;
	}

	@Override
	protected void onStart() {

		super.onStart();

		// Für das Binden des Services an die Activity

		Intent intent = new Intent(this, OddCalculatorService.class);
		bindService(intent, calcConnection, Context.BIND_AUTO_CREATE);

//		if (!isNewSession) {
//			loadValues();
//		}

	}

	@Override
	protected void onStop() {

		super.onStop();

		if (serviceBound) {

			unbindService(calcConnection);
			serviceBound = false;

		}

//		saveValues();
//		isNewSession = false;
	}

//	private void saveValues() {
//
//		PreferenceContainer.saveInt(PreferenceContainer.KEY_BIG_BLIND_CURRENT,
//				this, bigBlind);
//		PreferenceContainer.saveInt(
//				PreferenceContainer.KEY_SMALL_BLIND_CURRENT, this, smallBlind);
//		PreferenceContainer.saveInt(PreferenceContainer.KEY_ANTE_CURRENT, this,
//				ante);
//
//		long blindDiff = blindIncreaseTime - System.currentTimeMillis();
//		PreferenceContainer.saveLong(
//				PreferenceContainer.KEY_BLIND_DIFFERENCE_TIME, this, blindDiff);
//
//		PreferenceContainer.saveInt(PreferenceContainer.keyPot, this, pot);
//		PreferenceContainer.saveInt(PreferenceContainer.keyPotWithBets, this,
//				potWithBets);
//		PreferenceContainer.saveInt(PreferenceContainer.keyCurrentBetValue,
//				this, currentBet);
//
//		// Speichern der Phase
//
//		PreferenceContainer.saveInt(PreferenceContainer.keyPhaseValue, this,
//				phase);
//
//		// Speichern der Community Cards
//		int color;
//		int value;
//
//		// Speichern des Flops
//		if (flop.getCard1() != null) {
//			color = flop.getCard1().getColor();
//			value = flop.getCard1().getValue();
//
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardColorFlop1,
//					this, color);
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardValueFlop1,
//					this, value);
//
//			color = flop.getCard2().getColor();
//			value = flop.getCard2().getValue();
//
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardColorFlop2,
//					this, color);
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardValueFlop2,
//					this, value);
//
//			color = flop.getCard3().getColor();
//			value = flop.getCard3().getValue();
//
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardColorFlop3,
//					this, color);
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardValueFlop3,
//					this, value);
//
//		} else {
//
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardColorFlop1,
//					this, 0);
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardValueFlop1,
//					this, 0);
//
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardColorFlop2,
//					this, 0);
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardValueFlop2,
//					this, 0);
//
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardColorFlop3,
//					this, 0);
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardValueFlop3,
//					this, 0);
//
//		}
//
//		// Speichern des Turns
//
//		if (turn.getCard() != null) {
//
//			color = turn.getCard().getColor();
//			value = turn.getCard().getValue();
//
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardColorTurn,
//					this, color);
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardValueTurn,
//					this, value);
//		} else {
//
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardColorTurn,
//					this, 0);
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardValueTurn,
//					this, 0);
//
//		}
//
//		// Speichern des Rivers
//
//		if (river.getCard() != null) {
//
//			color = river.getCard().getColor();
//			value = river.getCard().getValue();
//
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardColorRiver,
//					this, color);
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardValueRiver,
//					this, value);
//
//		} else {
//
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardColorRiver,
//					this, 0);
//			PreferenceContainer.saveInt(PreferenceContainer.keyCardValueRiver,
//					this, 0);
//
//		}
//	}

//	private void loadValues() {
//		loadStartValues();
//
//		bigBlind = PreferenceContainer.loadInt(
//				PreferenceContainer.KEY_BIG_BLIND_CURRENT, this);
//		smallBlind = PreferenceContainer.loadInt(
//				PreferenceContainer.KEY_SMALL_BLIND_CURRENT, this);
//		ante = PreferenceContainer.loadInt(
//				PreferenceContainer.KEY_ANTE_CURRENT, this);
//		long blindDiff = PreferenceContainer.loadLong(
//				PreferenceContainer.KEY_BLIND_DIFFERENCE_TIME, this);
//		blindIncreaseTime = blindDiff + System.currentTimeMillis();
//
//		pot = PreferenceContainer.loadInt(PreferenceContainer.keyPot, this);
//		potWithBets = PreferenceContainer.loadInt(
//				PreferenceContainer.keyPotWithBets, this);
//		currentBet = PreferenceContainer.loadInt(
//				PreferenceContainer.keyCurrentBetValue, this);
//
//		// Laden der Phase
//
//		phase = PreferenceContainer.loadInt(PreferenceContainer.keyPhaseValue,
//				this);
//
//		// Laden der Community Cards
//
//		flopBox1.setBackgroundResource(R.drawable.ps_card_not_dealt);
//		flopBox2.setBackgroundResource(R.drawable.ps_card_not_dealt);
//		flopBox3.setBackgroundResource(R.drawable.ps_card_not_dealt);
//		flopText.setTextColor(getResources().getColor(R.color.poker_table));
//
//		turnBox.setBackgroundResource(R.drawable.ps_card_not_dealt);
//		turnText.setTextColor(getResources().getColor(R.color.poker_table));
//
//		riverBox.setBackgroundResource(R.drawable.ps_card_not_dealt);
//		riverText.setTextColor(getResources().getColor(R.color.poker_table));
//
//		flopBox1.setEnabled(false);
//		flopBox2.setEnabled(false);
//		flopBox3.setEnabled(false);
//
//		turnBox.setEnabled(false);
//
//		riverBox.setEnabled(false);
//
//		if (phase > 0) {
//
//			flopBox1.setBackgroundResource(R.drawable.button_card_in_game_back);
//			flopBox2.setBackgroundResource(R.drawable.button_card_in_game_back);
//			flopBox3.setBackgroundResource(R.drawable.button_card_in_game_back);
//			flopText.setTextColor(getResources().getColor(R.color.BLACK));
//
//			int color;
//			int value;
//
//			color = PreferenceContainer.loadInt(
//					PreferenceContainer.keyCardColorFlop1, this);
//			value = PreferenceContainer.loadInt(
//					PreferenceContainer.keyCardValueFlop1, this);
//
//			if (color != 0 && value != 0) {
//
//				flop.setCard1(new Card(color, value, this));
//
//				setButtonColor(flopBox3, color);
//				setButtonText(flopBox3, flop.getCard1());
//				flopBox3.setEnabled(true);
//
//				if (playingPlayers.contains(you)) {
//					callCalc();
//				}
//
//				color = PreferenceContainer.loadInt(
//						PreferenceContainer.keyCardColorFlop2, this);
//				value = PreferenceContainer.loadInt(
//						PreferenceContainer.keyCardValueFlop2, this);
//
//				flop.setCard2(new Card(color, value, this));
//
//				setButtonColor(flopBox2, color);
//				setButtonText(flopBox2, flop.getCard2());
//				flopBox2.setEnabled(true);
//
//				color = PreferenceContainer.loadInt(
//						PreferenceContainer.keyCardColorFlop3, this);
//				value = PreferenceContainer.loadInt(
//						PreferenceContainer.keyCardValueFlop3, this);
//
//				flop.setCard3(new Card(color, value, this));
//
//				setButtonColor(flopBox1, color);
//				setButtonText(flopBox1, flop.getCard3());
//				flopBox1.setEnabled(true);
//
//			}
//
//			if (phase > 1) {
//				turnBox.setBackgroundResource(R.drawable.button_card_in_game_back);
//				turnText.setTextColor(getResources().getColor(R.color.BLACK));
//
//				color = PreferenceContainer.loadInt(
//						PreferenceContainer.keyCardColorTurn, this);
//				value = PreferenceContainer.loadInt(
//						PreferenceContainer.keyCardValueTurn, this);
//
//				if (color != 0 && value != 0) {
//
//					turn.dealCard(new Card(color, value, this));
//
//					setButtonColor(turnBox, color);
//					setButtonText(turnBox, turn.getCard());
//					turnBox.setEnabled(true);
//
//					if (playingPlayers.contains(you)) {
//						callCalc();
//					}
//
//				}
//			}
//
//			if (phase > 2) {
//
//				riverBox.setBackgroundResource(R.drawable.button_card_in_game_back);
//				riverText.setTextColor(getResources().getColor(R.color.BLACK));
//
//				color = PreferenceContainer.loadInt(
//						PreferenceContainer.keyCardColorRiver, this);
//				value = PreferenceContainer.loadInt(
//						PreferenceContainer.keyCardValueRiver, this);
//
//				if (color != 0 && value != 0) {
//
//					river.dealCard(new Card(color, value, this));
//
//					setButtonColor(riverBox, color);
//					setButtonText(riverBox, river.getCard());
//					riverBox.setEnabled(true);
//
//					if (playingPlayers.contains(you)) {
//						callCalc();
//					}
//
//				}
//
//			}
//
//		} else {
//			flopBox1.setBackgroundResource(R.drawable.ps_card_not_dealt);
//			flopBox2.setBackgroundResource(R.drawable.ps_card_not_dealt);
//			flopBox3.setBackgroundResource(R.drawable.ps_card_not_dealt);
//			flopText.setTextColor(getResources().getColor(R.color.poker_table));
//
//			turnBox.setBackgroundResource(R.drawable.ps_card_not_dealt);
//			turnText.setTextColor(getResources().getColor(R.color.poker_table));
//
//			riverBox.setBackgroundResource(R.drawable.ps_card_not_dealt);
//			riverText
//					.setTextColor(getResources().getColor(R.color.poker_table));
//
//			flopBox1.setEnabled(false);
//			flopBox2.setEnabled(false);
//			flopBox3.setEnabled(false);
//
//			turnBox.setEnabled(false);
//
//			riverBox.setEnabled(false);
//		}
//		
//		
//
//	}

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
				int odd = msg.getData().getInt(PreferenceContainer.oddKey);

				if (calcOddDone) {
					illustrateOdd(odd);
					adjustBetLimitAdvice(odd);
				}
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

	private void loadStartValues() {
		startAmount = PreferenceContainer.loadInt(
				PreferenceContainer.KEY_INITIAN_CHIPS, this);
		bigBlind = PreferenceContainer.loadInt(
				PreferenceContainer.KEY_BIG_BLIND, this);
		smallBlind = PreferenceContainer.loadInt(
				PreferenceContainer.KEY_SMALL_BLIND, this);
		ante = PreferenceContainer.loadInt(PreferenceContainer.KEY_ANTE, this);
		playerNum = PreferenceContainer.loadInt(
				PreferenceContainer.KEY_PLAYER_NUM, this);
		isBlindLevels = PreferenceContainer.loadBool(
				PreferenceContainer.KEY_BLIND_LEVEL_BOOL, this);
		int blindLevel = PreferenceContainer.loadInt(
				PreferenceContainer.KEY_BLIND_LEVEL_TIME_INT, this);
		blindLevelTime = blindLevel * 60 * 1000;
		blindIncreaseTime = System.currentTimeMillis() + blindLevelTime;
	}

	private void callCalc() {
		if (!serviceBound) {
			return;
		}

		if (you.getHand().getCard1() == null
				|| you.getHand().getCard2() == null) {
			return;
		}

		if (!calcOddDone) {
			calcOddDone = true;
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

		int oppoNum = playingPlayers.size() - 1;

		data.putInt(PreferenceContainer.keyOppoNum, oppoNum);

		msg.setData(data);

		try {
			messenger.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private int loadInt(String key) {

		return PreferenceContainer.loadInt(key, this);

	}

	private void setPot(int pot) {
		this.pot = pot;

		this.potWithBets = pot;
		if (pot > 0) {
			potBox.setText(String.valueOf(pot));
		} else {
			potBox.setText("");
		}

		if (pot > 0) {
			potWithBetsBox.setText("(" + String.valueOf(pot) + ")");
		} else {
			potWithBetsBox.setText("");
		}

	}

	private void bet(Player player, int amount) {
		player.bet(amount);
		potWithBets = potWithBets + amount;
		if (potWithBets > 0) {
			potWithBetsBox.setText("(" + String.valueOf(potWithBets) + ")");
		} else {
			potWithBetsBox.setText("");
		}

	}

	private void goAllIn(Player player) {

		potWithBets = potWithBets + player.getMoney();
		potWithBetsBox.setText("(" + String.valueOf(potWithBets) + ")");
		if (player.getMoney() + player.getBet() > currentBet) {
			currentBet = player.getMoney() + player.getBet();
		}

		player.bet(player.getMoney());

	}

	private void setSessionOnClickListeners() {

		// User Bedientasten

		setCards.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!choosingWinner) {

					if (you.isInRound()) {
						onClickSetButton();
					} else {
						determineWinner();
					}
				} else if (choosingWinner && !splittingPot) {

					Toast.makeText(getApplicationContext(),
							R.string.split_pot_toast, Toast.LENGTH_SHORT)
							.show();
					splittingPot = true;
					setCards.setText(R.string.okay);
				} else {

					// Bleibt vom Pot was übrig?
					boolean incompleteSplit = false;
					splitList.clear();

					for (int i = 0; i < sessionPlayers.size(); i++) {
						if (sessionPlayers.get(i).isSelected()) {
							splitList.add(sessionPlayers.get(i));

							// Ist der Spieler AllIn?

							if (sessionPlayers.get(i).getPotShare() != potWithBets) {
								incompleteSplit = true;
							}
						}
					}

					if (splitList.size() == 0) {
						return;
					}

					Player minPlayer = null;

					if (incompleteSplit) {
						while (splitList.size() > 0) {
							int min = 1000000000;

							for (int i = 0; i < splitList.size(); i++) {

								if (splitList.get(i).getPotShare() != 0
										&& splitList.get(i).getPotShare() < min) {
									min = splitList.get(i).getPotShare();
									minPlayer = splitList.get(i);

								}

							}

							if (pot > min) {
								pot = pot - min;
							} else {
								pot = 0;
							}

							if (potWithBets > min) {
								potWithBets = potWithBets - min;
							} else {
								min = potWithBets;
								potWithBets = 0;

							}

							int rest = min % splitList.size();
							int gain = min / splitList.size();

							potWithBets = potWithBets + rest;
							pot = pot + rest;

							for (int i = 0; i < splitList.size(); i++) {

								Player player = splitList.get(i);

								player.setMoney(player.getMoney() + gain);

							}

							for (int i = 0; i < playingPlayers.size(); i++) {

								Player player1 = playingPlayers.get(i);

								player1.setPotShare(player1.getPotShare() - min);

								if (player1.getPotShare() <= 0) {
									playingPlayers.remove(player1);
									i--;
									player1.unselect();
								}

							}

							splitList.remove(minPlayer);
						}

						potBox.setText(String.valueOf(pot));
						potWithBetsBox.setText(String.valueOf(potWithBets));

						if (playingPlayers.size() < 2) {
							choosingWinner = false;
							splittingPot = false;
							finishRound();
						} else {
							determineWinner();
							splittingPot = false;
						}

					} else {

						playingPlayers = (CircleList) splitList.clone();
						for (int i = 0; i < splitList.size(); i++) {
							splitList.get(i).unselect();
						}

						choosingWinner = false;
						splittingPot = false;

						finishRound();
					}

				}

			}
		});

		callCheck.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickCallCheckButton();

			}
		});

		raiseBet.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Player player = onTurnNum;

				onClickRaiseBetButton(player.getMoney());

			}
		});

		fold.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickFoldButton();

			}
		});

		menu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickMenuButton();

			}
		});

		// KartenButtons setzen

		you.getHand1().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getCardFromUser(CARD_REQUEST_H2, 2);

			}
		});

		you.getHand2().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getCardFromUser(CARD_REQUEST_H1, 1);

			}
		});

		flopBox1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				getCardFromUser(CARD_REQUEST_F3, 5);
			}
		});

		flopBox2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				getCardFromUser(CARD_REQUEST_F2, 4);
			}
		});

		flopBox3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				getCardFromUser(CARD_REQUEST_F1, 3);
			}
		});

		turnBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				getCardFromUser(CARD_REQUEST_T, 6);
			}
		});

		riverBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				getCardFromUser(CARD_REQUEST_R, 7);
			}
		});

		// PlayerButtons setzen
		you.getOptions().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (choosingWinner && playingPlayers.contains(you)) {

					if (!splittingPot) {

						if (you.getPotShare() == potWithBets) {
							playingPlayers.clear();
							playingPlayers.add(you);

							choosingWinner = false;

							finishRound();
						} else {

							Player player = you;

							// Fall, dass der Spieler einen SidePot gewinnt
							int gain;

							if (player.getPotShare() < potWithBets) {
								player.setMoney(player.getPotShare()
										+ player.getMoney());
								player.setHandsWon(player.getHandsWon() + 1);
								potWithBets = potWithBets
										- player.getPotShare();
								gain = player.getPotShare();
							} else {
								player.setMoney(potWithBets + player.getMoney());
								player.setHandsWon(player.getHandsWon() + 1);
								potWithBets = 0;
								gain = potWithBets;
							}
							if (pot > player.getPotShare()) {
								pot = pot - player.getPotShare();

							} else {
								pot = 0;
							}

							potBox.setText(String.valueOf(pot));
							potWithBetsBox.setText(String.valueOf(potWithBets));

							CircleList playersToRemove = new CircleList();

							for (int i = 0; i < playingPlayers.size(); i++) {

								if (gain > playingPlayers.get(i).getPotShare()
										&& playingPlayers.get(i).getPotShare() != 0) {

									playingPlayers.get(i).setPotShare(0);
									playersToRemove.add(playingPlayers.get(i));

								} else if (gain < playingPlayers.get(i)
										.getPotShare()) {
									playingPlayers.get(i).setPotShare(
											playingPlayers.get(i).getPotShare()
													- gain);
								}

							}

							for (int i = 0; i < playersToRemove.size(); i++) {

								playersToRemove.get(i).setWentAllIn(false);
								playingPlayers.remove(playersToRemove.get(i));

							}

							player.setWentAllIn(false);
							playingPlayers.remove(player);
							player.unfocus();
							if (potWithBets != 0) {
								determineWinner();
							} else {
								choosingWinner = false;
								finishRound();
							}

						}
					} else {

						you.setSelected();

					}

				} else if (settingDealer && sessionPlayers.contains(you)) {

					for (int i = 0; i < sessionPlayers.size(); i++) {
						Player player1 = sessionPlayers.get(i);

						if (!player1.isOnTurn()) {
							player1.setOnTurn(false);
						}
					}

					adjustDealer(you);
					settingDealer = false;

				} else if (!sessionPlayers.contains(you)) {
					callPlayerOutMenu(playerButtons.indexOf(you));
				} else {
					callPlayerInMenu(playerButtons.indexOf(you));
				}

			}
		});

		for (int i = 0; i < sessionPlayers.size(); i++) {

			Player player2 = sessionPlayers.get(i);

			if (!(player2 instanceof You)) {

				OtherPlayer player = (OtherPlayer) sessionPlayers.get(i);

				player.getButton().setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View v) {

								Player player = findPlayerForView((ImageButton) v);

								if (choosingWinner
										&& playingPlayers.contains(player)) {

									if (!splittingPot) {

										if (player.getPotShare() == potWithBets) {

											// Fall, dass der Spieler alles
											// gewinnt
											playingPlayers.clear();

											playingPlayers.add(player);

											choosingWinner = false;

											finishRound();
										} else {

											// Fall, dass der Spieler einen
											// SidePot gewinnt
											int gain;

											if (player.getPotShare() < potWithBets) {

												player.setMoney(player
														.getPotShare()
														+ player.getMoney());
												potWithBets = potWithBets
														- player.getPotShare();
												gain = player.getPotShare();
											} else {
												player.setMoney(potWithBets
														+ player.getMoney());
												potWithBets = 0;
												gain = potWithBets;
											}
											if (pot > player.getPotShare()) {
												pot = pot
														- player.getPotShare();

											} else {
												pot = 0;
											}

											potBox.setText(String.valueOf(pot));
											potWithBetsBox.setText(String
													.valueOf(potWithBets));

											CircleList playersToRemove = new CircleList();

											for (int i = 0; i < playingPlayers
													.size(); i++) {

												if (gain >= playingPlayers.get(
														i).getPotShare()
														&& playingPlayers
																.get(i)
																.getPotShare() != 0) {

													playersToRemove
															.add(playingPlayers
																	.get(i));

												} else if (gain < playingPlayers
														.get(i).getPotShare()) {
													playingPlayers
															.get(i)
															.setPotShare(
																	playingPlayers
																			.get(i)
																			.getPotShare()
																			- gain);

												}

											}

											for (int i = 0; i < playersToRemove
													.size(); i++) {
												playersToRemove.get(i)
														.setPotShare(0);
												playersToRemove.get(i)
														.unfocus();
												playersToRemove.get(i)
														.setWentAllIn(false);
												playingPlayers
														.remove(playersToRemove
																.get(i));

											}

											player.setWentAllIn(false);
											playingPlayers.remove(player);
											player.unfocus();
											if (playingPlayers.size() > 1) {
												determineWinner();
											} else {
												choosingWinner = false;
												finishRound();
											}

										}
									} else {

										player.setSelected();
									}

								} else if (settingDealer
										&& sessionPlayers.contains(player)) {

									for (int i = 0; i < sessionPlayers.size(); i++) {
										Player player1 = sessionPlayers.get(i);

										if (!player1.isOnTurn()) {
											player1.setOnTurn(false);
										}
									}

									adjustDealer(player);
									settingDealer = false;

								} else if (!sessionPlayers.contains(player)) {
									callPlayerOutMenu(playerButtons
											.indexOf(player));
								} else {
									callPlayerInMenu(playerButtons
											.indexOf(player));
								}
							}
						});
			}
		}
	}

	private void onClickSetButton() {

		// Hier werden entweder die Hand oder die passenden CommCards gesetzt

		switch (phase) {

		case 0:

			// Hand setzen
			dealHand();

			break;

		case 1:

			// Flop setzen
			dealFlop();
			break;

		case 2:

			// Turn setzen
			dealTurn();
			break;

		case 3:

			// River setzen
			dealRiver();
			break;

		}

		// Am Ende: Button nicht mehr aktiv

		setCards.setEnabled(false);

		if (activePlayers.size() < 1 && !activePlayers.contains(you)) {
			switch (phase) {
			case 0:
				transferPhase();
				break;

			case 1:
				transferPhase();
				break;

			case 2:
				transferPhase();
				break;

			case 3:
				determineWinner();
				break;
			}
		}

	}

	private void onClickCallCheckButton() {

		// Callen oder Checken
		// Aktiver Spieler setzt das nach, was ihm zur aktuellen Bet fehlt.
		Player player = onTurnNum;
		if (player.getBet() < currentBet) {

			player.setPlayingHand(true);

			if (currentBet - player.getBet() < player.getMoney()) {
				bet(player, currentBet - player.getBet());
			} else {
				bet(player, player.getMoney());
				player.setInRound(false, true);
				player.setWentAllIn(true);

			}

		}

		adjustBetLimitAdvice(odd);

		// Weitergeben des "Dran sein"
		transferTurn();
	}

	private void onClickRaiseBetButton(int playerMoney) {
		// Hier wird geraised oder gebettet

		Intent intent = new Intent(getApplicationContext(), BetAmountPick.class);
		intent.putExtra(PreferenceContainer.keyCurrentBet, currentBet);
		intent.putExtra(PreferenceContainer.keyPlayerMoney, playerMoney);
		intent.putExtra(PreferenceContainer.keyMadeBets, madeBets);

		startActivityForResult(intent, BET_REQUEST);

	}

	private void onClickFoldButton() {

		Player player = onTurnNum;
		player.setInRound(false, false);

		playingPlayers.remove(player);
		if (player instanceof You) {
			setCards.setText(R.string.jump_next_round);
			setCards.setEnabled(true);
		}

		if (playingPlayers.contains(you) && playingPlayers.size() > 1) {

			callCalc();
		}

		// Überprüfen, ob nur noch ein Spieler; Ist dem so, dann hat dieser
		// Spieler gewonnen

		if (playingPlayers.size() < 2) {
			finishRound();
			return;
		}

		transferTurn();
	}

	private void onClickMenuButton() {

		Intent intent = new Intent(getApplicationContext(),
				PsMenuActivity.class);

		intent.putExtra(PreferenceContainer.keyCurrentBB, bigBlind);
		intent.putExtra(PreferenceContainer.keyCurrentSB, smallBlind);
		intent.putExtra(PreferenceContainer.keyCurrentAnte, ante);

		startActivityForResult(intent, MENU_REQUEST);

	}

	private void transferTurn() {

		if (activePlayers.size() > 1) {

			// Anpassen des Spielers, der als nächstes dran ist
			Player now = onTurnNum;
			// Es ist mindestens noch ein weiterer Spieler da
			Player next = activePlayers.get(activePlayers.indexOf(now) + 1);
			now.setOnTurn(false);
			next.setOnTurn(true);
			activePlayers.remove(now);
			onTurnNum = next;

			// Anpassen der CallCheck Taste
			if (next.getBet() == currentBet) {
				callCheck.setText(R.string.check);
			} else {
				callCheck.setText(R.string.call);
			}

			// Anpassen der BetRaise Taste

			if (currentBet > 0) {
				raiseBet.setText(R.string.raise);
			}

		} else if (activePlayers.size() == 1) {

			if (activePlayers.size() > 0) {

				Player now = onTurnNum;
				now.setOnTurn(false);
				activePlayers.remove(now);
			}

			// Gerade ist der letzte aktive Spieler dran gewesen => entweder
			// nächste Phase oder noch Karten setzen (falls User noch bei Runde
			// dabei) oder es ist vorbei und ein Gewinner muss bestimmt werden.

			// Falls weniger als 2 Spieler handlungsfähig, wird der Gewinner
			// sofort ermittelt
			int numPlayingInRound = 0;

			for (int i = 0; i < playingPlayers.size(); i++) {

				if (playingPlayers.get(i).isInRound()) {
					numPlayingInRound++;
				}

			}

			if (numPlayingInRound < 2) {
				determineWinner();
				return;
			}

			if (cardsSet || !you.isInRound()) {
				// Hier gehts in die nächste Runde

				switch (phase) {
				case 0:
					transferPhase();
					break;

				case 1:
					transferPhase();
					break;

				case 2:
					transferPhase();
					break;

				case 3:
					determineWinner();

					break;
				}

			} else if (you.isInRound()) {

				// Hier noch Karten setzen => alle anderen Buttons
				// deaktivieren

				callCheck.setEnabled(false);
				raiseBet.setEnabled(false);
				fold.setEnabled(false);
				setCards.setEnabled(true);

			} else {
				switch (phase) {
				case 0:
					transferPhase();
					break;

				case 1:
					transferPhase();
					break;

				case 2:
					transferPhase();
					break;

				case 3:
					determineWinner();

					break;
				}
			}

		} else {
			determineWinner();
		}

	}

	private void transferPhase() {

		// Bei allen Spielern, die AllIn gegangen sind, wird festgehalten, wie
		// viel sie kriegen, falls sie gewinnen.

		for (int i = 0; i < playingPlayers.size(); i++) {

			Player player = playingPlayers.get(i);

			if (player.isWentAllIn()) {
				calcPotShare(player);
				player.setWentAllIn(false);
			}

		}

		setPot(potWithBets);

		currentBet = 0;

		activePlayers.clear();

		for (int i = 0; i < sessionPlayers.size(); i++) {
			sessionPlayers.get(i).setBet(0);
		}

		for (int i = 0; i < playingPlayers.size(); i++) {

			if (playingPlayers.get(i).getMoney() > 0) {
				activePlayers.add(playingPlayers.get(i));
			}
		}

		// Phase wird gewechselt
		phase++;

		// Alle Tasten aktivieren + richtig setzen

		raiseBet.setEnabled(true);
		raiseBet.setText(R.string.bet);

		callCheck.setEnabled(true);
		callCheck.setText(R.string.check);

		fold.setEnabled(true);

		setCards.setEnabled(true);
		if (you.isInRound()) {
			if (phase == 1) {
				setCards.setText(R.string.set_flop);
			} else if (phase == 2) {
				setCards.setText(R.string.set_turn);
			} else if (phase == 3) {
				setCards.setText(R.string.set_river);
			}
		} else {
			setCards.setText(R.string.jump_next_round);
		}
		// Die Flopkarten hinlegen

		if (phase == 1) {
			flopBox1.setBackgroundResource(R.drawable.button_card_in_game_back);
			flopBox2.setBackgroundResource(R.drawable.button_card_in_game_back);
			flopBox3.setBackgroundResource(R.drawable.button_card_in_game_back);
			flopText.setTextColor(getResources().getColor(R.color.BLACK));
		} else if (phase == 2) {

			// Die Turnkarte hinlegen

			turnBox.setBackgroundResource(R.drawable.button_card_in_game_back);
			turnText.setTextColor(getResources().getColor(R.color.BLACK));

		} else if (phase == 3) {

			// Die Riverkarte hinlegen

			riverBox.setBackgroundResource(R.drawable.button_card_in_game_back);
			riverText.setTextColor(getResources().getColor(R.color.BLACK));
		}

		cardsSet = false;

		// Setzen, wer grad dran ist

		for (int i = sessionPlayers.indexOf(dealerNum) + 1; i < 2 * sessionPlayers
				.size(); i++) {

			Player player = sessionPlayers.get(i);
			if (player.isInRound()) {

				player.setOnTurn(true);
				onTurnNum = player;
				break;
			}

		}

	}

	private void finishRound() {

		Player player;

		if (playingPlayers.size() == 1) {

			player = playingPlayers.get(0);

			// Gewinner steht fest (erstmal einer)

			if (potWithBets != 0) {
				player.setMoney(player.getMoney() + potWithBets);
				player.setHandsWon(player.getHandsWon() + 1);
			}

			// Pot wieder auf null + Alle sind nicht dran
			setPot(0);
		} else {

			// Es gibt mehrere Gewinner

			int winNum = playingPlayers.size();

			int winAmount = 0;
			int potRest = 0;
			if (winNum != 0) {
				winAmount = potWithBets / winNum;
				potRest = potWithBets % winNum;
			}

			for (int i = 0; i < playingPlayers.size(); i++) {
				player = playingPlayers.get(i);
				player.setMoney(player.getMoney() + winAmount);
				player.setHandsWon(player.getHandsWon() + 1);
			}

			// Pot auf den Rest setzen

			setPot(potRest);

		}

		// Die gesetzten Karten resetten
		resetCards();

		// Alle Bets auf 0 setzen
		for (int i = 0; i < sessionPlayers.size(); i++) {
			sessionPlayers.get(i).setBet(0);
			sessionPlayers.get(i).setOnTurn(false);
			sessionPlayers.get(i).setTotalHands(
					sessionPlayers.get(i).getTotalHands() + 1);
			if (sessionPlayers.get(i).isPlayingHand()) {
				sessionPlayers.get(i).setHandsPlayed(
						sessionPlayers.get(i).getHandsPlayed() + 1);
			}
			sessionPlayers.get(i).setPlayingHand(false);
			sessionPlayers.get(i).updateStats();
		}

		// Dealer merken

		Player dealer = dealerNum;

		// Alle Spieler, die kein Geld mehr haben, sind nicht mehr dabei

		for (int i = 0; i < sessionPlayers.size(); i++) {

			if (sessionPlayers.get(i).getMoney() == 0) {

				Player player1 = sessionPlayers.get(i);

				if (player1.isDealer()) {
					player1.setDealer(false);

					Player nextDealer = sessionPlayers.get(sessionPlayers
							.indexOf(player1) - 1);
					dealer = nextDealer;
					nextDealer.setDealer(true);
				}

				if (player1 instanceof You) {
					((You) player1).getOptions().setBackgroundResource(
							R.drawable.button_ps_player_out_of_game);
					((You) player1).getHand1().setAlpha(0);
					((You) player1).getHand1().setEnabled(false);
					((You) player1).getHand2().setAlpha(0);
					((You) player1).getHand2().setEnabled(false);
				}

				if (player1 instanceof OtherPlayer) {
					((OtherPlayer) player1).getButton().setBackgroundResource(
							R.drawable.button_ps_player_out_of_game);
					((OtherPlayer) player1).getButton().setImageDrawable(
							getResources().getDrawable(
									R.drawable.ps_player_fold));
				}

				sessionPlayers.remove(sessionPlayers.get(i));
				i--;
			}

		}

		// Ab hier: Einleiten der neuen Runde

		// Alle CommCards ausschalten und zudecken; die CommTitel ausschalten

		riverBox.setBackgroundResource(R.drawable.ps_card_not_dealt);
		riverBox.setEnabled(false);
		riverText.setTextColor(getResources().getColor(R.color.poker_table));
		riverBox.setText("");

		turnBox.setBackgroundResource(R.drawable.ps_card_not_dealt);
		turnBox.setEnabled(false);
		turnBox.setText("");
		turnText.setTextColor(getResources().getColor(R.color.poker_table));

		flopBox1.setBackgroundResource(R.drawable.ps_card_not_dealt);
		flopBox1.setEnabled(false);
		flopBox1.setText("");

		flopBox2.setBackgroundResource(R.drawable.ps_card_not_dealt);
		flopBox2.setEnabled(false);
		flopBox2.setText("");

		flopBox3.setBackgroundResource(R.drawable.ps_card_not_dealt);
		flopBox3.setEnabled(false);
		flopBox3.setText("");

		flopText.setTextColor(getResources().getColor(R.color.poker_table));

		// Alle die nicht draussen sind, dabei und Aktiv

		activePlayers.clear();
		playingPlayers.clear();

		for (int i = 0; i < sessionPlayers.size(); i++) {
			playingPlayers.add(sessionPlayers.get(i));
			activePlayers.add(sessionPlayers.get(i));
			sessionPlayers.get(i).setInRound(true, true);
		}

		// Falls man mit blind Levels spielt und es Zeit ist, wird der Dialog
		// zum Setzen der Blinds aufgerufen

		if (isBlindLevels) {

			if (System.currentTimeMillis() >= blindIncreaseTime) {
				callBlindIncreaseMenu();
				blindIncreaseTime = blindIncreaseTime + blindLevelTime;
			}

		}

		// Setzen des Dealers

		dealer.setDealer(false);

		if (newDealer == null) {
			setDealer(sessionPlayers.get(sessionPlayers.indexOf(dealer) + 1));

		} else {
			setDealer(newDealer);
			dealerNum = newDealer;
			newDealer = null;
		}

		// Setzen der Phase
		phase = 0;

		// Neusetzen der Karten
		flop = new Flop();
		turn = new Turn();
		river = new River();

		you.setHand(new HandCards());

		// Setzen der Usertasten

		setCards.setEnabled(true);
		setCards.setText(R.string.set_hand);

		raiseBet.setEnabled(true);
		raiseBet.setText(R.string.raise);

		callCheck.setEnabled(true);
		callCheck.setText(R.string.call);

		fold.setEnabled(true);

		cardsSet = false;

		calcOddDone = false;

		TextView oddText = (TextView) findViewById(R.id.textViewOdd);
		TextView adviceText = (TextView) findViewById(R.id.textViewSuggest);

		oddText.setText("");
		adviceText.setText("");

	}

	private void determineWinner() {

		// Für den Fall, dass jemand ALLIN ging unmittelbar bevor
		// determineWinner gerufen wurde

		for (int i = 0; i < playingPlayers.size(); i++) {

			Player player = playingPlayers.get(i);

			if (player.isWentAllIn()) {
				calcPotShare(player);
				player.setWentAllIn(false);
			}

			if (player.getPotShare() == 0) {
				player.setPotShare(potWithBets);
			}

		}

		// Ab hier: normaler Fall ohne Allins und Sidepots
		for (int i = 0; i < playingPlayers.size(); i++) {

			playingPlayers.get(i).setFocused();

		}

		Toast.makeText(getApplicationContext(), R.string.winner_select,
				Toast.LENGTH_LONG).show();
		choosingWinner = true;

		// Deaktivieren der Userbuttons; SetCards wird zu SplitPot
		callCheck.setEnabled(false);
		raiseBet.setEnabled(false);
		fold.setEnabled(false);

		setCards.setText(R.string.split_pot);
		setCards.setEnabled(true);

	}

	private void dealHand() {

		cardsSet = true;

		// if (activePlayers.size() == 0) {
		// transferPhase();
		// }

		getCardFromUser(CARD_REQUEST_H1, 1);

		getCardFromUser(CARD_REQUEST_H2, 2);

	}

	private void dealFlop() {

		getCardFromUser(CARD_REQUEST_F1, 5);

		getCardFromUser(CARD_REQUEST_F2, 4);

		getCardFromUser(CARD_REQUEST_F3, 3);

		cardsSet = true;

	}

	private void dealTurn() {

		getCardFromUser(CARD_REQUEST_T, 6);

		cardsSet = true;

	}

	private void dealRiver() {
		getCardFromUser(CARD_REQUEST_R, 7);

		cardsSet = true;

	}

	private void startDealerSetting() {

		Toast.makeText(getApplicationContext(), R.string.select_dealer_toast,
				Toast.LENGTH_SHORT).show();

		if (setCards.isEnabled()) {
			setCards.setEnabled(false);
			activateSet = true;
		}
		menu.setEnabled(false);

		if (callCheck.isEnabled()) {
			callCheck.setEnabled(false);
			activateCall = true;
		}

		if (raiseBet.isEnabled()) {
			raiseBet.setEnabled(false);
			activateRaise = true;
		}

		if (fold.isEnabled()) {
			fold.setEnabled(false);

			activateFold = true;
		}

		for (int i = 0; i < sessionPlayers.size(); i++) {

			Player player = sessionPlayers.get(i);

			player.setFocused();

		}

		settingDealer = true;

	}

	private void adjustDealer(Player player) {

		newDealer = player;

		if (activateSet) {
			setCards.setEnabled(true);
			activateSet = false;
		}

		menu.setEnabled(true);

		if (activateFold) {
			fold.setEnabled(true);
			activateFold = false;
		}

		if (activateCall) {
			callCheck.setEnabled(true);
			activateCall = false;
		}

		if (activateRaise) {
			activateRaise = false;
			raiseBet.setEnabled(true);
		}

		if (phase == 0
				&& potWithBets <= bigBlind + smallBlind + sessionPlayers.size()
						* ante) {
			setDealer(newDealer);
			dealerNum = newDealer;
			newDealer = null;
		}
	}

	private void setDealer(Player dealNum) {

		// Vorbereitung: alle Spieler nicht Dealer, alle bets auf 0

		currentBet = 0;
		potWithBets = 0;
		potWithBetsBox.setText("");

		for (int i = 0; i < sessionPlayers.size(); i++) {

			Player player = sessionPlayers.get(i);
			player.setDealer(false);
			player.setOnTurn(false);
			if (player.getBet() != 0) {
				player.setMoney(player.getMoney() + player.getBet());
				player.setBet(0);

			}

		}

		// Setzen des Dealers

		Player newDealer = dealNum;

		this.dealerNum = newDealer;

		newDealer.setDealer(true);

		// Setzen von Big- und Small Blind, ante und des currentBet

		for (int i = 0; i < sessionPlayers.size(); i++) {

			bet(sessionPlayers.get(i), ante);

		}

		if (sessionPlayers.size() == 2) {

			bet(sessionPlayers.get(sessionPlayers.indexOf(dealNum) + 2),
					smallBlind);
			bet(sessionPlayers.get(sessionPlayers.indexOf(dealNum) + 1),
					bigBlind);
			currentBet = bigBlind + ante;
		} else {
			bet(sessionPlayers.get(sessionPlayers.indexOf(dealNum) + 1),
					smallBlind);
			bet(sessionPlayers.get(sessionPlayers.indexOf(dealNum) + 2),
					bigBlind);
			currentBet = bigBlind + ante;
		}

		// Setzen wer jetzt dran ist

		if (sessionPlayers.size() == 2) {
			dealNum.setOnTurn(true);
			onTurnNum = dealNum;
		} else {
			sessionPlayers.get(sessionPlayers.indexOf(dealNum) + 3).setOnTurn(
					true);
			onTurnNum = sessionPlayers.get(sessionPlayers.indexOf(dealNum) + 3);
		}

	}

	private void rebuyPlayer(int rebuyPlayerNum, int rebuyStack) {

		sessionPlayers.add(rebuyPlayerNum, playerButtons.get(rebuyPlayerNum));

		Player player = sessionPlayers.get(rebuyPlayerNum);
		player.setMoney(rebuyStack);
		player.setStartAmount(player.getStartAmount() + rebuyStack);
		player.updateStats();

		if (phase == 0
				&& potWithBets <= smallBlind + bigBlind + sessionPlayers.size()
						* ante) {

			playingPlayers.add(sessionPlayers.indexOf(player), player);
			activePlayers.add(sessionPlayers.indexOf(player), player);
			player.setInRound(true, true);
			setDealer(dealerNum);

		} else {

			player.unfocus();
		}

	}

	private void removePlayer(int removePlayerNum) {

		playerButtons.remove(removePlayerNum);

		playerNum--;

		setContentView(getLayoutId(playerNum));

	}

	private void calcPotShare(Player player) {

		int potShare = pot;
		int ownBet = player.getBet();

		for (int i = 0; i < sessionPlayers.size(); i++) {

			Player betPlayer = sessionPlayers.get(i);
			int otherBet = betPlayer.getBet();

			if (otherBet <= ownBet) {
				potShare = potShare + otherBet;
			} else {
				potShare = potShare + ownBet;
			}

		}

		player.setPotShare(potShare);
	}

	private void callPlayerOutMenu(int playerNum) {

		Intent intent = new Intent(getApplicationContext(),
				PsMenuPlayerOut.class);

		intent.putExtra(PreferenceContainer.KEY_PS_MENU_PLOUT_START_AMOUNT,
				startAmount);
		intent.putExtra(PreferenceContainer.KEY_PS_MENU_PLOUT_PL_NUM, playerNum);

		startActivityForResult(intent, MENU_PLAYER_OUT_REQUEST);

	}

	private void callPlayerInMenu(int playerNum) {

		Intent intent = new Intent(getApplicationContext(),
				PsMenuPlayerIn.class);

		String plName = playerButtons.get(playerNum).getName();
		int chips = playerButtons.get(playerNum).getMoney();
		Player player = playerButtons.get(playerNum);

		intent.putExtra(PreferenceContainer.KEY_PS_MENU_PLIN_NAME, plName);
		intent.putExtra(PreferenceContainer.KEY_PS_MENU_PLIN_PL_CHIPS, chips);
		intent.putExtra(PreferenceContainer.KEY_PS_MENU_PLIN_PL_NUM, playerNum);
		intent.putExtra(
				PreferenceContainer.KEY_PS_MENU_PLIN_AVERAGE_CHIP_AMOUNT,
				player.getAverage());
		intent.putExtra(PreferenceContainer.KEY_PS_MENU_PLIN_BALANCE,
				player.getBalance());
		intent.putExtra(PreferenceContainer.KEY_PS_MENU_PLIN_HANDS_PLAYED,
				player.getHandsPlayedPer());
		intent.putExtra(PreferenceContainer.KEY_PS_MENU_PLIN_HANDS_WON,
				player.getHandsWonPer());
		intent.putExtra(PreferenceContainer.KEY_PS_MENU_PLIN_HANDS_WON_OVERALL,
				player.getHandsWonOverallPer());
		intent.putExtra(PreferenceContainer.KEY_PS_MENU_PLIN_MAX_CHIP_AMOUNT,
				player.getMaxAmount());
		intent.putExtra(PreferenceContainer.KEY_PS_MENU_PLIN_MIN_CHIP_AMOUNT,
				player.getMinAmount());

		startActivityForResult(intent, MENU_PLAYER_IN_REQUEST);

	}

	private void callBlindIncreaseMenu() {

		Intent intent = new Intent(getApplicationContext(),
				PsMenuBlindActivity.class);

		intent.putExtra(PreferenceContainer.keyCurrentBB, bigBlind);
		intent.putExtra(PreferenceContainer.keyCurrentSB, smallBlind);
		intent.putExtra(PreferenceContainer.keyCurrentAnte, ante);

		startActivityForResult(intent, BLIND_ADJUST_REQUEST);

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

			you.setCard1(new Card(color, value, this));

			if (playingPlayers.contains(you)) {
				callCalc();
			}
		}

		if (requestCode == CARD_REQUEST_H2 && resultCode == RESULT_OK) {

			int color = data.getExtras().getInt(PreferenceContainer.keyColor);
			int value = data.getExtras().getInt(PreferenceContainer.keyValue);

			you.setCard2(new Card(color, value, this));

			if (playingPlayers.contains(you)) {
				callCalc();
			}

		}

		if (requestCode == CARD_REQUEST_F1 && resultCode == RESULT_OK) {

			int color = data.getExtras().getInt(PreferenceContainer.keyColor);
			int value = data.getExtras().getInt(PreferenceContainer.keyValue);

			flop.setCard1(new Card(color, value, this));

			setButtonColor(flopBox3, color);
			setButtonText(flopBox3, flop.getCard1());
			flopBox3.setEnabled(true);

			if (playingPlayers.contains(you)) {
				callCalc();
			}

		}

		if (requestCode == CARD_REQUEST_F2 && resultCode == RESULT_OK) {

			int color = data.getExtras().getInt(PreferenceContainer.keyColor);
			int value = data.getExtras().getInt(PreferenceContainer.keyValue);

			flop.setCard2(new Card(color, value, this));

			setButtonColor(flopBox2, color);
			setButtonText(flopBox2, flop.getCard2());
			flopBox2.setEnabled(true);

			if (playingPlayers.contains(you)) {
				callCalc();
			}

		}

		if (requestCode == CARD_REQUEST_F3 && resultCode == RESULT_OK) {

			int color = data.getExtras().getInt(PreferenceContainer.keyColor);
			int value = data.getExtras().getInt(PreferenceContainer.keyValue);

			flop.setCard3(new Card(color, value, this));

			setButtonColor(flopBox1, color);
			setButtonText(flopBox1, flop.getCard3());
			flopBox1.setEnabled(true);

			if (playingPlayers.contains(you)) {
				callCalc();
			}

		}

		if (requestCode == CARD_REQUEST_T && resultCode == RESULT_OK) {

			int color = data.getExtras().getInt(PreferenceContainer.keyColor);
			int value = data.getExtras().getInt(PreferenceContainer.keyValue);

			turn.dealCard(new Card(color, value, this));

			setButtonColor(turnBox, color);
			setButtonText(turnBox, turn.getCard());
			turnBox.setEnabled(true);

			if (playingPlayers.contains(you)) {
				callCalc();
			}
		}

		if (requestCode == CARD_REQUEST_R && resultCode == RESULT_OK) {

			int color = data.getExtras().getInt(PreferenceContainer.keyColor);
			int value = data.getExtras().getInt(PreferenceContainer.keyValue);

			river.dealCard(new Card(color, value, this));

			setButtonColor(riverBox, color);
			setButtonText(riverBox, river.getCard());
			riverBox.setEnabled(true);

			if (playingPlayers.contains(you)) {
				callCalc();
			}

		}

		if (requestCode == BET_REQUEST && resultCode == RESULT_OK) {

			int receivedBet = data.getExtras().getInt(
					PreferenceContainer.keyPickedValue);

			Player now = onTurnNum;
			now.setPlayingHand(true);

			// All in
			if (receivedBet == -100) {
				goAllIn(now);
				now.setWentAllIn(true);

			} else {

				bet(now, receivedBet - now.getBet());
				currentBet = receivedBet;

				// Der BB sollte auf jeden Fall mit aufgenommen werden

				boolean BBin = false;

				for (int i = 0; i < madeBets.length; i++) {

					if (madeBets[i] == bigBlind) {
						BBin = true;
					}

				}

				if (!BBin) {
					madeBets[0] = bigBlind;
				}

				if (receivedBet > bigBlind) {
					// An der Stelle: jetziger Bet größer als Big Blind, der
					// gerade herrscht, also wird geprüft, ob er aufgenommen
					// wird.

					// 1) Schauen, ob die Bet nicht schon drin ist und ob
					// raushauen von denen, die zu klein sind

					boolean alreadyIncluded = false;

					for (int i = 0; i < madeBets.length; i++) {

						if (madeBets[i] == currentBet) {
							alreadyIncluded = true;
						}

						if (madeBets[i] < bigBlind) {
							madeBets[i] = 0;
						}

					}

					if (!alreadyIncluded) {

						for (int i = 0; i < madeBets.length; i++) {

							if (madeBets[i] == 0) {
								madeBets[i] = currentBet;
								break;
							}

						}

					}
				}

				sortArray(madeBets);

			}

			// Alle Spieler, die bei der Runde noch dabei sind, aktivieren

			activePlayers.clear();

			for (int i = 0; i < playingPlayers.size(); i++) {

				if (playingPlayers.get(i).isInRound()) {
					activePlayers.add(playingPlayers.get(i));
				}

			}

			onTurnNum = now;

			if (now.getMoney() == 0) {
				now.setInRound(false, true);
			}

			adjustBetLimitAdvice(odd);

			transferTurn();
		}

		if (requestCode == MENU_REQUEST && resultCode == RESULT_OK) {

			boolean leave = data.getBooleanExtra(
					PsMenuActivity.KEY_CLOSE_PS_BOOL, false);

			if (leave) {
				finish();
			}

			boolean newDealer = data.getBooleanExtra(
					PsMenuActivity.KEY_SET_DEALER_BOOL, false);

			if (newDealer) {
				startDealerSetting();
			}

			boolean setNewBlinds = data.getBooleanExtra(
					PsMenuActivity.KEY_SET_NEW_BLIND_BOOL, false);

			if (setNewBlinds) {
				bigBlind = data.getIntExtra(PreferenceContainer.keyCurrentBB,
						bigBlind);
				smallBlind = data.getIntExtra(PreferenceContainer.keyCurrentSB,
						smallBlind);
				ante = data.getIntExtra(PreferenceContainer.keyCurrentAnte,
						ante);

				if (phase == 0
						&& potWithBets <= bigBlind + smallBlind
								+ sessionPlayers.size() * ante) {
					setDealer(dealerNum);
				}
			}

		}

		if (requestCode == MENU_PLAYER_OUT_REQUEST && resultCode == RESULT_OK) {

			boolean doRebuy = data.getBooleanExtra(
					PsMenuPlayerOut.KEY_REBUY_BOOL, false);
			int rebuyStack = data.getIntExtra(PsMenuPlayerOut.KEY_REBUY_INT,
					startAmount);
			int rebuyPlayerNum = data.getIntExtra(
					PreferenceContainer.KEY_PS_MENU_PLOUT_PL_NUM, 0);

			boolean doRemove = data.getBooleanExtra(
					PsMenuPlayerOut.KEY_REMOVE_BOOL, false);

			if (doRebuy) {
				rebuyPlayer(rebuyPlayerNum, rebuyStack);
			} else if (doRemove) {
				removePlayer(rebuyPlayerNum);
			}

		}

		if (requestCode == MENU_PLAYER_IN_REQUEST && resultCode == RESULT_OK) {

			boolean doChanges = data.getBooleanExtra(
					PsMenuPlayerIn.KEY_DO_CHANGE_BOOL, false);

			boolean doRemove = data.getBooleanExtra(
					PsMenuPlayerIn.KEY_DO_REMOVE_PLAYER, false);

			if (doChanges) {

				String newName = data
						.getStringExtra(PreferenceContainer.KEY_PS_MENU_PLIN_NAME);
				int newChips = data.getIntExtra(
						PreferenceContainer.KEY_PS_MENU_PLIN_PL_CHIPS, 0);
				int editPlayerNum = data.getIntExtra(
						PreferenceContainer.KEY_PS_MENU_PLIN_PL_NUM, 0);

				Player player = playerButtons.get(editPlayerNum);

				player.setName(newName);
				player.setMoney(newChips);

			}

			if (doRemove) {
				int editPlayerNum = data.getIntExtra(
						PreferenceContainer.KEY_PS_MENU_PLIN_PL_NUM, 0);
				Player player = playerButtons.get(editPlayerNum);

				player.setMoney(0);

				if (player.isDealer()) {
					player.setDealer(false);

					Player nextDealer = sessionPlayers.get(sessionPlayers
							.indexOf(player) + 1);

					nextDealer.setDealer(true);
				}

				if (player instanceof You) {
					((You) player).getOptions().setBackgroundResource(
							R.drawable.button_ps_player_out_of_game);
					((You) player).getHand1().setAlpha(0);
					((You) player).getHand1().setEnabled(false);
					((You) player).getHand2().setAlpha(0);
					((You) player).getHand2().setEnabled(false);
				}

				if (player instanceof OtherPlayer) {
					((OtherPlayer) player).getButton().setBackgroundResource(
							R.drawable.button_ps_player_out_of_game);
					((OtherPlayer) player).getButton().setImageDrawable(
							getResources().getDrawable(
									R.drawable.ps_player_fold));
				}

				sessionPlayers.remove(player);
				playingPlayers.remove(player);
				activePlayers.remove(player);

			}

		}

		if (requestCode == BLIND_ADJUST_REQUEST && resultCode == RESULT_OK) {
			bigBlind = data.getIntExtra(PreferenceContainer.keyCurrentBB,
					bigBlind);
			smallBlind = data.getIntExtra(PreferenceContainer.keyCurrentSB,
					smallBlind);
			ante = data.getIntExtra(PreferenceContainer.keyCurrentAnte, ante);

			if (phase == 0
					&& potWithBets <= bigBlind + smallBlind
							+ sessionPlayers.size() * ante) {
				setDealer(dealerNum);
			}
		}

	}

	private void illustrateOdd(int odd) {

		this.odd = odd;

		TextView oddView = (TextView) findViewById(R.id.textViewOdd);

		if (!playingPlayers.contains(you) || !calcOddDone) {

			oddView.setText("");
			return;
		}

		oddView.setText(String.valueOf(odd) + " %");

		if (odd < 25) {
			oddView.setTextColor(getResources().getColor(R.color.RED));
		} else if (odd < 50) {
			oddView.setTextColor(getResources().getColor(R.color.WHITE));
		} else if (odd < 75) {
			oddView.setTextColor(getResources().getColor(
					R.color.android_holo_blue_bright));
		} else {
			oddView.setTextColor(getResources().getColor(R.color.GREEN));
		}

	}

	private void adjustBetLimitAdvice(int odd) {

		TextView adviceText = (TextView) findViewById(R.id.textViewSuggest);

		if (!playingPlayers.contains(you) || !calcOddDone) {

			adviceText.setText("");
			return;
		}

		int betLimit = 0;

		if (phase == 0 && activePlayers.size() > 2
				&& currentBet <= 2 * bigBlind) {

			// Fall, dass es vor dem Flop ist und es neben Big und small Blind
			// noch genug Spieler gibt

			int theoPot = potWithBets + currentBet * (activePlayers.size() / 2);

			betLimit = theoPot * odd / 100;

		} else {

			// Nach dem Flop

			betLimit = (potWithBets * odd) / 100;

		}

		String advice = "";

		if (phase == 0 && currentBet <= bigBlind * 2) {

			if (odd < 15) {
				advice = getResources().getString(R.string.fold);
				adviceText.setTextColor(getResources().getColor(R.color.RED));
			} else if (odd < 20) {
				advice = getResources().getString(R.string.call);
				adviceText.setTextColor(getResources().getColor(
						R.color.android_holo_blue_bright));
			} else {
				if (currentBet == 0) {
					advice = getResources().getString(R.string.bet);
				} else {

					advice = getResources().getString(R.string.raise);
				}
				adviceText.setTextColor(getResources().getColor(R.color.GREEN));
			}

		} else {

			// Nicht preflop
			if (betLimit < currentBet) {
				advice = getResources().getString(R.string.fold);
				adviceText.setTextColor(getResources().getColor(R.color.RED));
			} else {

				if (currentBet > 0) {
					if (betLimit > 1.5 * currentBet) {

						advice = getResources().getString(R.string.raise);

						adviceText.setTextColor(getResources().getColor(
								R.color.GREEN));
					} else {
						advice = getResources().getString(R.string.call);
						adviceText.setTextColor(getResources().getColor(
								R.color.android_holo_blue_bright));
					}
				} else {

					if (odd > 40) {
						advice = getResources().getString(R.string.bet);
						adviceText.setTextColor(getResources().getColor(
								R.color.GREEN));
					} else {
						advice = getResources().getString(R.string.check);
						adviceText.setTextColor(getResources().getColor(
								R.color.WHITE));
					}

				}

			}
		}

		String text;

		text = String.valueOf(betLimit) + " (" + advice + ")";

		adviceText.setText(text);

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

	@Override
	public void finish() {

		resetCards();

		super.finish();
	}

	private void resetCards() {

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

	private Player findPlayerForView(ImageButton button) {

		int id = button.getId();

		switch (id) {

		case R.id.ps_imageButton_p1:

			return playerButtons.get(1);

		case R.id.ps_imageButton_p2:

			return playerButtons.get(2);

		case R.id.ps_imageButton_p3:

			return playerButtons.get(3);

		case R.id.ps_imageButton_p4:

			return playerButtons.get(4);

		case R.id.ps_imageButton_p5:

			return playerButtons.get(5);

		case R.id.ps_imageButton_p6:

			return playerButtons.get(6);

		case R.id.ps_imageButton_p7:
			return playerButtons.get(7);

		case R.id.ps_imageButton_p8:
			return playerButtons.get(8);

		case R.id.ps_imageButton_p9:
			return playerButtons.get(9);

		default:
			return null;

		}

	}

	private int getLayoutId(int playerNum) {
		int id;

		switch (playerNum) {

		case 2:
			id = R.layout.activity_poker_session_2pl;
			break;

		case 3:
			id = R.layout.activity_poker_session_3pl;
			break;

		case 4:
			id = R.layout.activity_poker_session_4pl;
			break;

		case 5:
			id = R.layout.activity_poker_session_5pl;
			break;

		case 6:
			id = R.layout.activity_poker_session_6pl;
			break;

		case 7:
			id = R.layout.activity_poker_session_7pl;
			break;
		case 8:
			id = R.layout.activity_poker_session_8pl;
			break;
		case 9:
			id = R.layout.activity_poker_session_9pl;
			break;

		case 10:
			id = R.layout.activity_poker_session_10pl;
			break;

		default:
			id = 0;

		}

		return id;
	}

	private void sortArray(int[] array) {

		boolean changed = true;

		while (changed) {

			changed = false;

			for (int i = 0; i < array.length - 1; i++) {
				if (array[i] > array[i + 1]) {
					int a = array[i];
					array[i] = array[i + 1];
					array[i + 1] = a;
					changed = true;
				}
			}

		}
	}

}
