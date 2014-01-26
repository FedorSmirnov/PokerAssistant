package com.FedorApps.pokerassistant.cardCombos;

import com.FedorApps.pokerassistant.Card;

public abstract class CardCombination {

	private Card[] level = new Card[5];

	public Card[] getLevel() {
		return level;
	}

	public void setLevel(Card[] level) {
		this.level = level;
	}

	public void setCertainLevel(int i, Card card) {
		level[i] = card;
	}

	public Card getCertainLevel(int i) {
		return level[i];
	}
	
	public abstract String toString();
	public abstract int getCOMBORDER();

}
