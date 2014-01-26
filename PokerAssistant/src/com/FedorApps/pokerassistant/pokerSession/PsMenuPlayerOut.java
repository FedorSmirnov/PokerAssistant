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

public class PsMenuPlayerOut extends Activity {

	// Keys der Activity

	public static final String KEY_REBUY_INT = "rebuy_amount";
	public static final String KEY_REBUY_BOOL = "rebuy_do";

	public static final String KEY_REMOVE_BOOL = "remove_do";

	// Views der Activity

	private TextView rebuyText;
	private EditText rebuyEdit;
	private Button rebuyButton;

	private Button removeButton;

	// Variablen der Activity

	private int playerNum;

	private boolean doRebuy = false;
	private int rebuyChips;

	private boolean doRemove = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ps_menu_player_out);

		Intent intent = getIntent();

		rebuyChips = intent.getIntExtra(
				PreferenceContainer.KEY_PS_MENU_PLOUT_START_AMOUNT, 0);

		playerNum = intent
				.getIntExtra(PreferenceContainer.KEY_PS_MENU_PLOUT_PL_NUM, 0);

		rebuyText = (TextView) findViewById(R.id.ps_menu_player_out_textView_rebuy_money);

		rebuyEdit = (EditText) findViewById(R.id.ps_menu_player_out_editText_rebuy_money);

		rebuyEdit.setText(String.valueOf(rebuyChips));

		rebuyButton = (Button) findViewById(R.id.ps_menu_player_out_button_rebuy);

		removeButton = (Button) findViewById(R.id.ps_menu_player_out_button_remove);

		rebuyButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String string = rebuyEdit.getText().toString();

				if (string.equals("")) {
					Toast.makeText(getApplicationContext(),
							R.string.ps_menu_player_out_rebuy_stack_toast,
							Toast.LENGTH_SHORT).show();
					return;
				}

				rebuyChips = Integer.valueOf(string);

				doRebuy = true;
				doRemove = false;

				finish();

			}
		});

//		removeButton.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				rebuyChips = 0;
//				doRebuy = false;
//
//				doRemove = true;
//
//				finish();
//
//			}
//		});

	}

	@Override
	public void finish() {

		Intent intent = new Intent();

		intent.putExtra(PreferenceContainer.KEY_PS_MENU_PLOUT_PL_NUM, playerNum);
		intent.putExtra(KEY_REBUY_BOOL, doRebuy);
		intent.putExtra(KEY_REBUY_INT, rebuyChips);
		intent.putExtra(KEY_REMOVE_BOOL, doRemove);

		setResult(RESULT_OK, intent);

		super.finish();
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ps_menu_player_out, menu);
		return true;
	}

}
