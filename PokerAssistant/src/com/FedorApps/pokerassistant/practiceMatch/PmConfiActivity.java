package com.FedorApps.pokerassistant.practiceMatch;

import com.FedorApps.pokerassistant.PreferenceContainer;
import com.FedorApps.pokerassistant.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PmConfiActivity extends Activity {

	// Elemente, mit denen der User interagiert

	Button cancel;
	Button start;

	Button easy;
	Button normal;
	Button hard;

	int lastPlayerNum;
	int lastDiff;

	boolean normalUnlock;
	boolean hardUnlock;

	Button plus;
	Button minus;
	TextView playerNumText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pm_confi);

		this.setTitle(getResources().getString(R.string.pm_menu_confi_diaTitle));

		easy = (Button) findViewById(R.id.pm_confi_button_easy);
		normal = (Button) findViewById(R.id.pm_confi_button_normal);
		hard = (Button) findViewById(R.id.pm_confi_button_hard);

		cancel = (Button) findViewById(R.id.pm_conf_button_neg);
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		lastDiff = PreferenceContainer.loadInt(
				PreferenceContainer.KEY_DIFFICULTY, this);
		if (lastDiff == 4) {
			lastDiff = 1;
		}

		normalUnlock = PreferenceContainer.loadBool(
				PreferenceContainer.KEY_NORM_UNLOCK, this);
		hardUnlock = PreferenceContainer.loadBool(
				PreferenceContainer.KEY_HARD_UNLOCK, this);

		easy.setBackgroundColor(getResources().getColor(R.color.GREEN_20));

		easy.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				easy.setBackgroundColor(getResources().getColor(R.color.GREEN));
				lastDiff = 1;

				normal.setBackgroundColor(getResources().getColor(
						R.color.android_holo_blue_bright_20_per_cent));
				hard.setBackgroundColor(getResources().getColor(R.color.RED_20));
			}
		});

		normal.setBackgroundColor(getResources().getColor(
				R.color.android_holo_blue_bright_20_per_cent));

		if (normalUnlock) {
			normal.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					normal.setBackgroundColor(getResources().getColor(
							R.color.android_holo_blue_bright));
					lastDiff = 2;

					easy.setBackgroundColor(getResources().getColor(
							R.color.GREEN_20));
					hard.setBackgroundColor(getResources().getColor(
							R.color.RED_20));

				}
			});
		} else {

			normal.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(),
							R.string.pm_toast_normal_unlock, Toast.LENGTH_SHORT)
							.show();

				}
			});

		}

		if (hardUnlock) {

			hard.setBackgroundColor(getResources().getColor(R.color.RED_20));

			hard.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					hard.setBackgroundColor(getResources()
							.getColor(R.color.RED));
					lastDiff = 3;

					easy.setBackgroundColor(getResources().getColor(
							R.color.GREEN_20));
					normal.setBackgroundColor(getResources().getColor(
							R.color.android_holo_blue_bright_20_per_cent));

				}
			});
		} else {

			hard.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(),
							R.string.pm_toast_hard_unlock, Toast.LENGTH_SHORT)
							.show();

				}
			});

		}

		switch (lastDiff) {

		case 3:

			hard.setBackgroundColor(getResources().getColor(R.color.RED));
			lastDiff = 3;

			easy.setBackgroundColor(getResources().getColor(R.color.GREEN_20));
			normal.setBackgroundColor(getResources().getColor(
					R.color.android_holo_blue_bright_20_per_cent));
			break;

		case 2:
			normal.setBackgroundColor(getResources().getColor(
					R.color.android_holo_blue_bright));
			lastDiff = 2;

			easy.setBackgroundColor(getResources().getColor(R.color.GREEN_20));
			hard.setBackgroundColor(getResources().getColor(R.color.RED_20));
			break;

		default:
			easy.setBackgroundColor(getResources().getColor(R.color.GREEN));
			lastDiff = 1;

			normal.setBackgroundColor(getResources().getColor(
					R.color.android_holo_blue_bright_20_per_cent));
			hard.setBackgroundColor(getResources().getColor(R.color.RED_20));
			break;

		}

		playerNumText = (TextView) findViewById(R.id.pm_conf_textViewPlNum);

		int playerNum = PreferenceContainer.loadInt(
				PreferenceContainer.KEY_PLAYER_NUM_PM, this);

		playerNumText.setText(String.valueOf(playerNum));

		plus = (Button) findViewById(R.id.pm_conf_button_plNum_plus);
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

		minus = (Button) findViewById(R.id.pm_conf_button_plNum_minus);
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

		start = (Button) findViewById(R.id.pm_conf_button_pos);

		start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				saveValues();
				Intent intent = new Intent(getApplicationContext(),
						PracticeMatch.class);

				startActivity(intent);
				finish();

			}
		});

	}

	private void saveValues() {

		String playerNumString = playerNumText.getText().toString();
		int playerNum = Integer.valueOf(playerNumString);

		PreferenceContainer.saveInt(PreferenceContainer.KEY_PLAYER_NUM_PM,
				this, playerNum);

		PreferenceContainer.saveInt(PreferenceContainer.KEY_DIFFICULTY, this,
				lastDiff);

	}

}
