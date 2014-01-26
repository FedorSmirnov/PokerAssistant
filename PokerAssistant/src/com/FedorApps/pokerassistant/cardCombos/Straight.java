package com.FedorApps.pokerassistant.cardCombos;

import java.util.ArrayList;

import com.FedorApps.pokerassistant.Card;

public class Straight extends CardCombination {

	private final int COMBORDER = 5;

	private boolean Straight = false;
	private boolean aceChecked = false;

	Card high = null;
	Card start = null;

	public Straight(ArrayList<Card> list) {

		while (list.size() >= 5) {

			// Man sucht sich die kleinste Karte oder das Ass heraus

			Card current = list.get(0);

			for (int i = 0; i < list.size(); i++) {

				if (list.get(i).getValue() == 14 && !aceChecked) {
					current = list.get(i);
					break;
				} else if (list.get(i).getValue() < current.getValue()) {
					current = list.get(i);
				}

			}

			// Hier ist im current der mögliche Strassenanfang gespeichert

			boolean goOn = true;
			int count = 1;

			int value;

			if (current.getValue() == 14) {
				value = 1;
			} else {
				value = current.getValue();
			}

			while (count < 5 && goOn) {
				goOn = false;

				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getValue() == (value + count)) {
						count++;

						if (count == 5) {
							high = list.get(i);
						}

						goOn = true;
						break;
					}
				}
			}

			if (count == 5) {
				Straight = true;
				start = current;
			}

			if (current.getValue() != 14) {
				list.remove(current);
			}else{
				aceChecked = true;
			}

		}

		if (Straight) {
			setCertainLevel(0, high);
		}

	}

	@Override
	public String toString() {

		String result;
		result = "Straight: " + start.getValueString() + " to "
				+ high.getValueString();
		return result;

	}

	@Override
	public int getCOMBORDER() {
		// TODO Auto-generated method stub
		return COMBORDER;
	}

	public boolean isStraight() {
		return Straight;
	}

}
