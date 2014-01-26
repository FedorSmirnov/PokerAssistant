package com.FedorApps.pokerassistant.pokerSession;

import java.util.ArrayList;

public class CircleList extends ArrayList<Player> {

	@Override
	public Player get(int index) {

		while (index > this.size() - 1) {

			index = index - this.size();
		}
		
		if (index < 0){
			index = index + this.size();
		}

		return super.get(index);
	}

}
