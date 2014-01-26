package com.FedorApps.pokerassistant;

import android.app.Activity;

public class Card {

	// Zuordnung der Farben: 1 => spade; 2 => heart; 3 => diamond; 4 => club
	private int color;
	private int value;
	private Activity activity;

	public Card(int color, int value, Activity activity) {

		this.color = color;
		this.value = value;
		this.activity = activity;
	}

	@Override
	public String toString() {

		String result;

		// Ausgeben des Resultats
		result = getColorString() + " " + getValueString();
		return result;
	}

	public String getColorString() {
		String strColor;
		// Setzen der Farbe
		if (color == 1) {
			strColor = activity.getApplicationContext().getString(
					R.string.spade);
		} else if (color == 2) {
			strColor = activity.getString(R.string.heart);
		} else if (color == 3) {
			strColor = activity.getString(R.string.diamond);
		} else {
			strColor = activity.getString(R.string.club);
		}

		return strColor;

	}

	public String getValueString() {

		String strValue;

		// Setzen des Werts
		if (value == 14) {
			strValue = activity.getString(R.string.Ace);
		} else if (value == 13) {
			strValue = activity.getString(R.string.King);
		} else if (value == 12) {
			strValue = activity.getString(R.string.Queen);
		} else if (value == 11) {
			strValue = activity.getString(R.string.Jack);
		} else {
			strValue = String.valueOf(value);
		}

		return strValue;
	}
	
	public String getShortValueString(){
		
		String strValue;
		
		// Setzen des Werts
				if (value == 14) {
					strValue = "A";
				} else if (value == 13) {
					strValue = "K";
				} else if (value == 12) {
					strValue = "Q";
				} else if (value == 11) {
					strValue = "J";
				} else {
					strValue = String.valueOf(value);
				}

				return strValue;
	}

	public int getColor() {
		return color;
	}

	public int getValue() {
		return value;
	}

}
