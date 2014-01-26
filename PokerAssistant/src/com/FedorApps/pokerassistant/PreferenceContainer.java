package com.FedorApps.pokerassistant;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.FedorApps.pokerassistant.R;

public class PreferenceContainer {

	// Keys für die verschiedenen ladbaren Sachen

	// Keys der gespeicherten Karten (Cardpick für PS und OC sowie PG)
	public static final String keyColorFirst = "FRAG_VAL_COLOR_FIRST";
	public static final String keyValueFirst = "FRAG_VAL_VALUE_FIRST";

	public static final String keyColorSecond = "FRAG_VAL_COLOR_SECOND";
	public static final String keyValueSecond = "FRAG_VAL_VALUE_SECOND";

	public static final String keyColorThird = "FRAG_VAL_COLOR_THIRD";
	public static final String keyValueThird = "FRAG_VAL_VALUE_THIRD";

	public static final String keyColorFourth = "FRAG_VAL_COLOR_FOURTH";
	public static final String keyValueFourth = "FRAG_VAL_VALUE_FOURTH";

	public static final String keyColorFifth = "FRAG_VAL_COLOR_FIFTH";
	public static final String keyValueFifth = "FRAG_VAL_VALUE_FIFTH";

	public static final String keyColorSixth = "FRAG_VAL_COLOR_SIXTH";
	public static final String keyValueSixth = "FRAG_VAL_VALUE_SIXTH";

	public static final String keyColorSeventh = "FRAG_VAL_COLOR_SEVENTH";
	public static final String keyValueSeventh = "FRAG_VAL_VALUE_SEVENTH";

	public static final String keyColorEighth = "FRAG_VAL_COLOR_EIGHTH";
	public static final String keyValueEighth = "FRAG_VAL_VALUE_EIGHTH";

	public static final String keyOppoNum = "OPPONENT_NUM";

	public static final String keyPot = "pot_content";
	public static final String keyPotWithBets = "pot_with_bets_content";

	public static final String keyCurrentBetValue = "current_bet_value";

	public static final String keyPhaseValue = "current_phase_value";

	// keys für das Speichern der CommunityCards

	public static final String keyCardColorFlop1 = "card_color_flop1";
	public static final String keyCardValueFlop1 = "card_value_flop1";

	public static final String keyCardColorFlop2 = "card_color_flop2";
	public static final String keyCardValueFlop2 = "card_value_flop2";

	public static final String keyCardColorFlop3 = "card_color_flop3";
	public static final String keyCardValueFlop3 = "card_value_flop3";

	public static final String keyCardColorTurn = "card_color_turn";
	public static final String keyCardValueTurn = "card_value_turn";

	public static final String keyCardColorRiver = "card_color_river";
	public static final String keyCardValueRiver = "card_value_river";

	// Keys für das Speichern der Konfieingaben, die bei der letzten Benutzung
	// gemacht wurden. (wahrscheinlich alles müll)

	public static final String KEY_PLAYER_NUM = "player_number_key_123";
	public static final String KEY_BIG_BLIND = "big_blind_key_123";
	public static final String KEY_BIG_BLIND_CURRENT = "current_big_blind_key_123";
	public static final String KEY_SMALL_BLIND = "small_blind_key_123";
	public static final String KEY_SMALL_BLIND_CURRENT = "current_small_blind_key_123";
	public static final String KEY_INITIAN_CHIPS = "initial_chip_key_123";
	public static final String KEY_ANTE = "ante_key_123";
	public static final String KEY_ANTE_CURRENT = "current_ante_key_123";

	public static final String KEY_BLIND_DIFFERENCE_TIME = "time_to_next_blind_raise";

	public static final String KEY_BLIND_LEVEL_BOOL = "do_blind_levels_key_123";
	public static final String KEY_BLIND_LEVEL_TIME_INT = "blind_level_time_123";

	// Keys für die Sachen, die für das Confi Menü des Practice Match
	// gespeichert werden müssen.

	public static final String KEY_PLAYER_NUM_PM = "player_number_pm";
	public static final String KEY_DIFFICULTY = "difficulty_chosen_last_time";
	public static final String KEY_NORM_UNLOCK = "normal_unlocked_bool";
	public static final String KEY_HARD_UNLOCK = "hard_unlocked_bool";

	// Keys für die Kommunikation zwischen den Activities

	// Keys für die CardPick Activity
	public static final String keyColor = "Card_Color";
	public static final String keyValue = "Card_Value";
	public static final String keyCardNum = "Card_Number";
	public static final String keySetCardNum = "Set_Card_Number";

	// Für den Aufruf der Betsetzactivity
	public static final String keyCurrentBet = "CURRENT_BET";
	public static final String keyMadeBets = "MADE_BETS";
	public static final String keyPickedValue = "BAP_PICKED_VALUE";
	public static final String keyPlayerMoney = "BAP_PLAYER_MONEY";

	// Für den Menüaufruf
	public static final String keyCurrentBB = "CURRENT_BIG_BLIND";
	public static final String keyCurrentSB = "CURRENT_SMALL_BLIND";
	public static final String keyCurrentAnte = "CURRENT_ANTE";

	// Keys, um mit dem Player out Menu zu kommunizieren

	public static final String KEY_PS_MENU_PLOUT_START_AMOUNT = "start_chips_stack";
	public static final String KEY_PS_MENU_PLOUT_PL_NUM = "number_of_the_out_player";

	// Keys, um mit dem Player in Menu zu kommunizieren

	public static final String KEY_PS_MENU_PLIN_NAME = "name_of_chosen_player";
	public static final String KEY_PS_MENU_PLIN_PL_CHIPS = "chips_of_chosen_player";
	public static final String KEY_PS_MENU_PLIN_PL_NUM = "number_of_the_in_player";
	public static final String KEY_PS_MENU_PLIN_HANDS_PLAYED = "number_of_hands_played";
	public static final String KEY_PS_MENU_PLIN_HANDS_WON = "number_of_hands_won";
	public static final String KEY_PS_MENU_PLIN_HANDS_WON_OVERALL = "number_of_hands_won_overall";
	public static final String KEY_PS_MENU_PLIN_BALANCE = "player_balance";
	public static final String KEY_PS_MENU_PLIN_MAX_CHIP_AMOUNT = "player_max_amount";
	public static final String KEY_PS_MENU_PLIN_MIN_CHIP_AMOUNT = "player_min_amount";
	public static final String KEY_PS_MENU_PLIN_AVERAGE_CHIP_AMOUNT = "player_average_amount";

	// Keys um mit dem Service zu kommunizieren

	public static final String oddKey = "Key_For_The_Odds";

	public static int loadInt(String key, Activity activity) {

		SharedPreferences pref = activity.getSharedPreferences(
				activity.getString(R.string.preference_file_key),
				Context.MODE_PRIVATE);

		int result = pref.getInt(key, 4);
		return result;

	}

	public static void saveInt(String key, Activity activity, int value) {
		SharedPreferences pref = activity.getSharedPreferences(
				activity.getString(R.string.preference_file_key),
				Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static boolean loadBool(String key, Activity activity) {
		SharedPreferences pref = activity.getSharedPreferences(
				activity.getString(R.string.preference_file_key),
				Context.MODE_PRIVATE);

		boolean result = pref.getBoolean(key, false);
		return result;
	}

	public static void saveBool(String key, Activity activity, boolean value) {

		SharedPreferences pref = activity.getSharedPreferences(
				activity.getString(R.string.preference_file_key),
				Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();

	}

	public static void saveLong(String key, Activity activity, long value) {

		SharedPreferences pref = activity.getSharedPreferences(
				activity.getString(R.string.preference_file_key),
				Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = pref.edit();
		editor.putLong(key, value);
		editor.commit();

	}

	public static long loadLong(String key, Activity activity) {
		SharedPreferences pref = activity.getSharedPreferences(
				activity.getString(R.string.preference_file_key),
				Context.MODE_PRIVATE);

		long result = pref.getLong(key, 0);
		return result;
	}

}
