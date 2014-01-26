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
import android.widget.TextView;
import android.widget.Toast;

public class PsMenuPlayerIn extends Activity {

	// Keys der Activity

	public static final String KEY_DO_CHANGE_BOOL = "apply_the_changes";
	public static final String KEY_DO_REMOVE_PLAYER = "remove_the_selected_player";

	// Views der Activity
	private EditText nameEdit;
	private EditText chipsEdit;

	private Button closeButton;
	private Button applyButton;
	private Button removePlayer;

	private TextView handsPlayed;
	private TextView handsWon;
	private TextView handsWonOverall;
	private TextView balance;
	private TextView maxChips;
	private TextView minChips;
	private TextView averageChips;

	// Variablen der Activity

	private String name;
	private int chips;

	private boolean doChange = false;
	private boolean doRemovePlayer = false;

	private int playerNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ps_menu_player_in);

		Intent intent = getIntent();

		int hPlayed = intent.getIntExtra(
				PreferenceContainer.KEY_PS_MENU_PLIN_HANDS_PLAYED, 0);
		handsPlayed = (TextView) findViewById(R.id.ps_menu_player_in_textView_handsPlayedValue);
		handsPlayed.setText(String.valueOf(hPlayed) + " %");

		int hWon = intent.getIntExtra(PreferenceContainer.KEY_PS_MENU_PLIN_HANDS_WON,
				0);
		handsWon = (TextView) findViewById(R.id.ps_menu_player_in_textView_handsWonValue);
		handsWon.setText(String.valueOf(hWon) + " %");

		int hWonOverall = intent.getIntExtra(
				PreferenceContainer.KEY_PS_MENU_PLIN_HANDS_WON_OVERALL, 0);
		handsWonOverall = (TextView) findViewById(R.id.ps_menu_player_in_textView_handsWonOverallValue);
		handsWonOverall.setText(String.valueOf(hWonOverall) + " %");

		int bal = intent.getIntExtra(PreferenceContainer.KEY_PS_MENU_PLIN_BALANCE, 0);
		balance = (TextView) findViewById(R.id.ps_menu_player_in_textView_balanceValue);
		balance.setText(String.valueOf(bal));
		if (bal > 0) {
			balance.setTextColor(getResources().getColor(R.color.GREEN));
		}
		if (bal < 0) {
			balance.setTextColor(getResources().getColor(R.color.RED));
		}

		int max = intent.getIntExtra(
				PreferenceContainer.KEY_PS_MENU_PLIN_MAX_CHIP_AMOUNT, 0);
		maxChips = (TextView) findViewById(R.id.ps_menu_player_in_textView_maximalStackValue);
		maxChips.setText(String.valueOf(max));

		int min = intent.getIntExtra(
				PreferenceContainer.KEY_PS_MENU_PLIN_MIN_CHIP_AMOUNT, 0);
		minChips = (TextView) findViewById(R.id.ps_menu_player_in_textView_minimalStackValue);
		minChips.setText(String.valueOf(min));

		int average = intent.getIntExtra(
				PreferenceContainer.KEY_PS_MENU_PLIN_AVERAGE_CHIP_AMOUNT, 0);
		averageChips = (TextView) findViewById(R.id.ps_menu_player_in_textView_averageStackValue);
		averageChips.setText(String.valueOf(average));

		playerNum = intent.getIntExtra(PreferenceContainer.KEY_PS_MENU_PLIN_PL_NUM, 0);

		name = intent.getStringExtra(PreferenceContainer.KEY_PS_MENU_PLIN_NAME);
		chips = intent.getIntExtra(PreferenceContainer.KEY_PS_MENU_PLIN_PL_CHIPS, 0);

		nameEdit = (EditText) findViewById(R.id.ps_menu_player_in_editText_name);
		nameEdit.setText(name);

		chipsEdit = (EditText) findViewById(R.id.ps_menu_player_in_editText_chips);
		chipsEdit.setText(String.valueOf(chips));

		closeButton = (Button) findViewById(R.id.ps_menu_player_in_button_close);
		closeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		applyButton = (Button) findViewById(R.id.ps_menu_player_in_button_apply);
		applyButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				doChange = true;

				name = nameEdit.getText().toString();

				String string = chipsEdit.getText().toString();

				if (name.equals("") || string.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Set the player characteristics",
							Toast.LENGTH_SHORT).show();
					return;
				}

				chips = Integer.valueOf(string);
				finish();

			}
		});

		removePlayer = (Button) findViewById(R.id.ps_menu_player_in_button_remove_player);
		removePlayer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				doRemovePlayer = true;
				finish();
			}
		});
	}

	@Override
	public void finish() {

		Intent intent = new Intent();

		intent.putExtra(KEY_DO_CHANGE_BOOL, doChange);
		intent.putExtra(PreferenceContainer.KEY_PS_MENU_PLIN_PL_NUM, playerNum);
		intent.putExtra(PreferenceContainer.KEY_PS_MENU_PLIN_NAME, name);
		intent.putExtra(PreferenceContainer.KEY_PS_MENU_PLIN_PL_CHIPS, chips);
		intent.putExtra(KEY_DO_REMOVE_PLAYER, doRemovePlayer);

		setResult(RESULT_OK, intent);

		super.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ps_menu_player_in, menu);
		return true;
	}

}
