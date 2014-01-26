package com.FedorApps.pokerassistant.pokerSession;

import com.FedorApps.pokerassistant.Card;
import com.FedorApps.pokerassistant.HandCards;
import com.FedorApps.pokerassistant.R;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

public class You extends Player {

	// Die Elemente im Lyout, die dem Spieler zugeordner sind
	private Button hand1;
	private Button hand2;
	private Button options;
	private HandCards hand;

	// --------------------------------------------------------

	// Zustände

	private Activity activity;

	public You(int buyIn, Activity activity) {

		super(buyIn);

		this.activity = activity;

		// Zuweisen der Layout Elemente

		hand1 = (Button) activity.findViewById(R.id.ps_button_hand1);
		hand2 = (Button) activity.findViewById(R.id.ps_button_hand2);
		options = (Button) activity.findViewById(R.id.ps_button_youSettings);

		betBox = (TextView) activity.findViewById(R.id.ps_textView_bet_you);
		moneyBox = (TextView) activity.findViewById(R.id.ps_textView_money_you);
		dealerBox = (TextView) activity.findViewById(R.id.ps_dealer_you);

		// ------------------------------------------------------------

		// Initialisieren der Werte
		setMoney(buyIn);
		setBet(0);
		hand1.setBackgroundResource(R.drawable.card_back_small);
		hand2.setBackgroundResource(R.drawable.card_back_small);
		hand1.setEnabled(false);
		hand2.setEnabled(false);
		hand = new HandCards();
		// -----------------------------------------------------------

	}

	public HandCards getHand() {
		return hand;
	}

	@Override
	public void setInRound(boolean isInRound, boolean staysInGame) {

		super.setInRound(isInRound, staysInGame);

		if (isInRound) {
			hand1.setBackgroundResource(R.drawable.card_back_small);
			hand1.setText("");
			hand1.setAlpha(1);
			hand1.setEnabled(false);
			hand2.setBackgroundResource(R.drawable.card_back_small);
			hand2.setText("");
			hand2.setAlpha(1);
			hand2.setEnabled(false);
			hand = new HandCards();
		} else if (!staysInGame) {

			hand1.setAlpha(0);
			hand2.setAlpha(0);

			hand1.setEnabled(false);
			hand2.setEnabled(false);
			hand = new HandCards();
		}
	}

	@Override
	public void setOnTurn(boolean isOnTurn) {

		super.setOnTurn(isOnTurn);

		if (isOnTurn) {
			options.setBackgroundResource(R.drawable.button_ps_player_on_turn);
		} else {
			options.setBackgroundResource(R.drawable.button_ps_player);
		}
	}

	public void setCard1(Card card) {
		hand.setCard1(card);
		setButtonColor(hand2, hand.getCard1().getColor());
		setButtonText(hand2, hand.getCard1());
		hand2.setEnabled(true);

	}

	public void setCard2(Card card) {
		hand.setCard2(card);
		setButtonColor(hand1, hand.getCard2().getColor());
		setButtonText(hand1, card);
		hand1.setEnabled(true);
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

		button.setTextColor(activity.getResources().getColor(R.color.WHITE));

		button.setText(card.getShortValueString());
	}

	public void setHand(HandCards hand) {
		this.hand = hand;
	}

	@Override
	public void setFocused() {

		options.setBackgroundResource(R.drawable.button_ps_player_on_turn);

	}

	@Override
	public void unfocus() {

		options.setBackgroundResource(R.drawable.button_ps_player);

	}

	public Button getOptions() {
		return options;
	}

	public Button getHand1() {
		return hand1;
	}

	public Button getHand2() {
		return hand2;
	}

	public void setSelected() {

		if (!selected) {
			options.setBackgroundResource(R.drawable.ps_player_selected);
			selected = true;
		} else {
			options.setBackgroundResource(R.drawable.button_ps_player_on_turn);
			selected = false;
		}

	}

	@Override
	public void unselect() {
		selected = false;
		options.setBackgroundResource(R.drawable.button_ps_player);
	}

}
