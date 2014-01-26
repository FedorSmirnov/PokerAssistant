package com.FedorApps.pokerassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

public class CardPick extends FragmentActivity implements ActivityWithFragments {

	private int setCardNum;

	// Zuordnung der Farben: 1 => spade; 2 => heart; 3 => diamond; 4 => club
	private int cardColor;

	private int cardValue;

	private int cardNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_pick);

		Intent intent = getIntent();
		cardNum = intent.getExtras().getInt(PreferenceContainer.keyCardNum);

		setCardNum = intent.getExtras().getInt(
				PreferenceContainer.keySetCardNum);

		SharedPreferences pref = getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = pref.edit();

		String keyColor = getKeyStringColor(setCardNum);
		String keyValue = getKeyStringValue(setCardNum);

		editor.putInt(keyColor, 0);
		editor.putInt(keyValue, 0);
		editor.commit();

		String title;
		switch (cardNum) {
		case 1:
			title = "Set your hand card color.";
			break;
		case 2:
			title = "Set your hand card color.";
			break;
		case 3:
			title = "Set the flop card color.";

			break;
		case 4:
			title = "Set the turn card color.";
			break;
		case 5:
			title = "Set the river card color.";
			break;
		default:
			title = "Set the card color.";
		}

		this.setTitle(title);

		if (savedInstanceState != null) {
			return;
		}

		FragColorPick colorFrag = new FragColorPick();

		getSupportFragmentManager().beginTransaction()
				.add(R.id.probcalc_frag_container, colorFrag).commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.card_pick, menu);
		return true;
	}

	@Override
	public void onFragItemSelected(int res, boolean color) {

		if (color) {
			// color=true => Farbe wurde gerade gewählt
			// Wert setzen
			cardColor = res;
			String title;

			switch (cardNum) {
			case 1:
				title = "Set your hand card value.";
				break;
			case 2:
				title = "Set your hand card value.";
				break;
			case 3:
				title = "Set the flop card value";
				break;
			case 4:
				title = "Set the turn card value.";
				break;
			case 5:
				title = "Set the river card color.";
				break;
			default:
				title = "Set the card value.";

			}

			this.setTitle(title);

			// Fragmente austauschen
			FragValPick valPick = new FragValPick();

			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();

			transaction.replace(R.id.probcalc_frag_container, valPick);
			transaction.addToBackStack(null);
			transaction.commit();

		} else {
			cardValue = res;
			finish();

		}

	}

	@Override
	public void finish() {

		updatePickedCards();
		Intent data = new Intent();
		data.putExtra(PreferenceContainer.keyColor, cardColor);
		data.putExtra(PreferenceContainer.keyValue, cardValue);

		if (cardValue == 0) {
			setResult(RESULT_CANCELED);
		} else {

			setResult(RESULT_OK, data);
		}

		super.finish();
	}

	public int getColor() {
		return cardColor;
	}

	private void updatePickedCards() {

		SharedPreferences pref = getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = pref.edit();

		String keyColor = getKeyStringColor(setCardNum);
		String keyValue = getKeyStringValue(setCardNum);

		editor.putInt(keyColor, cardColor);
		editor.putInt(keyValue, cardValue);
		editor.commit();

	}

	private String getKeyStringColor(int i) {

		switch (i) {
		case (1):
			return PreferenceContainer.keyColorFirst;

		case (2):
			return PreferenceContainer.keyColorSecond;

		case (3):
			return PreferenceContainer.keyColorThird;

		case (4):
			return PreferenceContainer.keyColorFourth;

		case (5):
			return PreferenceContainer.keyColorFifth;
		case (6):
			return PreferenceContainer.keyColorSixth;
		case (7):
			return PreferenceContainer.keyColorSeventh;
		default:
			return PreferenceContainer.keyColorEighth;
		}

	}

	private String getKeyStringValue(int i) {

		switch (i) {
		case (1):
			return PreferenceContainer.keyValueFirst;

		case (2):
			return PreferenceContainer.keyValueSecond;

		case (3):
			return PreferenceContainer.keyValueThird;

		case (4):
			return PreferenceContainer.keyValueFourth;

		case (5):
			return PreferenceContainer.keyValueFifth;
		case (6):
			return PreferenceContainer.keyValueSixth;
		case (7):
			return PreferenceContainer.keyValueSeventh;
		default:
			return PreferenceContainer.keyValueEighth;
		}

	}

}
