package com.FedorApps.pokerassistant.cardCombos;

import java.util.ArrayList;

import com.FedorApps.pokerassistant.Card;

public class Flush extends CardCombination {

	private final int COMBORDER = 6;

	private String color;
	private boolean isFlush;

	public Flush(ArrayList<Card> list) {

		if (list.size() >= 5) {

			checkFlush(1, list);
			checkFlush(2, list);
			checkFlush(3, list);
			checkFlush(4, list);

		}

	}

	private void checkFlush(int c, ArrayList<Card> list) {

		int count = 0;
		Card highest = null;

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getColor() == c) {
				count++;
				if (highest == null) {
					highest = list.get(i);
				} else if (list.get(i).getValue() > highest.getValue()) {
					highest = list.get(i);
				}
			}
		}

		if (count >= 5) {

			isFlush = true;

			switch (c) {
			case 1:
				color = "Spade";
				break;
			case 2:
				color = "Heart";
				break;
			case 3:
				color = "Diamond";
				break;
			default:
				color = "Club";
			}

			setCertainLevel(0, highest);

		}

	}

	@Override
	public String toString() {

		String result;
		result = color + " flush";
		return result;

	}

	@Override
	public int getCOMBORDER() {
		// TODO Auto-generated method stub
		return COMBORDER;
	}

	public boolean isFlush() {
		return isFlush;
	}

}
