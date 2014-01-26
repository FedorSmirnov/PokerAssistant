package com.FedorApps.pokerassistant.cardCombos;

import java.util.ArrayList;

import com.FedorApps.pokerassistant.Card;

public class FullHouse extends CardCombination {

	private final int COMBORDER = 7;

	public FullHouse(ArrayList<Card> list) {

		Card current;
		int pos = 0;

		Card firstThree = null;
		Card secondThree = null;

		while (list.size() > 1 && pos < list.size()) {

			Card firstHit = null;
			Card secondHit = null;
			for (int i = pos + 1; i < list.size(); i++) {
				current = list.get(pos);
				// Uebereinstimmung gefunden
				if (current.getValue() == list.get(i).getValue()) {

					if (firstHit == null) {
						firstHit = list.get(i);
					} else {
						secondHit = list.get(i);

						// An dieser Stelle: Drilling gefunden
						if (firstThree == null) {

							// Erster Drilling
							firstThree = current;
							list.remove(firstHit);
							list.remove(secondHit);
							list.remove(current);

							i = pos;
							firstHit = null;
							secondHit = null;
						} else {
							// Zweiter Drilling

							// Erster Drilling
							secondThree = current;
							list.remove(firstHit);
							list.remove(secondHit);
							list.remove(current);

							i = pos + 1;
							firstHit = null;
							secondHit = null;
							break;
						}
					}

				}
			}
			pos++;
		}

		// Man hat den/die Drillinge gefunden

		if (secondThree != null) {
			// Man hat zwei Drillinge gefunden
			if (firstThree.getValue() > secondThree.getValue()) {
				setCertainLevel(0, firstThree);
				setCertainLevel(1, secondThree);
			} else {
				setCertainLevel(0, secondThree);
				setCertainLevel(1, firstThree);
			}
		} else {
			// Man hat einen Drilling gefunden

			setCertainLevel(0, firstThree);

			// Suchen nach dem höchsten Paar im Rest
			pos = 0;
			Card firstPair = null;
			Card secondPair = null;

			while (list.size() > 1 && pos < list.size()) {

				for (int i = pos + 1; i < list.size(); i++) {
					current = list.get(pos);
					if (current.getValue() == list.get(i).getValue()) {
						// Paar gefunden

						if (firstPair == null) {
							// erstes Paar
							firstPair = current;
							list.remove(list.get(i));
							list.remove(current);
							i = pos;
						} else {
							// zweites Paar

							secondPair = current;
							list.remove(list.get(i));
							list.remove(current);
							i = pos;
						}

					}

				}
				pos++;
			}

			// Man hat das/die Paare gefunden

			if (secondPair != null) {

				if (firstPair.getValue() > secondPair.getValue()) {
					setCertainLevel(1, firstPair);
				} else {
					setCertainLevel(1, secondPair);
				}

			} else {
				setCertainLevel(1, firstPair);
			}
		}

	}

	@Override
	public String toString() {

		String result;

		result = "Full House: " + getCertainLevel(0).getValueString()
				+ "s over " + getCertainLevel(1).getValueString() + "s";
		return result;

	}

	@Override
	public int getCOMBORDER() {
		// TODO Auto-generated method stub
		return COMBORDER;
	}

}
