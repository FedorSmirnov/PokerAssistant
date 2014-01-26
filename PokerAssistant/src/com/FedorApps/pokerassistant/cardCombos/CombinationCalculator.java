package com.FedorApps.pokerassistant.cardCombos;

import java.util.ArrayList;

import com.FedorApps.pokerassistant.Card;
import com.FedorApps.pokerassistant.Flop;
import com.FedorApps.pokerassistant.HandCards;
import com.FedorApps.pokerassistant.River;
import com.FedorApps.pokerassistant.Turn;

public class CombinationCalculator {

	private CardCombination combi;

	private ArrayList<Card> evaluatedCards = new ArrayList<Card>();

	public CombinationCalculator(HandCards hand, Flop flop, Turn turn,
			River river) {

		// Alle Karten, die übergeben wurden in die Liste aufnehmen
		if (hand.getCard1() != null) {
			evaluatedCards.add(hand.getCard1());
			evaluatedCards.add(hand.getCard2());

			if (flop.getCard1() != null) {
				evaluatedCards.add(flop.getCard1());
				evaluatedCards.add(flop.getCard2());
				evaluatedCards.add(flop.getCard3());

				if (turn.getCard() != null) {
					evaluatedCards.add(turn.getCard());

					if (river.getCard() != null) {
						evaluatedCards.add(river.getCard());
					}
				}
			}
		}

		if (evaluatedCards.size() > 0) {
			calculateTheCombination();
		}

	}

	// Methode, um die höchste Kombo zu finden, die sich mit den übergebenen
	// Karten bilden lässt.
	private void calculateTheCombination() {

		// 1) Hilfsliste anlegen
		ArrayList<Card> helpList = makeHelpList();

		// 2) Checken, ob es Paare gibt
		boolean foundPair = pairsExist(helpList);
		boolean foundDrilling = false;
		boolean foundVierling = false;
		boolean found2Pair = false;
		boolean foundFullHouse = false;
		boolean foundStraight = false;
		boolean foundFlush = false;
		boolean foundStraightFlush = false;

		// Wenn Paar gefunden, muss man die combi setzen und dann auf 2 Paare,
		// Drilling usw. überprüfen
		if (foundPair) {

			combi = new Pair(makeHelpList());

			// Überprüfung Drilling
			Drilling drilling = new Drilling(makeHelpList());

			foundDrilling = drilling.isDrilling();

			if (foundDrilling) {
				combi = drilling;

				// Überprüfung Vierling
				Vierling vierling = new Vierling(makeHelpList());
				foundVierling = vierling.isVierling();

				if (foundVierling) {
					combi = vierling;
				}
			}

			// Überprüfung zwei Paare (und eventuell FullHouse)
			if (!foundVierling) {

				TwoPairs twopairs = new TwoPairs(makeHelpList());

				found2Pair = twopairs.isTwoPairs();

				if (found2Pair) {
					if (!foundDrilling) {
						combi = twopairs;
					}

					// Überprüfung auf FullHouse

					if (foundDrilling) {
						foundFullHouse = true;
						combi = new FullHouse(makeHelpList());
					}
				}

			}

		}

		// Hier kommt das Überprüfen auf alle anderen Kombis

		// Überprüfen auf eine Strasse

		Straight straight = new Straight(makeHelpList());

		foundStraight = straight.isStraight();

		if (foundStraight && !foundFullHouse && !foundVierling) {
			combi = straight;
		}

		// Überprüfen auf den Flush
		Flush flush = new Flush(makeHelpList());
		foundFlush = flush.isFlush();

		if (foundFlush && !foundVierling && !foundFullHouse) {
			combi = flush;
		}

		// Überprüfen auf StraightFlush

		if (foundFlush && foundStraight) {

			StraightFlush straightFlush = new StraightFlush(makeHelpList());
			foundStraightFlush = straightFlush.isStraightFlush();

			if (foundStraightFlush) {
				combi = straightFlush;
			}

		}

		// Wenn nichts gefunden, hat man ne HighCard
		if (!foundPair && !foundStraight && !foundFlush && !foundStraightFlush) {

			combi = new HighCard(makeHelpList());
		}

	}

	// Methode sucht ein Paar in der Kombination
	private boolean pairsExist(ArrayList<Card> helpList) {
		Card current;
		int value;

		while (helpList.size() > 1) {
			current = helpList.get(0);
			value = current.getValue();
			for (int i = 1; i < helpList.size(); i++) {

				if (helpList.get(i).getValue() == value) {

					// Hier muss das Paar "erschaffen" werden

					return true;
				}

			}
			helpList.remove(current);
		}
		return false;

	}

	private ArrayList<Card> makeHelpList() {
		ArrayList<Card> helplist = new ArrayList<Card>();
		for (int i = 0; i < evaluatedCards.size(); i++) {
			helplist.add(evaluatedCards.get(i));
		}
		return helplist;
	}

	public String toString() {
		return combi.toString();
	}

	public CardCombination getCombi() {
		return combi;
	}
	
	

}
