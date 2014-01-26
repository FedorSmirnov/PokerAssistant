package com.FedorApps.pokerassistant.cardCombos;

import java.util.ArrayList;

import com.FedorApps.pokerassistant.Card;

public class CardDeck {

	private ArrayList<Card> deck;

	public CardDeck() {
		deck = new ArrayList<Card>();

		for (int i1 = 1; i1 < 5; i1++) {
			for (int i2 = 2; i2 < 15; i2++) {

				Card card = new Card(i1, i2, null);

				deck.add(card);

			}
		}
	}

	public ArrayList<Card> getDeck() {
		return deck;
	}

}
