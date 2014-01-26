package com.FedorApps.pokerassistant.practiceMatch;

import android.app.Activity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.FedorApps.pokerassistant.HandCards;
import com.FedorApps.pokerassistant.R;
import com.FedorApps.pokerassistant.pokerSession.OtherPlayer;
import com.FedorApps.pokerassistant.pokerSession.Player;
import com.FedorApps.pokerassistant.pokerSession.PokerSession;

public class VirtualPlayer extends Player {
	
	HandCards hand = new HandCards();
	
	private TextView nameBox;
	
	ImageView card1;
	ImageView card2;
	
	Button statBut;
	
	Activity activity;
	

	// Verhalten, wenn vorne
	private int agressivity;

	// Wie leicht wird gefoldet?
	private int defence;

	public VirtualPlayer(int buyIn, String name, int num, Activity activity) {
		super(buyIn);
		this.num = num;
		this.activity = activity;
		
		
		if (num == 1){
			
			nameBox = (TextView) activity
					.findViewById(R.id.ps_textView_name_p1);
			moneyBox = (TextView) activity
					.findViewById(R.id.ps_textView_money_p1);
			betBox = (TextView) activity.findViewById(R.id.ps_textView_bet_p1);
			dealerBox = (TextView) activity.findViewById(R.id.ps_dealer_p1);

			
			
			
			statBut = (Button) activity.findViewById(R.id.ps_imageButton_p1);
			
			card1 = (ImageView) activity.findViewById(R.id.pm_i_view_op1_c1);
			card2 = (ImageView) activity.findViewById(R.id.pm_i_view_op1_c2);
			
		}
		
		agressivity = 1;
		defence = 1;
		
		setMoney(buyIn);
		setBet(0);
		setName(name);

	}

	public int makeDecision(int odd, int betEdge, int curBet, int bigBlind) {

		if (curBet > betEdge) {

			// fold
			return 0;

		} else if (betEdge > curBet && betEdge - curBet < bigBlind) {
			// check/call
			return 1;
		} else {
			// bet/raise
			return (betEdge - curBet);
		}

	}
	
	public void setOnTurn(boolean isOnTurn) {
		super.setOnTurn(isOnTurn);

		if (isOnTurn) {
			statBut.setBackgroundResource(R.drawable.button_ps_player_on_turn);
		} else {
			statBut.setBackgroundResource(R.drawable.button_ps_player);

		}
	}

	public HandCards getHand() {
		return hand;
	}

	public Button getStatBut() {
		return statBut;
	}
	
	
	
	

}
