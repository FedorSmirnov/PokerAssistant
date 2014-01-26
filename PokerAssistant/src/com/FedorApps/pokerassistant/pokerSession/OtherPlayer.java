package com.FedorApps.pokerassistant.pokerSession;

import com.FedorApps.pokerassistant.R;

import android.app.Activity;
import android.widget.ImageButton;
import android.widget.TextView;

public class OtherPlayer extends Player {

	// Elemente im Layout, die dem Spieler zugeordnet sind
	private TextView nameBox;

	private ImageButton button;

	// ----------------------------------------------------

	// Zustände

	Activity activity;

	public OtherPlayer(int buyIn) {
		super(buyIn);
	}

	public OtherPlayer(int buyIn, String name, int num, Activity activity) {

		// Zuweisen der Zeiger auf die Elemente im Layout

		super(buyIn);
		this.num = num;
		this.activity = activity;

		switch (num) {

		case 1:
			nameBox = (TextView) activity
					.findViewById(R.id.ps_textView_name_p1);
			moneyBox = (TextView) activity
					.findViewById(R.id.ps_textView_money_p1);
			betBox = (TextView) activity.findViewById(R.id.ps_textView_bet_p1);
			dealerBox = (TextView) activity.findViewById(R.id.ps_dealer_p1);

			if (activity instanceof PokerSession) {

				button = (ImageButton) activity
						.findViewById(R.id.ps_imageButton_p1);
			}
			break;

		case 2:
			nameBox = (TextView) activity
					.findViewById(R.id.ps_textView_name_p2);
			moneyBox = (TextView) activity
					.findViewById(R.id.ps_textView_money_p2);
			betBox = (TextView) activity.findViewById(R.id.ps_textView_bet_p2);
			dealerBox = (TextView) activity.findViewById(R.id.ps_dealer_p2);
			button = (ImageButton) activity
					.findViewById(R.id.ps_imageButton_p2);
			break;

		case 3:
			nameBox = (TextView) activity
					.findViewById(R.id.ps_textView_name_p3);
			moneyBox = (TextView) activity
					.findViewById(R.id.ps_textView_money_p3);
			betBox = (TextView) activity.findViewById(R.id.ps_textView_bet_p3);
			dealerBox = (TextView) activity.findViewById(R.id.ps_dealer_p3);
			button = (ImageButton) activity
					.findViewById(R.id.ps_imageButton_p3);
			break;

		case 4:
			nameBox = (TextView) activity
					.findViewById(R.id.ps_textView_name_p4);
			moneyBox = (TextView) activity
					.findViewById(R.id.ps_textView_money_p4);
			betBox = (TextView) activity.findViewById(R.id.ps_textView_bet_p4);
			dealerBox = (TextView) activity.findViewById(R.id.ps_dealer_p4);
			button = (ImageButton) activity
					.findViewById(R.id.ps_imageButton_p4);
			break;

		case 5:
			nameBox = (TextView) activity
					.findViewById(R.id.ps_textView_name_p5);
			moneyBox = (TextView) activity
					.findViewById(R.id.ps_textView_money_p5);
			betBox = (TextView) activity.findViewById(R.id.ps_textView_bet_p5);
			dealerBox = (TextView) activity.findViewById(R.id.ps_dealer_p5);
			button = (ImageButton) activity
					.findViewById(R.id.ps_imageButton_p5);
			break;

		case 6:
			nameBox = (TextView) activity
					.findViewById(R.id.ps_textView_name_p6);
			moneyBox = (TextView) activity
					.findViewById(R.id.ps_textView_money_p6);
			betBox = (TextView) activity.findViewById(R.id.ps_textView_bet_p6);
			dealerBox = (TextView) activity.findViewById(R.id.ps_dealer_p6);
			button = (ImageButton) activity
					.findViewById(R.id.ps_imageButton_p6);
			break;

		case 7:
			nameBox = (TextView) activity
					.findViewById(R.id.ps_textView_name_p7);
			moneyBox = (TextView) activity
					.findViewById(R.id.ps_textView_money_p7);
			betBox = (TextView) activity.findViewById(R.id.ps_textView_bet_p7);
			dealerBox = (TextView) activity.findViewById(R.id.ps_dealer_p7);
			button = (ImageButton) activity
					.findViewById(R.id.ps_imageButton_p7);
			break;

		case 8:
			nameBox = (TextView) activity
					.findViewById(R.id.ps_textView_name_p8);
			moneyBox = (TextView) activity
					.findViewById(R.id.ps_textView_money_p8);
			betBox = (TextView) activity.findViewById(R.id.ps_textView_bet_p8);
			dealerBox = (TextView) activity.findViewById(R.id.ps_dealer_p8);
			button = (ImageButton) activity
					.findViewById(R.id.ps_imageButton_p8);
			break;

		case 9:

			nameBox = (TextView) activity
					.findViewById(R.id.ps_textView_name_p9);
			moneyBox = (TextView) activity
					.findViewById(R.id.ps_textView_money_p9);
			betBox = (TextView) activity.findViewById(R.id.ps_textView_bet_p9);
			dealerBox = (TextView) activity.findViewById(R.id.ps_dealer_p9);
			button = (ImageButton) activity
					.findViewById(R.id.ps_imageButton_p9);
			break;

		}

		// Zuweisung abgeschlossen.

		// Setzen der Anfangswerte

		setMoney(buyIn);
		setBet(0);
		setName(name);

	}

	public void setName(String name) {

		super.setName(name);

		nameBox.setText(name);

	}

	@Override
	public void setOnTurn(boolean isOnTurn) {
		super.setOnTurn(isOnTurn);

		if (isOnTurn) {
			button.setBackgroundResource(R.drawable.button_ps_player_on_turn);
		} else {
			button.setBackgroundResource(R.drawable.button_ps_player);

		}
	}

	@Override
	public void setInRound(boolean isInRound, boolean staysInGame) {

		super.setInRound(isInRound, staysInGame);

		if (isInRound) {
			button.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.ps_player_unfold));
		} else if (!staysInGame) {
			button.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.ps_player_fold));
		}
	}

	@Override
	public void setFocused() {

		button.setBackgroundResource(R.drawable.button_ps_player_on_turn);

	}

	@Override
	public void unfocus() {
		button.setBackgroundResource(R.drawable.button_ps_player);
		button.setImageDrawable(activity.getResources().getDrawable(
				R.drawable.ps_player_fold));
	}

	public ImageButton getButton() {
		return button;
	}

	public void setSelected() {

		if (!selected) {
			button.setBackgroundResource(R.drawable.ps_player_selected);
			selected = true;
		} else {
			button.setBackgroundResource(R.drawable.button_ps_player_on_turn);
			selected = false;
		}

	}

	@Override
	public void unselect() {
		selected = false;
		button.setBackgroundResource(R.drawable.button_ps_player);
	}

}
