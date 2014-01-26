package com.FedorApps.pokerassistant.cardCombos;

import java.util.ArrayList;

import com.FedorApps.pokerassistant.Card;

public class HighCard extends CardCombination {

	private final int COMBORDER = 1;

	public HighCard(ArrayList<Card> list) {

		Card current;
		int i1 = 0;
		while (list.size() >= 1 && i1 < 5) {

			current = list.get(0);
			for (int i2 = 0; i2 < list.size(); i2++) {

				if (current.getValue() < list.get(i2).getValue()) {
					current = list.get(i2);
				}

			}

			setCertainLevel(i1, current);
			list.remove(current);
			i1++;
		}

	}

	public String toString() {

		String result;

		result = "Highcard " + getCertainLevel(0).getValueString();
		return result;
	}

	public int getCOMBORDER() {
		return COMBORDER;
	}

}
