package com.FedorApps.pokerassistant.pokerSession;

import android.widget.TextView;

public class Player {

	private String name;
	protected int num;

	private int money;
	private int bet = 0;
	private boolean isDealer;
	protected boolean selected = false;

	// Ist gerade dran
	private boolean isOnTurn = false;

	// Gefoldet oder noch dabei
	private boolean isInRound = true;
	private boolean staysInGame = true;

	// Variablen für den Sidepot
	private boolean wentAllIn = false;
	private int potShare = 0;

	// Variablen für die Statistik

	// Spielstatistiken
	// Insgesamt bei Händen dabei
	private int totalHands = 0;

	// Hände, bei denen man was gesetzt hat
	private int handsPlayed = 0;
	private int handsPlayedPer = 0;
	// Hände, bei denen man was gewonnen hat
	private int handsWon = 0;
	private int handsWonPer = 0;
	private int handsWonOverallPer = 0;

	// Chipstatistiken
	private int balance = 0;
	private int startAmount;
	private int maxAmount;
	private int minAmount;
	private int average;
	// Zum Rechnen des Durchschnitts
	private int sum;
	private boolean playingHand = false;

	// Die zugeordneten Views

	protected TextView betBox;
	protected TextView moneyBox;
	protected TextView dealerBox;

	public Player(int buyIn) {
		this.maxAmount = buyIn;
		this.minAmount = buyIn;
		this.average = buyIn;
		this.sum = buyIn;
		this.startAmount = buyIn;
	}

	public String getName() {
		return name;
	}

	public int getMoney() {
		return money;
	}

	public int getBet() {
		return bet;
	}

	public boolean isDealer() {
		return isDealer;
	}

	public void setName(String name) {
		this.name = name;

	}

	public void setMoney(int money) {

		this.money = money;

		moneyBox.setText(String.valueOf(money));

	}

	public void setBet(int bet) {

		this.bet = bet;

		if (bet > 0) {
			betBox.setText(String.valueOf(bet));
		} else {
			betBox.setText("");
		}

	}

	public void setDealer(boolean isDealer) {

		this.isDealer = isDealer;

		if (isDealer) {

			dealerBox.setText("D");
			dealerBox.setEnabled(true);

		} else {

			dealerBox.setText("");
			dealerBox.setEnabled(false);
		}

	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public void bet(int amount) {

		setBet(bet + amount);
		setMoney(getMoney() - amount);

	}

	public boolean isOnTurn() {
		return isOnTurn;
	}

	public void setOnTurn(boolean isOnTurn) {
		this.isOnTurn = isOnTurn;
	}

	public boolean isInRound() {
		return isInRound;
	}

	public void setInRound(boolean isInRound, boolean staysInGame) {
		this.isInRound = isInRound;
		this.staysInGame = staysInGame;
	}

	public boolean isStaysInGame() {
		return staysInGame;
	}

	public void setFocused() {

	}

	public void unfocus() {

	}

	public void setSelected() {

	}

	public void unselect() {

	}

	public boolean isSelected() {
		return selected;
	}

	public boolean isWentAllIn() {
		return wentAllIn;
	}

	public void setWentAllIn(boolean wentAllIn) {
		this.wentAllIn = wentAllIn;
	}

	public int getPotShare() {
		return potShare;
	}

	public void setPotShare(int potShare) {
		this.potShare = potShare;
	}

	public int getTotalHands() {
		return totalHands;
	}

	public void setTotalHands(int totalHands) {
		this.totalHands = totalHands;
	}

	public int getHandsPlayed() {
		return handsPlayed;
	}

	public void setHandsPlayed(int handsPlayed) {
		this.handsPlayed = handsPlayed;
	}

	public int getHandsWon() {
		return handsWon;
	}

	public void setHandsWon(int handsWon) {
		this.handsWon = handsWon;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(int maxAmount) {
		this.maxAmount = maxAmount;
	}

	public int getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(int minAmount) {
		this.minAmount = minAmount;
	}

	public int getAverage() {
		return average;
	}

	public void setAverage(int average) {
		this.average = average;
	}

	public boolean isPlayingHand() {
		return playingHand;
	}

	public void setPlayingHand(boolean playingHand) {
		this.playingHand = playingHand;
	}

	public void updateStats() {

		if (totalHands != 0) {
			handsPlayedPer = (handsPlayed * 100) / totalHands;
		}
		if (handsPlayed != 0) {
			handsWonPer = (handsWon * 100) / handsPlayed;
		}

		if (totalHands != 0) {
			handsWonOverallPer = (handsWon * 100) / totalHands;
		}

		balance = money - startAmount;

		if (maxAmount < money) {
			maxAmount = money;
		}

		if (minAmount > money) {
			minAmount = money;
		}

		sum = sum + money;

		average = sum / (totalHands + 1);

	}

	public int getHandsPlayedPer() {
		return handsPlayedPer;
	}

	public int getHandsWonPer() {
		return handsWonPer;
	}

	public int getHandsWonOverallPer() {
		return handsWonOverallPer;
	}

	public int getStartAmount() {
		return startAmount;
	}

	public void setStartAmount(int startAmount) {
		this.startAmount = startAmount;
	}

}
