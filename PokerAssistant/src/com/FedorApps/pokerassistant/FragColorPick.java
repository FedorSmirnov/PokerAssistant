package com.FedorApps.pokerassistant;

import android.app.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class FragColorPick extends Fragment {

	private ActivityWithFragments listener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Layout setzen
		View view = inflater
				.inflate(R.layout.frag_color_pick, container, false);

		// Den view Buttons ihre Listener zuweisen

		ImageButton spade = (ImageButton) view.findViewById(R.id.probcalc_button_spade);
		spade.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				listener.onFragItemSelected(1, true);
			}
		});

		ImageButton heart = (ImageButton) view.findViewById(R.id.probcalc_button_heart);
		heart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				listener.onFragItemSelected(2, true);

			}
		});

		ImageButton diamond = (ImageButton) view
				.findViewById(R.id.probcalc_button_diamond);
		diamond.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				listener.onFragItemSelected(3, true);

			}
		});

		ImageButton club = (ImageButton) view.findViewById(R.id.probcalc_button_club);
		club.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				listener.onFragItemSelected(4, true);

			}
		});

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

}
