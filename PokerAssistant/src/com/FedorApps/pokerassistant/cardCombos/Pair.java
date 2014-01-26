package com.FedorApps.pokerassistant.cardCombos;

import java.util.ArrayList;

import com.FedorApps.pokerassistant.Card;

public class Pair extends CardCombination {

	private final int COMBORDER = 2;

	public Pair(ArrayList<Card> list) {

		// Man sucht nach dem Paar, setzt es als level1 und sucht aus dem Rest
		// die drei grössten Karten heraus

		Card current;
		boolean pairFound = false;
		int count = 0;
		int pos = 0;
		while (list.size() > 1 && count < 5) {

			current = list.get(pos);
			if (!pairFound) {

				// Paar noch nicht gefunden
				for (int i = pos + 1; i < list.size(); i++) {
					if (current.getValue() == list.get(i).getValue()) {
						setCertainLevel(0, current);
						pairFound = true;

						list.remove(list.get(i));
						list.remove(current);
						count = 2;
						pos = 0;
					}
				}

				if (!pairFound) {
					pos = pos + 1;
				}

			} else {

				// Paar gefunden

				for (int i = pos + 1; i < list.size(); i++) {
					if (current.getValue() < list.get(i).getValue()) {
						current = list.get(i);
					}
				}
				setCertainLevel(count - 1, current);
				list.remove(current);
				count++;

			}

		}

	}

	@Override
	public String toString() {

		String result;

		result = "A pair of " + getCertainLevel(0).getValueString() + "s";
		return result;

	}

	@Override
	public int getCOMBORDER() {

		return COMBORDER;
	}

}
