package com.FedorApps.pokerassistant.cardCombos;

import android.app.Activity;

import com.FedorApps.pokerassistant.Card;
import com.FedorApps.pokerassistant.Flop;
import com.FedorApps.pokerassistant.HandCards;
import com.FedorApps.pokerassistant.River;
import com.FedorApps.pokerassistant.Turn;

public class OddCalculator {

	private CardDeck deck;

	private int odd;
	private int endSum;
	private long calcTime;

	// Für Testzwecke: Ein Ding, dass die Handkarten des Gegners kriegt

	public OddCalculator(HandCards hand, HandCards opponent, Flop flop,
			Turn turn, River river, Activity activity) {
		this.deck = new CardDeck();

		// Deck anpassen
		long startTime = System.currentTimeMillis();

		if (opponent.getCard1() == null && river.getCard() == null
				&& turn.getCard() == null) {

			// Vorbereiten des Decks
			// Vorbereiten des Decks: Entfernen aller bekannter Karten
			removeCard(hand.getCard1().getValue(), hand.getCard1().getColor());
			removeCard(hand.getCard2().getValue(), hand.getCard2().getColor());

			removeCard(flop.getCard1().getValue(), flop.getCard1().getColor());
			removeCard(flop.getCard2().getValue(), flop.getCard2().getColor());
			removeCard(flop.getCard3().getValue(), flop.getCard3().getColor());

			int wins = 0;
			int sum = 0;

			for (int i1 = 0; i1 < deck.getDeck().size(); i1++) {

				Card riverCard = deck.getDeck().get(i1);

				for (int i2 = 0; i2 < deck.getDeck().size(); i2++) {

					if (i2 != i1) {

						Card turnCard = deck.getDeck().get(i2);

						for (int i3 = 0; i3 < deck.getDeck().size(); i3++) {

							if (i3 != i1 && i3 != i2) {

								Card firstOppoCard = deck.getDeck().get(i3);

								for (int i4 = 0; i4 < deck.getDeck().size(); i4++) {

									if (i4 != i1 && i4 != i2 && i4 != i3) {

										Card secondOppoCard = deck.getDeck()
												.get(i4);
										river.dealCard(riverCard);
										opponent.setCard1(firstOppoCard);
										opponent.setCard2(secondOppoCard);

										turn.dealCard(turnCard);

										boolean win = checkSituation(hand,
												opponent, flop, turn, river);

										if (win) {
											wins++;
										}

										sum++;
									}
								}

							}

						}

					}
				}

			}

			odd = (wins * 100) / sum;
			endSum = sum;

			opponent.setCard1(null);
			opponent.setCard2(null);

			turn.dealCard(null);

			river.dealCard(null);

		}

		// Nicht bekannt: River und Gegnerkarten
		if (opponent.getCard1() == null && river.getCard() == null
				&& turn != null) {

			// Vorbereiten des Decks
			// Vorbereiten des Decks: Entfernen aller bekannter Karten
			removeCard(hand.getCard1().getValue(), hand.getCard1().getColor());
			removeCard(hand.getCard2().getValue(), hand.getCard2().getColor());

			removeCard(flop.getCard1().getValue(), flop.getCard1().getColor());
			removeCard(flop.getCard2().getValue(), flop.getCard2().getColor());
			removeCard(flop.getCard3().getValue(), flop.getCard3().getColor());

			removeCard(turn.getCard().getValue(), turn.getCard().getColor());

			int wins = 0;
			int sum = 0;

			for (int i1 = 0; i1 < deck.getDeck().size(); i1++) {

				Card riverCard = deck.getDeck().get(i1);

				for (int i2 = 0; i2 < deck.getDeck().size(); i2++) {

					if (i2 != i1) {

						Card firstOppoCard = deck.getDeck().get(i2);

						for (int i3 = 0; i3 < deck.getDeck().size(); i3++) {

							if (i3 != i1 && i3 != i2) {

								Card secondOppoCard = deck.getDeck().get(i3);

								river.dealCard(riverCard);
								opponent.setCard1(firstOppoCard);
								opponent.setCard2(secondOppoCard);

								boolean win = checkSituation(hand, opponent,
										flop, turn, river);

								if (win) {
									wins++;
								}

								sum++;

							}

						}

					}
				}

			}

			odd = (wins * 100) / sum;
			endSum = sum;

			opponent.setCard1(null);
			opponent.setCard2(null);

			river.dealCard(null);

		}

		// Der jetzige Fall: nur der River ist nicht bekannt (eigentlich als
		// letztes überprüft.)

		if (river.getCard() == null && opponent.getCard1() != null) {

			// Vorbereiten des Decks: Entfernen aller bekannter Karten
			removeCard(hand.getCard1().getValue(), hand.getCard1().getColor());
			removeCard(hand.getCard2().getValue(), hand.getCard2().getColor());

			removeCard(opponent.getCard1().getValue(), opponent.getCard1()
					.getColor());
			removeCard(opponent.getCard2().getValue(), opponent.getCard2()
					.getColor());

			removeCard(flop.getCard1().getValue(), flop.getCard1().getColor());
			removeCard(flop.getCard2().getValue(), flop.getCard2().getColor());
			removeCard(flop.getCard3().getValue(), flop.getCard3().getColor());

			removeCard(turn.getCard().getValue(), turn.getCard().getColor());

			int wins = 0;
			int sum = 0;

			for (int i = 0; i < deck.getDeck().size(); i++) {

				Card test = deck.getDeck().get(i);
				river.dealCard(test);

				boolean win = checkSituation(hand, opponent, flop, turn, river);

				if (win) {
					wins++;
				}

				sum++;

			}

			odd = (wins * 100) / sum;
			endSum = sum;

			river = null;

		}

		long endTime = System.currentTimeMillis();
		calcTime = endTime - startTime;

	}

	private void removeCard(int value, int color) {

		for (int i = 0; i < deck.getDeck().size(); i++) {

			if (deck.getDeck().get(i).getColor() == color
					&& deck.getDeck().get(i).getValue() == value) {
				deck.getDeck().remove(deck.getDeck().get(i));
			}

		}

	}

	private boolean checkSituation(HandCards hand, HandCards opponent,
			Flop flop, Turn turn, River river) {

		CardCombination myCombination;
		CardCombination otherCombination;

		CombinationCalculator calc1 = new CombinationCalculator(hand, flop,
				turn, river);
		CombinationCalculator calc2 = new CombinationCalculator(opponent, flop,
				turn, river);

		myCombination = calc1.getCombi();
		otherCombination = calc2.getCombi();

		boolean win = CombinationComparator.compareCombinations(myCombination,
				otherCombination);

		return win;

	}

	public int getOdd() {
		return odd;
	}

	public int getEndSum() {
		return endSum;
	}

	public long getCalcTime() {
		return calcTime / 1000;
	}

}
