package com.FedorApps.pokerassistant;

public class HandCards {

	private Card card1;
	private Card card2;

	public HandCards() {
		card1 = null;
		card2 = null;
	}

	

	public void setCard1(Card card1) {
		this.card1 = card1;
	}



	public void setCard2(Card card2) {
		this.card2 = card2;
	}



	public Card getCard1() {
		return card1;
	}

	public Card getCard2() {
		return card2;
	}

}
