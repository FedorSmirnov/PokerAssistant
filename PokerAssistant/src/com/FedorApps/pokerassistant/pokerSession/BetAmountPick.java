package com.FedorApps.pokerassistant.pokerSession;

import com.FedorApps.pokerassistant.PreferenceContainer;
import com.FedorApps.pokerassistant.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BetAmountPick extends Activity {

	// Variablen der Activity
	private int currentBet;
	private int[] betValues = new int[9];
	private int pickedValue = 0;
	private int playerMoney;

	// Steuerelemente

	private Button buttonAllIn;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private Button button5;
	private Button button6;
	private Button button7;
	private Button button8;
	private Button button9;
	private Button buttonOk;

	private EditText otherEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bet_amount_pick);

		Intent intent = getIntent();
		currentBet = intent.getIntExtra(PreferenceContainer.keyCurrentBet, 0);
		betValues = intent.getIntArrayExtra(PreferenceContainer.keyMadeBets);
		playerMoney = intent.getIntExtra(PreferenceContainer.keyPlayerMoney, 0);

		// Setzen des Dialogtitels

		String title = "Set the bet";
		this.setTitle(title);

		// Setzen der Views
		buttonAllIn = (Button) findViewById(R.id.betap_buttonAllIn);
		button1 = (Button) findViewById(R.id.betap_button1);
		button2 = (Button) findViewById(R.id.betap_button2);
		button3 = (Button) findViewById(R.id.betap_button3);
		button4 = (Button) findViewById(R.id.betap_button4);
		button5 = (Button) findViewById(R.id.betap_button5);
		button6 = (Button) findViewById(R.id.betap_button6);
		button7 = (Button) findViewById(R.id.betap_button7);
		button8 = (Button) findViewById(R.id.betap_button8);
		button9 = (Button) findViewById(R.id.betap_button9);
		buttonOk = (Button) findViewById(R.id.betap_button_other_okay);

		otherEdit = (EditText) findViewById(R.id.betap_edit_other);

		// Setzen des Zustands sowie der Listener für die Buttons 1 bis 9
		for (int i = 0; i < betValues.length; i++) {

			Button button;

			switch (i) {
			case 0:
				button = button1;
				break;
			case 1:
				button = button2;
				break;
			case 2:
				button = button3;
				break;
			case 3:
				button = button4;
				break;
			case 4:
				button = button5;
				break;
			case 5:
				button = button6;
				break;
			case 6:
				button = button7;
				break;
			case 7:
				button = button8;
				break;
			default:
				button = button9;
				break;
			}

			if (betValues[i] != 0) {
				button.setText(String.valueOf(betValues[i]));
				if (betValues[i] <= currentBet || betValues[i] > playerMoney) {
					button.setEnabled(false);
				} else {
					button.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							Button button = (Button) v;

							int value = Integer.valueOf(button.getText()
									.toString());
							pickedValue = value;
							finish();

						}
					});
				}
			} else {
				button.setText("---");
				button.setEnabled(false);
			}

		}

		buttonAllIn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				pickedValue = -100;
				finish();

			}
		});

		buttonOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String string = otherEdit.getText().toString();

				if (string.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Choose a bet or enter a new one.",
							Toast.LENGTH_SHORT).show();
				} else {
					pickedValue = Integer.valueOf(string);

					if (pickedValue <= currentBet) {
						Toast.makeText(getApplicationContext(),
								"Set a higher number", Toast.LENGTH_SHORT)
								.show();
					} else if (pickedValue > playerMoney) {

						Toast.makeText(getApplicationContext(),
								"Not enough chips for this bet.",
								Toast.LENGTH_SHORT).show();

					} else {
						finish();
					}
				}

			}
		});

	}

	@Override
	public void finish() {

		Intent data = new Intent();
		data.putExtra(PreferenceContainer.keyPickedValue, pickedValue);

		if (pickedValue == 0) {
			setResult(RESULT_CANCELED);
		} else {
			setResult(RESULT_OK, data);
		}

		super.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bet_amount_pick, menu);
		return true;
	}

}
