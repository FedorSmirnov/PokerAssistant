package com.FedorApps.pokerassistant;

public class River {
	
	private Card card;

	public River() {
		card = null;
	}

	public void dealCard(Card card) {
		this.card = card;
	}

	public Card getCard() {
		return card;
	}

}
