package com.FedorApps.pokerassistant;

import com.FedorApps.pokerassistant.cardCombos.MonteCarloOddCalculator;
import com.FedorApps.pokerassistant.practiceMatch.PracticeMatch;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class OddCalculatorService extends Service {

	// Der Binder, den eine Activity beim Binden kriegt, um mit dem Service
	// kommunizieren zu können
	private Messenger messenger = new Messenger(new IncomingHandler());

	// Messenger, über den der Service mit der Activity kommunizieren kann.
	private Messenger messengerToActivity = null;

	// String für die Übergabe der Odds
	private static final String oddKey = "Key_For_The_Odds";

	// Die Strings, die der Service braucht, um die gesetzten Karten auszulesen.

	private static final String keyColorFirst = "FRAG_VAL_COLOR_FIRST";
	private static final String keyValueFirst = "FRAG_VAL_VALUE_FIRST";

	private static final String keyColorSecond = "FRAG_VAL_COLOR_SECOND";
	private static final String keyValueSecond = "FRAG_VAL_VALUE_SECOND";

	private static final String keyColorThird = "FRAG_VAL_COLOR_THIRD";
	private static final String keyValueThird = "FRAG_VAL_VALUE_THIRD";

	private static final String keyColorFourth = "FRAG_VAL_COLOR_FOURTH";
	private static final String keyValueFourth = "FRAG_VAL_VALUE_FOURTH";

	private static final String keyColorFifth = "FRAG_VAL_COLOR_FIFTH";
	private static final String keyValueFifth = "FRAG_VAL_VALUE_FIFTH";

	private static final String keyColorSixth = "FRAG_VAL_COLOR_SIXTH";
	private static final String keyValueSixth = "FRAG_VAL_VALUE_SIXTH";

	private static final String keyColorSeventh = "FRAG_VAL_COLOR_SEVENTH";
	private static final String keyValueSeventh = "FRAG_VAL_VALUE_SEVENTH";

	private static final String keyColorEighth = "FRAG_VAL_COLOR_EIGHTH";
	private static final String keyValueEighth = "FRAG_VAL_VALUE_EIGHTH";

	private static final String keyOppoNum = "OPPONENT_NUM";

	// Variablen, die der Service braucht, um die Wahrscheinlichkeit zu rechnen.

	private HandCards hand = new HandCards();
	private Flop flop = new Flop();
	private Turn turn = new Turn();
	private River river = new River();
	private int oppoNum;
	private int odd;
	private MonteCarloOddCalculator monty;

	// Variablen, mit denen der Service ankommende Nachrichten liest.

	public static final int MSG_START_CALC = 1000;
	public static final int MSG_ADD_MESSENGER = 2000;
	public static final int MSG_KEEP_CALC = 3000;
	public static final int MSG_VIRT_CALC = 4000;

	private int keepCalcTime = 0;

	// Wichtig: maximale anzahl an Calculations

	private int maxCalcTime = 10;
	private int firstCalcMilSec = 100;
	private int keepCalcMilSec = 500;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return messenger.getBinder();
	}

	class IncomingHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case MSG_START_CALC:

				keepCalcTime = 0;

				readCardsFromMessage(msg.getData());

				monty = new MonteCarloOddCalculator(hand, oppoNum, flop, turn,
						river, firstCalcMilSec);
				odd = monty.getOdd();

				Message msg1 = Message.obtain(null, ProbCalc.MSG_RESULT);
				Bundle data1 = new Bundle();
				data1.putInt(oddKey, odd);
				msg1.setData(data1);

				try {
					messengerToActivity.send(msg1);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;

			case MSG_KEEP_CALC:

				keepCalcTime++;

				if (keepCalcTime > maxCalcTime) {
					return;
				}

				monty.calculateOdds(keepCalcMilSec);
				odd = monty.getOdd();

				Message msg2 = Message.obtain(null, ProbCalc.MSG_RESULT);
				Bundle data2 = new Bundle();
				data2.putInt(oddKey, odd);
				msg2.setData(data2);

				try {
					messengerToActivity.send(msg2);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;

			case MSG_ADD_MESSENGER:

				messengerToActivity = msg.replyTo;
				break;

			case MSG_VIRT_CALC:

				keepCalcTime = 0;

				readCardsFromMessage(msg.getData());

				monty = new MonteCarloOddCalculator(hand, oppoNum, flop, turn,
						river, 2000);
				odd = monty.getOdd();

				Message msgVirt = Message.obtain(null,
						PracticeMatch.MSG_VIRT_RESULT);
				Bundle dataVirt = new Bundle();
				dataVirt.putInt(oddKey, odd);
				msgVirt.setData(dataVirt);

				try {
					messengerToActivity.send(msgVirt);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;

			default:
				super.handleMessage(msg);
			}
		}

	}

	public int giveOdd() {
		return odd;
	}

	private void readCardsFromMessage(Bundle data) {

		setHand(data);
		setFlop(data);
		setTurn(data);
		setRiver(data);
		setOppoNum(data);

	}

	private void setHand(Bundle data) {

		int color = data.getInt(getKeyStringColor(1));
		int value = data.getInt(getKeyStringValue(1));

		Card card1;
		Card card2;

		if (color != 0) {
			card1 = new Card(color, value, null);
		} else {
			card1 = null;
		}

		color = data.getInt(getKeyStringColor(2));
		value = data.getInt(getKeyStringValue(2));

		if (color != 0) {
			card2 = new Card(color, value, null);
		} else {
			card2 = null;
		}

		if (card1 == null || card2 == null) {
			hand.setCard1(null);
			hand.setCard2(null);
		} else {
			hand.setCard1(card1);
			hand.setCard2(card2);
		}

	}

	private void setFlop(Bundle data) {
		int color = data.getInt(getKeyStringColor(3));
		int value = data.getInt(getKeyStringValue(3));

		Card card1;
		Card card2;
		Card card3;

		if (color != 0) {
			card1 = new Card(color, value, null);
		} else {
			card1 = null;
		}

		color = data.getInt(getKeyStringColor(4));
		value = data.getInt(getKeyStringValue(4));

		if (color != 0) {
			card2 = new Card(color, value, null);
		} else {
			card2 = null;
		}

		color = data.getInt(getKeyStringColor(5));
		value = data.getInt(getKeyStringValue(5));

		if (color != 0) {
			card3 = new Card(color, value, null);
		} else {
			card3 = null;
		}

		if (card1 == null || card2 == null || card3 == null) {
			flop.setCard1(null);
			flop.setCard2(null);
			flop.setCard3(null);
		} else {
			flop.setCard1(card1);
			flop.setCard2(card2);
			flop.setCard3(card3);
		}
	}

	private void setTurn(Bundle data) {

		int color = data.getInt(getKeyStringColor(6));
		int value = data.getInt(getKeyStringValue(6));

		Card card1;

		if (color != 0) {
			card1 = new Card(color, value, null);
		} else {
			card1 = null;
		}

		if (card1 == null) {
			turn.dealCard(null);
		} else {
			turn.dealCard(card1);
		}

	}

	private void setRiver(Bundle data) {
		int color = data.getInt(getKeyStringColor(7));
		int value = data.getInt(getKeyStringValue(7));

		Card card1;

		if (color != 0) {
			card1 = new Card(color, value, null);
		} else {
			card1 = null;
		}
		if (card1 == null) {
			river.dealCard(null);
		} else {
			river.dealCard(card1);
		}

	}

	private void setOppoNum(Bundle data) {

		oppoNum = data.getInt(keyOppoNum);

	}

	private String getKeyStringColor(int i) {

		switch (i) {
		case (1):
			return keyColorFirst;

		case (2):
			return keyColorSecond;

		case (3):
			return keyColorThird;

		case (4):
			return keyColorFourth;

		case (5):
			return keyColorFifth;
		case (6):
			return keyColorSixth;
		case (7):
			return keyColorSeventh;
		default:
			return keyColorEighth;
		}

	}

	private String getKeyStringValue(int i) {

		switch (i) {
		case (1):
			return keyValueFirst;

		case (2):
			return keyValueSecond;

		case (3):
			return keyValueThird;

		case (4):
			return keyValueFourth;

		case (5):
			return keyValueFifth;
		case (6):
			return keyValueSixth;
		case (7):
			return keyValueSeventh;
		default:
			return keyValueEighth;
		}

	}

	@Override
	public boolean onUnbind(Intent intent) {

		return super.onUnbind(intent);
	}

}
