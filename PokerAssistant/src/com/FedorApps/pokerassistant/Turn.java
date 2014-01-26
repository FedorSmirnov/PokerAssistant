package com.FedorApps.pokerassistant;

public class Turn {

	private Card card;

	public Turn() {
		card = null;
	}

	public void dealCard(Card card) {
		this.card = card;
	}

	public Card getCard() {
		return card;
	}

}
