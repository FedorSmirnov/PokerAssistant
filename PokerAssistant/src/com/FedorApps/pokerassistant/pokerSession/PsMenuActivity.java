package com.FedorApps.pokerassistant.pokerSession;

import com.FedorApps.pokerassistant.PreferenceContainer;
import com.FedorApps.pokerassistant.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class PsMenuActivity extends Activity {

	// Requests der Activity

	public static final int BLIND_ADJUST_REQUEST = 1000;

	// Keys der Activity

	public static final String KEY_CLOSE_PS_BOOL = "leave the poker session";
	public static final String KEY_SET_DEALER_BOOL = "set a new dealer";
	public static final String KEY_SET_NEW_BLIND_BOOL = "set a new blind level";

	// Views der Activity

	private Button close;
	private Button setDealer;
	private Button adjustBlinds;

	// Rückgabewerte der Activity

	private boolean leaveSession = false;
	private boolean setNewDealer = false;
	private boolean setNewBlindLevel = false;

	private int bigBlind;
	private int smallBlind;
	private int ante;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ps_menu);

		// Setzen der Buttons

		close = (Button) findViewById(R.id.ps_menu_button_ps_close);

		setDealer = (Button) findViewById(R.id.ps_menu_button_dealer_set);

		adjustBlinds = (Button) findViewById(R.id.ps_menu_button_blinds_adjust);

		// Auslesen der Eingabe

		Intent intent = getIntent();

		bigBlind = intent.getIntExtra(PreferenceContainer.keyCurrentBB, 0);
		smallBlind = intent.getIntExtra(PreferenceContainer.keyCurrentSB, 0);
		ante = intent.getIntExtra(PreferenceContainer.keyCurrentAnte, 0);

		// Setzen der Listener

		close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				leaveSession = true;
				finish();

			}
		});

		setDealer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setNewDealer = true;
				finish();

			}
		});

		adjustBlinds.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				setNewBlindLevel = true;

				Intent intent = new Intent(getApplicationContext(),
						PsMenuBlindActivity.class);

				intent.putExtra(PreferenceContainer.keyCurrentBB, bigBlind);
				intent.putExtra(PreferenceContainer.keyCurrentSB, smallBlind);
				intent.putExtra(PreferenceContainer.keyCurrentAnte, ante);

				startActivityForResult(intent, BLIND_ADJUST_REQUEST);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ps_menu, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == BLIND_ADJUST_REQUEST && resultCode == RESULT_OK) {

			bigBlind = data.getIntExtra(PreferenceContainer.keyCurrentBB, 0);
			smallBlind = data.getIntExtra(PreferenceContainer.keyCurrentSB, 0);
			ante = data.getIntExtra(PreferenceContainer.keyCurrentAnte, 0);
			finish();

		}

	}

	@Override
	public void finish() {

		Intent intent = new Intent();
		intent.putExtra(KEY_CLOSE_PS_BOOL, leaveSession);
		intent.putExtra(KEY_SET_DEALER_BOOL, setNewDealer);

		intent.putExtra(KEY_SET_NEW_BLIND_BOOL, setNewBlindLevel);
		intent.putExtra(PreferenceContainer.keyCurrentBB, bigBlind);
		intent.putExtra(PreferenceContainer.keyCurrentSB, smallBlind);
		intent.putExtra(PreferenceContainer.keyCurrentAnte, ante);

		setResult(RESULT_OK, intent);

		super.finish();
	}

}
