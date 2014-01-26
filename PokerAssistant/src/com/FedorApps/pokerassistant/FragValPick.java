package com.FedorApps.pokerassistant;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragValPick extends Fragment {

	// Schnittstelle zur aufrufenden Activity
	private ActivityWithFragments listener;

	// Keys des Fragments

	// Key zur Anzahl gewählter Karten

	private static final String keyColorFirst = "FRAG_VAL_COLOR_FIRST";
	private static final String keyValueFirst = "FRAG_VAL_VALUE_FIRST";

	private static final String keyColorSecond = "FRAG_VAL_COLOR_SECOND";
	private static final String keyValueSecond = "FRAG_VAL_VALUE_SECOND";

	private static final String keyColorThird = "FRAG_VAL_COLOR_THIRD";
	private static final String keyValueThird = "FRAG_VAL_VALUE_THIRD";

	private static final String keyColorFourth = "FRAG_VAL_COLOR_FOURTH";
	private static final String keyValueFourth = "FRAG_VAL_VALUE_FOURTH";

	private static final String keyColorFifth = "FRAG_VAL_COLOR_FIFTH";
	private static final String keyValueFifth = "FRAG_VAL_VALUE_FIFTH";

	private static final String keyColorSixth = "FRAG_VAL_COLOR_SIXTH";
	private static final String keyValueSixth = "FRAG_VAL_VALUE_SIXTH";

	private static final String keyColorSeventh = "FRAG_VAL_COLOR_SEVENTH";
	private static final String keyValueSeventh = "FRAG_VAL_VALUE_SEVENTH";

	private static final String keyColorEighth = "FRAG_VAL_COLOR_EIGHTH";
	private static final String keyValueEighth = "FRAG_VAL_VALUE_EIGHTH";

	// Sachen, die gemerkt werden müssen

	// Farbe der Karte, die gerade gesetzt wird.
	private int curColor = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Setzten des Layouts
		View view = inflater
				.inflate(R.layout.frag_value_pick, container, false);

		// Setzten der Farbe der gerade verarbeiteten Karte
		curColor = listener.getColor();

		// Für jeden Button: Setzen des OnClick Listeners
		Button ace = (Button) view
				.findViewById(R.id.probcalc_valpick_button_ace);
		ace.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onFragItemSelected(14, false);

			}
		});

		Button queen = (Button) view
				.findViewById(R.id.probcalc_valpick_button_queen);
		queen.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onFragItemSelected(12, false);

			}
		});

		Button king = (Button) view
				.findViewById(R.id.probcalc_valpick_button_king);
		king.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onFragItemSelected(13, false);

			}
		});

		Button jack = (Button) view
				.findViewById(R.id.probcalc_valpick_button_jack);
		jack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onFragItemSelected(11, false);

			}
		});

		Button b10 = (Button) view
				.findViewById(R.id.probcalc_valpick_button_10);
		b10.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onFragItemSelected(10, false);

			}
		});

		Button b9 = (Button) view.findViewById(R.id.probcalc_valpick_button_9);
		b9.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onFragItemSelected(9, false);

			}
		});

		Button b8 = (Button) view.findViewById(R.id.probcalc_valpick_button_8);
		b8.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onFragItemSelected(8, false);

			}
		});

		Button b7 = (Button) view.findViewById(R.id.probcalc_valpick_button_7);
		b7.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onFragItemSelected(7, false);

			}
		});

		Button b6 = (Button) view.findViewById(R.id.probcalc_valpick_button_6);
		b6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onFragItemSelected(6, false);

			}
		});

		Button b5 = (Button) view.findViewById(R.id.probcalc_valpick_button_5);
		b5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onFragItemSelected(5, false);

			}
		});

		Button b4 = (Button) view.findViewById(R.id.probcalc_valpick_button_4);
		b4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onFragItemSelected(4, false);

			}
		});

		Button b3 = (Button) view.findViewById(R.id.probcalc_valpick_button_3);
		b3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onFragItemSelected(3, false);

			}
		});

		Button b2 = (Button) view.findViewById(R.id.probcalc_valpick_button_2);
		b2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onFragItemSelected(2, false);

			}
		});

		// Auslesen, wie viele Karten schon gesetzt wurden

		for (int i = 1; i <= 8; i++) {

			// Bestimmen der Strings, die als keys verwendet werden.
			String colorKey = getKeyStringColor(i);
			String valueKey = getKeyStringValue(i);

			int pickedColor = loadInt(colorKey);

			if (pickedColor != 0) {

				int pickedValue = loadInt(valueKey);

				if (pickedColor == curColor) {

					// Deaktivieren der Karten, die bereits gewählt wurden.
					disableButton(pickedValue, view);
				}
			}
		}

		return view;

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		if (activity instanceof ActivityWithFragments) {
			listener = (ActivityWithFragments) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement MyListFragment.onItemSelectedListener");
		}
	}

	private void disableButton(int value, View view) {

		Button disable;

		switch (value) {
		case 2:
			disable = (Button) view
					.findViewById(R.id.probcalc_valpick_button_2);
			disable.setEnabled(false);
			disable.setBackgroundColor(getResources().getColor(
					R.color.WHITE_50_trans));
			return;

		case 3:
			disable = (Button) view
					.findViewById(R.id.probcalc_valpick_button_3);
			disable.setEnabled(false);
			disable.setBackgroundColor(getResources().getColor(
					R.color.WHITE_50_trans));
			return;

		case 4:
			disable = (Button) view
					.findViewById(R.id.probcalc_valpick_button_4);
			disable.setEnabled(false);
			disable.setBackgroundColor(getResources().getColor(
					R.color.WHITE_50_trans));
			return;

		case 5:
			disable = (Button) view
					.findViewById(R.id.probcalc_valpick_button_5);
			disable.setEnabled(false);
			disable.setBackgroundColor(getResources().getColor(
					R.color.WHITE_50_trans));
			return;

		case 6:
			disable = (Button) view
					.findViewById(R.id.probcalc_valpick_button_6);
			disable.setEnabled(false);
			disable.setBackgroundColor(getResources().getColor(
					R.color.WHITE_50_trans));
			return;

		case 7:
			disable = (Button) view
					.findViewById(R.id.probcalc_valpick_button_7);
			disable.setEnabled(false);
			disable.setBackgroundColor(getResources().getColor(
					R.color.WHITE_50_trans));
			return;

		case 8:
			disable = (Button) view
					.findViewById(R.id.probcalc_valpick_button_8);
			disable.setEnabled(false);
			disable.setBackgroundColor(getResources().getColor(
					R.color.WHITE_50_trans));
			return;

		case 9:
			disable = (Button) view
					.findViewById(R.id.probcalc_valpick_button_9);
			disable.setEnabled(false);
			disable.setBackgroundColor(getResources().getColor(
					R.color.WHITE_50_trans));
			return;

		case 10:
			disable = (Button) view
					.findViewById(R.id.probcalc_valpick_button_10);
			disable.setEnabled(false);
			disable.setBackgroundColor(getResources().getColor(
					R.color.WHITE_50_trans));
			return;

		case 11:
			disable = (Button) view
					.findViewById(R.id.probcalc_valpick_button_jack);
			disable.setEnabled(false);
			disable.setBackgroundColor(getResources().getColor(
					R.color.WHITE_50_trans));
			return;
		case 12:
			disable = (Button) view
					.findViewById(R.id.probcalc_valpick_button_queen);
			disable.setEnabled(false);
			disable.setBackgroundColor(getResources().getColor(
					R.color.WHITE_50_trans));
			return;
		case 13:
			disable = (Button) view
					.findViewById(R.id.probcalc_valpick_button_king);
			disable.setEnabled(false);
			disable.setBackgroundColor(getResources().getColor(
					R.color.WHITE_50_trans));
			return;
		case 14:
			disable = (Button) view
					.findViewById(R.id.probcalc_valpick_button_ace);
			disable.setEnabled(false);
			disable.setBackgroundColor(getResources().getColor(
					R.color.WHITE_50_trans));
			return;

		}
	}

	private int loadInt(String key) {

		SharedPreferences pref = getActivity().getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);

		int result = pref.getInt(key, 0);
		return result;

	}

	private String getKeyStringColor(int i) {

		switch (i) {
		case (1):
			return keyColorFirst;

		case (2):
			return keyColorSecond;

		case (3):
			return keyColorThird;

		case (4):
			return keyColorFourth;

		case (5):
			return keyColorFifth;
		case (6):
			return keyColorSixth;
		case (7):
			return keyColorSeventh;
		default:
			return keyColorEighth;
		}

	}

	private String getKeyStringValue(int i) {

		switch (i) {
		case (1):
			return keyValueFirst;

		case (2):
			return keyValueSecond;

		case (3):
			return keyValueThird;

		case (4):
			return keyValueFourth;

		case (5):
			return keyValueFifth;
		case (6):
			return keyValueSixth;
		case (7):
			return keyValueSeventh;
		default:
			return keyValueEighth;
		}

	}

}
