package com.FedorApps.pokerassistant.cardCombos;

import java.util.ArrayList;

import com.FedorApps.pokerassistant.Card;

public class StraightFlush extends CardCombination {

	private final int COMBORDER = 9;

	private boolean StraightFlush = false;
	private boolean spadeAceChecked = false;
	private boolean heartAceChecked = false;
	private boolean diamondAceChecked = false;
	private boolean clubAceChecked = false;
	Card high = null;
	Card start = null;

	public StraightFlush(ArrayList<Card> list) {

		while (list.size() >= 5) {

			// Man sucht sich die kleinste Karte oder das Ass heraus

			Card current = list.get(0);

			for (int i = 0; i < list.size(); i++) {

				if (list.get(i).getValue() == 14) {

					if (list.get(i).getColor() == 1 && !spadeAceChecked) {

						current = list.get(i);
						break;
					}

					if (list.get(i).getColor() == 2 && !heartAceChecked) {

						current = list.get(i);
						break;
					}

					if (list.get(i).getColor() == 3 && !diamondAceChecked) {

						current = list.get(i);
						break;
					}

					if (list.get(i).getColor() == 4 && !clubAceChecked) {

						current = list.get(i);
						break;
					}
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
					if (list.get(i).getValue() == (value + count)
							&& list.get(i).getColor() == current.getColor()) {
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
				StraightFlush = true;
				start = current;
			}

			if (current.getValue() != 14) {
				list.remove(current);
			} else {
				if (current.getColor() == 1) {
					spadeAceChecked = true;
				}
				if (current.getColor() == 2) {
					heartAceChecked = true;
				}
				if (current.getColor() == 3) {
					diamondAceChecked = true;
				}
				if (current.getColor() == 4) {
					clubAceChecked = true;
				}
			}

		}

		if (StraightFlush) {
			setCertainLevel(0, high);
		}
	}

	@Override
	public String toString() {

		String result;
		String name;

		if (high.getValue() == 14) {
			name = "Royal flush.";
			return name;
		} else {
			name = "Straight flush: ";
		}

		result = name + start.getValueString() + " to " + high.getValueString();
		return result;

	}

	@Override
	public int getCOMBORDER() {
		// TODO Auto-generated method stub
		return COMBORDER;
	}

	public boolean isStraightFlush() {
		return StraightFlush;
	}

}
