package com.FedorApps.pokerassistant.cardCombos;

public class CombinationComparator {

	// Returns true by win or deuce; false otherwise
	public static boolean compareCombinations(CardCombination yourComb,
			CardCombination otherComb) {

		if (yourComb.getCOMBORDER() > otherComb.getCOMBORDER()) {
			return true;
		} else if (yourComb.getCOMBORDER() < otherComb.getCOMBORDER()) {
			return false;
		} else {

			if (yourComb.getCertainLevel(0).getValue() > otherComb
					.getCertainLevel(0).getValue()) {
				return true;
			} else if (yourComb.getCertainLevel(0).getValue() < otherComb
					.getCertainLevel(0).getValue()) {
				return false;
			} else {

				if (yourComb.getCertainLevel(1) == null) {
					return true;
				}

				if (yourComb.getCertainLevel(1).getValue() > otherComb
						.getCertainLevel(1).getValue()) {
					return true;
				} else if (yourComb.getCertainLevel(1).getValue() < otherComb
						.getCertainLevel(1).getValue()) {
					return false;
				} else {

					if (yourComb.getCertainLevel(2) == null) {
						return true;
					}

					if (yourComb.getCertainLevel(2).getValue() > otherComb
							.getCertainLevel(2).getValue()) {
						return true;
					} else if (yourComb.getCertainLevel(2).getValue() < otherComb
							.getCertainLevel(2).getValue()) {
						return false;
					} else {

						if (yourComb.getCertainLevel(3) == null) {
							return true;
						}

						if (yourComb.getCertainLevel(3).getValue() > otherComb
								.getCertainLevel(3).getValue()) {
							return true;
						} else if (yourComb.getCertainLevel(3).getValue() < otherComb
								.getCertainLevel(3).getValue()) {
							return false;
						} else {

							if (yourComb.getCertainLevel(4) == null) {
								return true;
							}

							if (yourComb.getCertainLevel(4).getValue() > otherComb
									.getCertainLevel(4).getValue()) {
								return true;
							} else if (yourComb.getCertainLevel(4).getValue() < otherComb
									.getCertainLevel(4).getValue()) {
								return false;
							} else {
								return true;
							}

						}

					}

				}

			}
		}

	}
}
