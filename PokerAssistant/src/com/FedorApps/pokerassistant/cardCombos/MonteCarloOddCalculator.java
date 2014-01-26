package com.FedorApps.pokerassistant.cardCombos;

import java.util.ArrayList;
import java.util.Random;

import com.FedorApps.pokerassistant.Card;
import com.FedorApps.pokerassistant.Flop;
import com.FedorApps.pokerassistant.HandCards;
import com.FedorApps.pokerassistant.River;
import com.FedorApps.pokerassistant.Turn;

public class MonteCarloOddCalculator {

	private CardDeck deck;

	private int odd;

	private ArrayList<HandCards> opponents = new ArrayList<HandCards>();
	Random random = new Random();
	int givenMilliSec;

	private HandCards hand;
	private Flop flop;
	private Turn turn;
	private River river;
	private int count;

	private int wins;

	public MonteCarloOddCalculator(HandCards hand, int oppoNum, Flop flop,
			Turn turn, River river, int givenMilliSec) {

		this.givenMilliSec = givenMilliSec;
		this.deck = new CardDeck();

		for (int i = 0; i < oppoNum; i++) {

			HandCards oppo = new HandCards();
			opponents.add(oppo);

		}

		this.hand = hand;
		this.flop = flop;
		this.turn = turn;
		this.river = river;
		wins = 0;
		count = 0;

		calculateOdds(givenMilliSec);

	}

	public void calculateOdds(int givenMilliSec) {

		this.givenMilliSec = givenMilliSec;

		if (flop.getCard1() == null) {

			// Flop, river und turn unbekannt

			// Vorbereiten des Decks
			removeCard(hand.getCard1().getValue(), hand.getCard1().getColor());
			removeCard(hand.getCard2().getValue(), hand.getCard2().getColor());

			long endTime = System.currentTimeMillis() + givenMilliSec;

			while (System.currentTimeMillis() < endTime) {

				// Flop erzeugen

				// Erste Flopkarte
				int cardnum = random.nextInt(deck.getDeck().size());
				Card flopCard1 = deck.getDeck().get(cardnum);
				deck.getDeck().remove(flopCard1);
				flop.setCard1(flopCard1);

				// Zweite Flopkarte
				cardnum = random.nextInt(deck.getDeck().size());
				Card flopCard2 = deck.getDeck().get(cardnum);
				deck.getDeck().remove(flopCard2);
				flop.setCard2(flopCard2);

				// Dritte Flopkarte
				cardnum = random.nextInt(deck.getDeck().size());
				Card flopCard3 = deck.getDeck().get(cardnum);
				deck.getDeck().remove(flopCard3);
				flop.setCard3(flopCard3);

				// Turn erzeugen
				cardnum = random.nextInt(deck.getDeck().size());
				Card turnCard = deck.getDeck().get(cardnum);
				deck.getDeck().remove(turnCard);
				turn.dealCard(turnCard);

				// River erzeugen.
				cardnum = random.nextInt(deck.getDeck().size());
				Card riverCard = deck.getDeck().get(cardnum);
				deck.getDeck().remove(riverCard);
				river.dealCard(riverCard);

				// Die Gegner erzeugen

				for (int i = 0; i < opponents.size(); i++) {
					createOpponent(i);
				}

				boolean win = true;

				for (int i = 0; i < opponents.size(); i++) {

					if (!checkSituation(hand, opponents.get(i), flop, turn,
							river)) {
						win = false;
					}

				}

				if (win) {
					wins++;
				}

				count++;

				// Flop wieder ins Deck
				deck.getDeck().add(flopCard1);
				deck.getDeck().add(flopCard2);
				deck.getDeck().add(flopCard3);

				// Turn wieder ins Deck

				deck.getDeck().add(turnCard);

				// River wieder ins Deck
				deck.getDeck().add(riverCard);

				// OppoKarten wieder ins Deck;
				returnOppoCards();

			}

			// Flop löschen
			flop.setCard1(null);
			flop.setCard2(null);
			flop.setCard3(null);

			// Turn löschen
			turn.dealCard(null);

			// River löschen
			river.dealCard(null);

			// OppoKarten löschen
			deleteOppoCards();

			if (wins == 0) {
				odd = 0;
			} else {
				odd = (wins * 100) / count;
			}

		}

		else if (turn.getCard() == null) {

			// Turn und River unbekannt

			// Vorbereiten des Decks
			removeCard(hand.getCard1().getValue(), hand.getCard1().getColor());
			removeCard(hand.getCard2().getValue(), hand.getCard2().getColor());

			removeCard(flop.getCard1().getValue(), flop.getCard1().getColor());
			removeCard(flop.getCard2().getValue(), flop.getCard2().getColor());
			removeCard(flop.getCard3().getValue(), flop.getCard3().getColor());

			long endTime = System.currentTimeMillis() + givenMilliSec;

			while (System.currentTimeMillis() < endTime) {

				// Turn erzeugen
				int cardnum = random.nextInt(deck.getDeck().size());
				Card turnCard = deck.getDeck().get(cardnum);
				deck.getDeck().remove(turnCard);
				turn.dealCard(turnCard);

				// River erzeugen.
				cardnum = random.nextInt(deck.getDeck().size());
				Card riverCard = deck.getDeck().get(cardnum);
				deck.getDeck().remove(riverCard);
				river.dealCard(riverCard);

				// Die Gegner erzeugen

				for (int i = 0; i < opponents.size(); i++) {
					createOpponent(i);
				}

				boolean win = true;

				for (int i = 0; i < opponents.size(); i++) {

					if (!checkSituation(hand, opponents.get(i), flop, turn,
							river)) {
						win = false;
					}

				}

				if (win) {
					wins++;
				}

				count++;

				// Turn wieder ins Deck

				deck.getDeck().add(turnCard);

				// River wieder ins Deck
				deck.getDeck().add(riverCard);

				// OppoKarten wieder ins Deck;
				returnOppoCards();

			}

			turn.dealCard(null);

			// River löschen
			river.dealCard(null);

			// OppoKarten löschen
			deleteOppoCards();

			if (wins == 0) {
				odd = 0;
			} else {
				odd = (wins * 100) / count;
			}
		}

		else if (river.getCard() == null) {
			// Situation: Nur River unbekannt
			// Vorbereiten des Decks
			removeCard(hand.getCard1().getValue(), hand.getCard1().getColor());
			removeCard(hand.getCard2().getValue(), hand.getCard2().getColor());

			removeCard(flop.getCard1().getValue(), flop.getCard1().getColor());
			removeCard(flop.getCard2().getValue(), flop.getCard2().getColor());
			removeCard(flop.getCard3().getValue(), flop.getCard3().getColor());

			removeCard(turn.getCard().getValue(), turn.getCard().getColor());

			long endTime = System.currentTimeMillis() + givenMilliSec;

			while (System.currentTimeMillis() < endTime) {

				// River erzeugen.
				int cardnum = random.nextInt(deck.getDeck().size());
				Card riverCard = deck.getDeck().get(cardnum);
				deck.getDeck().remove(riverCard);
				river.dealCard(riverCard);

				// Die Gegner erzeugen

				for (int i = 0; i < opponents.size(); i++) {
					createOpponent(i);
				}

				boolean win = true;

				for (int i = 0; i < opponents.size(); i++) {

					if (!checkSituation(hand, opponents.get(i), flop, turn,
							river)) {
						win = false;
					}

				}

				if (win) {
					wins++;
				}

				count++;

				// River wieder ins Deck
				deck.getDeck().add(riverCard);

				// OppoKarten wieder ins Deck;
				returnOppoCards();

			}

			// River löschen
			river.dealCard(null);

			// OppoKarten löschen
			deleteOppoCards();

			if (wins == 0) {
				odd = 0;
			} else {
				odd = (wins * 100) / count;
			}

		} else {
			// Alles (bis auf die Gegnerkarten ist bekannt)

			// Vorbereiten des Decks
			removeCard(hand.getCard1().getValue(), hand.getCard1().getColor());
			removeCard(hand.getCard2().getValue(), hand.getCard2().getColor());

			removeCard(flop.getCard1().getValue(), flop.getCard1().getColor());
			removeCard(flop.getCard2().getValue(), flop.getCard2().getColor());
			removeCard(flop.getCard3().getValue(), flop.getCard3().getColor());

			removeCard(turn.getCard().getValue(), turn.getCard().getColor());

			removeCard(river.getCard().getValue(), river.getCard().getColor());

			long endTime = System.currentTimeMillis() + givenMilliSec;

			while (System.currentTimeMillis() < endTime) {

				// Die Gegner erzeugen

				for (int i = 0; i < opponents.size(); i++) {
					createOpponent(i);
				}

				boolean win = true;

				for (int i = 0; i < opponents.size(); i++) {

					if (!checkSituation(hand, opponents.get(i), flop, turn,
							river)) {
						win = false;
					}

				}

				if (win) {
					wins++;
				}

				count++;

				// OppoKarten wieder ins Deck;
				returnOppoCards();

			}

			// OppoKarten löschen
			deleteOppoCards();

			if (wins == 0) {
				odd = 0;
			} else {
				odd = (wins * 100) / count;
			}
		}

	}

	private void removeCard(int value, int color) {

		for (int i = 0; i < deck.getDeck().size(); i++) {

			if (deck.getDeck().get(i).getColor() == color
					&& deck.getDeck().get(i).getValue() == value) {
				deck.getDeck().remove(deck.getDeck().get(i));
			}

		}

	}

	// Returns true by win or deuce; false otherwise
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

	private void createOpponent(int i) {

		// Erste Gegnerkarte setzen
		int cardnum = random.nextInt(deck.getDeck().size());
		Card oppoCard1 = deck.getDeck().get(cardnum);
		deck.getDeck().remove(oppoCard1);

		// Zweite Gegnerkarte setzen
		cardnum = random.nextInt(deck.getDeck().size());
		Card oppoCard2 = deck.getDeck().get(cardnum);
		deck.getDeck().remove(oppoCard2);

		opponents.get(i).setCard1(oppoCard1);
		opponents.get(i).setCard2(oppoCard2);

	}

	private void returnOppoCards() {

		for (int i = 0; i < opponents.size(); i++) {

			deck.getDeck().add(opponents.get(i).getCard1());
			deck.getDeck().add(opponents.get(i).getCard2());

		}
	}

	private void deleteOppoCards() {

		for (int i = 0; i < opponents.size(); i++) {

			HandCards oppo = opponents.get(i);
			oppo.setCard1(null);
			oppo.setCard2(null);

		}

	}

	public int getOdd() {
		return odd;
	}

}
