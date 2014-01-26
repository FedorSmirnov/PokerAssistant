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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PsMenuBlindActivity extends Activity {

	private int bigBlind;
	private int smallBlind;
	private int ante;

	private EditText bigBlindEdit;
	private TextView smallBlindText;
	private EditText anteEdit;

	private Button okay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ps_menu_blind);

		Intent intent = getIntent();

		bigBlind = intent.getIntExtra(PreferenceContainer.keyCurrentBB, 0);
		smallBlind = intent.getIntExtra(PreferenceContainer.keyCurrentSB, 0);
		ante = intent.getIntExtra(PreferenceContainer.keyCurrentAnte, 0);

		bigBlindEdit = (EditText) findViewById(R.id.ps_menu_blind_editText_bb_edit);
		bigBlindEdit.setText(String.valueOf(bigBlind));

		bigBlindEdit.addTextChangedListener(new TextWatcher() {

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
				String string = bigBlindEdit.getText().toString();

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

		smallBlindText = (TextView) findViewById(R.id.ps_menu_blind_textView_sb_value);
		smallBlindText.setText(String.valueOf(smallBlind));

		anteEdit = (EditText) findViewById(R.id.ps_menu_blind_editText_ante_edit);
		anteEdit.setText(String.valueOf(ante));

		okay = (Button) findViewById(R.id.ps_menu_blind_button_okay);
		okay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String bb = bigBlindEdit.getText().toString();
				String aa = anteEdit.getText().toString();

				if (bb.equals("") || aa.equals("")) {
					Toast.makeText(getApplicationContext(),
							R.string.ps_menu_blind_insert_values_toast,
							Toast.LENGTH_SHORT).show();
					return;
				}

				finish();

			}
		});

	}

	@Override
	public void finish() {

		String b = bigBlindEdit.getText().toString();
		bigBlind = Integer.valueOf(b);

		String s = smallBlindText.getText().toString();
		smallBlind = Integer.valueOf(s);

		String a = anteEdit.getText().toString();
		ante = Integer.valueOf(a);

		Intent intent = new Intent();
		intent.putExtra(PreferenceContainer.keyCurrentBB, bigBlind);
		intent.putExtra(PreferenceContainer.keyCurrentSB, smallBlind);
		intent.putExtra(PreferenceContainer.keyCurrentAnte, ante);

		setResult(RESULT_OK, intent);

		super.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ps_menu_blind, menu);
		return true;
	}

}
