package com.FedorApps.pokerassistant.cardCombos;

import java.util.ArrayList;

import com.FedorApps.pokerassistant.Card;

public class TwoPairs extends CardCombination {

	private final int COMBORDER = 3;
	private boolean isTwoPairs = false;

	public TwoPairs(ArrayList<Card> list) {

		int firstPair = 0;
		int secondPair = 0;
		int thirdPair = 0;
		int kicker = 0;

		Card firstCard = null;
		Card secondCard = null;
		Card thirdCard = null;
		Card kickerCard;

		Card current = list.get(0);
		int pos = 0;

		// Untersuchen auf Paare

		// While Schleife, solange durchfahren bis entweder die Liste leer oder
		// pos hochgeschaltet
		while (list.size() >= 1 && pos < list.size()) {

			current = list.get(pos);

			// Die ganze Liste wird durchfahren
			for (int i = pos + 1; i < list.size(); i++) {

				// Man findet ein Paar
				if (current.getValue() == list.get(i).getValue()) {
					// Vorher noch kein Paar
					if (firstPair == 0) {
						firstPair = current.getValue();
						firstCard = current;
						list.remove(list.get(i));
						list.remove(current);
						if (list.size() == 0 || pos >= list.size()) {
							break;
						}
						current = list.get(pos);
						i = pos;
					}
					// Vorher schon ein Paar
					else if (secondPair == 0) {
						secondPair = current.getValue();
						secondCard = current;
						list.remove(list.get(i));
						list.remove(current);
						if (list.size() == 0 || pos >= list.size()) {
							break;
						}

						current = list.get(0);
						i = pos;

					} else {
						// Vorher schon zwei Paare
						thirdPair = current.getValue();
						thirdCard = current;
						list.remove(list.get(i));
						list.remove(current);
						if (list.size() == 0 || pos >= list.size()) {
							break;
						}
						current = list.get(pos);
						i = pos;
					}

				}// Kein Paar gefunden
			}

			pos++;

		}

		// Man weiﬂ, ob Paare dabei sind

		// Man hat min. 2 Paare gefunden
		if (firstPair != 0 && secondPair != 0) {

			// Suchen des Kickers
			if (list.size() != 0) {
				current = list.get(0);
			}

			for (int i = 0; i < list.size(); i++) {
				if (current.getValue() < list.get(i).getValue()) {
					current = list.get(i);
				}
			}

			kicker = current.getValue();
			kickerCard = current;

			// Ordnen der ersten zwei Paare
			if (firstPair < secondPair) {

				int place;
				Card placeCard;

				place = firstPair;
				firstPair = secondPair;
				secondPair = place;

				placeCard = firstCard;
				firstCard = secondCard;
				secondCard = placeCard;

			}

			if (thirdPair == 0) {

				setCertainLevel(0, firstCard);
				setCertainLevel(1, secondCard);
				setCertainLevel(2, kickerCard);
				isTwoPairs = true;

			} else {

				if (thirdPair > firstPair) {
					if (secondPair > kicker) {
						kicker = secondPair;
						kickerCard = secondCard;

					}

					secondPair = firstPair;
					secondCard = firstCard;

					firstPair = thirdPair;
					firstCard = thirdCard;
					setCertainLevel(0, firstCard);
					setCertainLevel(1, secondCard);
					setCertainLevel(2, kickerCard);
					isTwoPairs = true;

				} else if (thirdPair > secondPair) {
					if (secondPair > kicker) {
						kicker = secondPair;
						kickerCard = secondCard;

					}

					secondPair = thirdPair;
					secondCard = thirdCard;
					setCertainLevel(0, firstCard);
					setCertainLevel(1, secondCard);
					setCertainLevel(2, kickerCard);
					isTwoPairs = true;

				} else {
					if (thirdPair > kicker) {
						kicker = thirdPair;
						kickerCard = thirdCard;

					}

					setCertainLevel(0, firstCard);
					setCertainLevel(1, secondCard);
					setCertainLevel(2, kickerCard);
					isTwoPairs = true;
				}

			}

		}// Keine zwei Paare gefunden

	}

	@Override
	public String toString() {

		String result;

		result = "Two pairs: " + getCertainLevel(0).getValueString() + "s and "
				+ getCertainLevel(1).getValueString() + "s";
		return result;

	}

	@Override
	public int getCOMBORDER() {
		// TODO Auto-generated method stub
		return COMBORDER;
	}

	public boolean isTwoPairs() {
		return isTwoPairs;
	}

}
