package com.FedorApps.pokerassistant.pokerSession;

import com.FedorApps.pokerassistant.PreferenceContainer;
import com.FedorApps.pokerassistant.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PsStartConfigurationActivity extends Activity {

	// Elemente, mit denen der User interagiert

	Button cancel;
	Button start;

	Button plus;
	Button minus;
	TextView playerNumText;

	EditText initialCAtext;
	EditText bigBlindText;
	TextView smallBlindText;
	EditText anteEdit;

	CheckBox blindLevelsBox;
	TextView increaseText;
	EditText minEditText;
	TextView minText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ps_start_configuration);

		this.setTitle(getResources().getString(R.string.ps_confi_diaTitle));

		cancel = (Button) findViewById(R.id.ps_conf_button_neg);
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		playerNumText = (TextView) findViewById(R.id.ps_conf_textViewPlNum);

		int playerNum = PreferenceContainer.loadInt(
				PreferenceContainer.KEY_PLAYER_NUM, this);
		playerNumText.setText(String.valueOf(playerNum));

		plus = (Button) findViewById(R.id.ps_conf_button_plNum_plus);
		plus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int number;
				String string;

				string = playerNumText.getText().toString();
				number = Integer.valueOf(string);

				if (number < 10) {
					number++;
				}

				string = String.valueOf(number);
				playerNumText.setText(string);

			}
		});

		minus = (Button) findViewById(R.id.ps_conf_button_plNum_minus);
		minus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int number;
				String string;

				string = playerNumText.getText().toString();
				number = Integer.valueOf(string);

				if (number > 2) {
					number--;
				}

				string = String.valueOf(number);
				playerNumText.setText(string);

			}
		});

		initialCAtext = (EditText) findViewById(R.id.ps_conf_editText_initialCA);

		int initial = PreferenceContainer.loadInt(
				PreferenceContainer.KEY_INITIAN_CHIPS, this);
		initialCAtext.setText(String.valueOf(initial));

		bigBlindText = (EditText) findViewById(R.id.ps_conf_editText_BigBlind);
		int big = PreferenceContainer.loadInt(
				PreferenceContainer.KEY_BIG_BLIND, this);
		bigBlindText.setText(String.valueOf(big));

		smallBlindText = (TextView) findViewById(R.id.ps_conf_editText_SmallBlind);
		int small = PreferenceContainer.loadInt(
				PreferenceContainer.KEY_SMALL_BLIND, this);
		smallBlindText.setText(String.valueOf(small));

		bigBlindText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				String string = bigBlindText.getText().toString();

				if (!string.equals("")) {
					int big = Integer.valueOf(string);
					if ((big % 2) == 1) {
						big--;
					}

					int small = big / 2;
					smallBlindText.setText(String.valueOf(small));
				}
			}
		});

		blindLevelsBox = (CheckBox) findViewById(R.id.ps_conf_chechBox_blindLevel);
		boolean levels = PreferenceContainer.loadBool(
				PreferenceContainer.KEY_BLIND_LEVEL_BOOL, this);
		blindLevelsBox.setChecked(levels);

		increaseText = (TextView) findViewById(R.id.ps_conf_textView_blindIncrease);
		minEditText = (EditText) findViewById(R.id.ps_conf_editText_blindLevelTime);
		int minutes = PreferenceContainer.loadInt(
				PreferenceContainer.KEY_BLIND_LEVEL_TIME_INT, this);
		minEditText.setText(String.valueOf(minutes));
		minText = (TextView) findViewById(R.id.ps_conf_textView_blindLevelTime);

		if (!levels) {
			increaseText.setEnabled(false);
			increaseText.setAlpha(0);
			minText.setEnabled(false);
			minText.setAlpha(0);
			minEditText.setEnabled(false);
			minEditText.setAlpha(0);
		} else {
			increaseText.setEnabled(true);
			increaseText.setAlpha(1);
			minText.setEnabled(true);
			minText.setAlpha(1);
			minEditText.setEnabled(true);
			minEditText.setAlpha(1);

		}

		blindLevelsBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (blindLevelsBox.isChecked()) {
							increaseText.setEnabled(true);
							increaseText.setAlpha(1);
							minText.setEnabled(true);
							minText.setAlpha(1);
							minEditText.setEnabled(true);
							minEditText.setAlpha(1);
						} else {
							increaseText.setEnabled(false);
							increaseText.setAlpha(0);
							minText.setEnabled(false);
							minText.setAlpha(0);
							minEditText.setEnabled(false);
							minEditText.setAlpha(0);
						}
					}
				});

		anteEdit = (EditText) findViewById(R.id.ps_conf_editText_Ante);
		int ante = PreferenceContainer.loadInt(PreferenceContainer.KEY_ANTE,
				this);
		anteEdit.setText(String.valueOf(ante));
		start = (Button) findViewById(R.id.ps_conf_button_pos);
		start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				saveValues();

				Intent intent = new Intent(getApplicationContext(),
						PokerSession.class);
				intent.putExtra(PokerSession.KEY_NEW_BOOL, true);

				startActivity(intent);
				finish();

			}
		});

	}

	private void saveValues() {

		String smallText = smallBlindText.getText().toString();
		String bigText = bigBlindText.getText().toString();
		String anteText = anteEdit.getText().toString();

		if (smallText.equals("") || bigText.equals("") || anteText.equals("")) {
			Toast.makeText(getApplicationContext(),
					R.string.ps_menu_blind_insert_values_toast,
					Toast.LENGTH_SHORT).show();
			return;
		}

		String initialText = initialCAtext.getText().toString();

		if (initialText.equals("")) {
			Toast.makeText(getApplicationContext(),
					"Enter a valid initial chip amount", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		String time = minEditText.getText().toString();

		if (time.equals("")) {
			Toast.makeText(getApplicationContext(),
					"Enter a valid time for the blind levels.",
					Toast.LENGTH_SHORT).show();
			return;
		}

		int small = Integer.valueOf(smallText);
		int big = Integer.valueOf(bigText);
		int ante = Integer.valueOf(anteText);
		int initial = Integer.valueOf(initialText);
		boolean blindLevels = blindLevelsBox.isChecked();
		int minAmount = Integer.valueOf(time);
		String playerNumString = playerNumText.getText().toString();
		int playerNum = Integer.valueOf(playerNumString);

		PreferenceContainer.saveInt(PreferenceContainer.KEY_INITIAN_CHIPS,
				this, initial);
		PreferenceContainer.saveInt(PreferenceContainer.KEY_BIG_BLIND, this,
				big);
		PreferenceContainer.saveInt(PreferenceContainer.KEY_ANTE, this, ante);
		PreferenceContainer.saveInt(PreferenceContainer.KEY_SMALL_BLIND, this,
				small);
		PreferenceContainer.saveInt(PreferenceContainer.KEY_PLAYER_NUM, this,
				playerNum);
		PreferenceContainer.saveBool(PreferenceContainer.KEY_BLIND_LEVEL_BOOL,
				this, blindLevels);
		PreferenceContainer.saveInt(
				PreferenceContainer.KEY_BLIND_LEVEL_TIME_INT, this, minAmount);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ps_start_configuration, menu);
		return true;
	}

}
