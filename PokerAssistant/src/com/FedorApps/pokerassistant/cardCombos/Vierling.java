package com.FedorApps.pokerassistant.cardCombos;

import java.util.ArrayList;

import com.FedorApps.pokerassistant.Card;

public class Vierling extends CardCombination {

	private final int COMBORDER = 8;
	private boolean isVierling = false;

	public Vierling(ArrayList<Card> list) {

		Card current = list.get(0);
		int count = 0;
		int pos = 0;
		while (list.size() > 1 && count < 5) {

			if (pos < list.size() && pos <= 4) {
				current = list.get(pos);
			} else {
				break;
			}

			if (!isVierling) {
				// Vierling noch nicht gefunden
				Card firstCard = null;
				Card secondCard = null;
				for (int i = pos + 1; i < list.size(); i++) {

					if (current.getValue() == list.get(i).getValue()) {
						if (firstCard != null && secondCard != null) {
							isVierling = true;
							list.remove(list.get(i));
							list.remove(firstCard);
							list.remove(secondCard);
							list.remove(current);
							setCertainLevel(0, current);
							count = 4;
							pos = 0;

						} else if (firstCard != null) {

							secondCard = list.get(i);

						} else {
							firstCard = list.get(i);
						}
					}

				}

				if (!isVierling) {
					pos++;
				}

			} else {
				// Vierling gefunden

				for (int i = 0; i < list.size(); i++) {

					if (current.getValue() < list.get(i).getValue()) {
						current = list.get(i);
					}

				}
				list.remove(current);
				setCertainLevel(count - 1, current);
				count++;

			}

		}

	}

	@Override
	public String toString() {
		
		String result;

		result = "Four of a kind: " + getCertainLevel(0).getValueString()
				+ "s";
		return result;
		
	}

	@Override
	public int getCOMBORDER() {
		// TODO Auto-generated method stub
		return COMBORDER;
	}

	public boolean isVierling() {
		return isVierling;
	}

}
