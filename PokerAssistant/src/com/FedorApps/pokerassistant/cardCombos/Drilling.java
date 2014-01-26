package com.FedorApps.pokerassistant.cardCombos;

import java.util.ArrayList;

import com.FedorApps.pokerassistant.Card;

public class Drilling extends CardCombination {

	private final int COMBORDER = 4;
	private boolean drilling = false;

	public Drilling(ArrayList<Card> list) {

		Card current = list.get(0);
		int count = 0;
		int pos = 0;
		while (list.size() > 1 && count < 5) {

			if (pos < list.size() && pos <= 5) {
				current = list.get(pos);
			} else {
				break;
			}

			if (!drilling) {
				// Drilling noch nicht gefunden
				Card firstCard = null;
				for (int i = pos + 1; i < list.size(); i++) {

					if (current.getValue() == list.get(i).getValue()) {
						if (firstCard != null) {
							drilling = true;
							list.remove(list.get(i));
							list.remove(firstCard);
							list.remove(current);
							setCertainLevel(0, current);
							count = 3;
							pos = 0;

						} else {
							firstCard = list.get(i);
						}
					}

				}

				if (!drilling) {
					pos++;
				}

			} else {
				// Drilling gefunden

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

		result = "Three of a kind: " + getCertainLevel(0).getValueString()
				+ "s";
		return result;

	}

	@Override
	public int getCOMBORDER() {
		// TODO Auto-generated method stub
		return COMBORDER;
	}

	public boolean isDrilling() {
		return drilling;
	}

}
