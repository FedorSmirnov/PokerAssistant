package com.FedorApps.pokerassistant;

public class Flop {

	private Card card1;
	private Card card2;
	private Card card3;

	public Flop() {
		card1 = null;
		card2 = null;
		card3 = null;
	}

	public void setCard1(Card card1) {
		this.card1 = card1;
	}

	public void setCard2(Card card2) {
		this.card2 = card2;
	}

	public void setCard3(Card card3) {
		this.card3 = card3;
	}

	public Card getCard1() {
		return card1;
	}

	public Card getCard2() {
		return card2;
	}

	public Card getCard3() {
		return card3;
	}

}
