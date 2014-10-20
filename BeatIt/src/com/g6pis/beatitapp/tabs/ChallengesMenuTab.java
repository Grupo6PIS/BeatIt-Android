package com.g6pis.beatitapp.tabs;

import java.util.List;
import java.util.Random;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts.Intents.UI;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.challenges.bouncinggame.BouncingGameFinished;
import com.g6pis.beatitapp.challenges.bouncinggame.BouncingGameUI;
import com.g6pis.beatitapp.challenges.invitefriends.CanYouPlayFinished;
import com.g6pis.beatitapp.challenges.invitefriends.CanYouPlayUI;
import com.g6pis.beatitapp.challenges.shutthedog.ShutTheDogFinished;
import com.g6pis.beatitapp.challenges.shutthedog.ShutTheDogUI;
import com.g6pis.beatitapp.challenges.songcomplete.SongCompleteFinished;
import com.g6pis.beatitapp.challenges.songcomplete.SongCompleteUI;
import com.g6pis.beatitapp.challenges.textandcolor.TextAndColorFinished;
import com.g6pis.beatitapp.challenges.textandcolor.TextAndColorUI;
import com.g6pis.beatitapp.challenges.usainbolt.UsainBoltFinished;
import com.g6pis.beatitapp.challenges.usainbolt.UsainBoltUI;
import com.g6pis.beatitapp.challenges.wakemeup.WakeMeUpFinished;
import com.g6pis.beatitapp.challenges.wakemeup.WakeMeUpUI;
import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.datatypes.DTState;

public class ChallengesMenuTab extends Fragment implements
		AdapterView.OnItemClickListener {
	private ListView challengeMenu;
	private MyAdapter adapter;
	Random rand = new Random();

	private static final String ID_Usain_Bolt = "1";
	private static final String ID_Wake_Me_Up = "2";
	private static final String ID_Can_You_Play = "3";
	private static final String ID_Calla_Al_Perro = "4";
	private static final String ID_Bouncing_Game = "5";
	private static final String ID_Throw_The_Phone = "6";
	private static final String ID_Catch_Me = "7";
	private static final String ID_TextAndColor = "8";
	private static final String ID_SongComplete = "9";

	// private static final String ID_ = "10";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.challenge_menu_tab,
				container, false);

		((Home) getActivity()).refreshButton.setVisibility(View.INVISIBLE);

		challengeMenu = (ListView) rootView
				.findViewById(R.id.challengesMenu_list);
		challengeMenu.setOnItemClickListener(this);

		DataManager dm = (DataManager) DataManager.getInstance();
		List<DTState> challenges = dm.getChallenges();
		int total_challenges = challenges.size();
		DTState[] items = new DTState[total_challenges];

		for (int index = 0; index < total_challenges; index++) {
			items[index] = challenges.get(index);
		}

		adapter = new MyAdapter(getActivity().getApplicationContext(), items);
		challengeMenu.setAdapter(adapter);

		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent challenge = null;
		if (adapter.getItem(position).getChallengeId().equals(ID_Usain_Bolt)) {
			if (adapter.getItem(position).getCurrentAttempt() == 0)
				challenge = new Intent(getActivity().getApplicationContext(),
						UsainBoltUI.class);
			else
				challenge = new Intent(getActivity().getApplicationContext(),
						UsainBoltFinished.class);
		} else if (adapter.getItem(position).getChallengeId()
				.equals(ID_Wake_Me_Up)) {
			if (adapter.getItem(position).getCurrentAttempt() == 0)
				challenge = new Intent(getActivity().getApplicationContext(),
						WakeMeUpUI.class);
			else
				challenge = new Intent(getActivity().getApplicationContext(),
						WakeMeUpFinished.class);
		} else if (adapter.getItem(position).getChallengeId()
				.equals(ID_Can_You_Play)) {
			if (adapter.getItem(position).getCurrentAttempt() == 0)
				challenge = new Intent(getActivity().getApplicationContext(),
						CanYouPlayUI.class);
			else
				challenge = new Intent(getActivity().getApplicationContext(),
						CanYouPlayFinished.class);
		} else if (adapter.getItem(position).getChallengeId()
				.equals(ID_Calla_Al_Perro)) {
			if (adapter.getItem(position).getCurrentAttempt() == 0)
				challenge = new Intent(getActivity().getApplicationContext(),
						ShutTheDogUI.class);
			else
				challenge = new Intent(getActivity().getApplicationContext(),
						ShutTheDogFinished.class);
		} else if (adapter.getItem(position).getChallengeId()
				.equals(ID_Bouncing_Game)) {
			if (adapter.getItem(position).getCurrentAttempt() == 0)
				challenge = new Intent(getActivity().getApplicationContext(),
						BouncingGameUI.class);
			else
				challenge = new Intent(getActivity().getApplicationContext(),
						BouncingGameFinished.class);

		/*} else if (adapter.getItem(position).getChallengeId()
				.equals(ID_Throw_The_Phone)) {
			if (adapter.getItem(position).getCurrentAttempt() == 0)
				challenge = new Intent(getActivity().getApplicationContext(),
						ThrowThePhoneUI.class);
			else
				challenge = new Intent(getActivity().getApplicationContext(),
						ThrowThePhoneFinished.class);
		} else if (adapter.getItem(position).getChallengeId()
				.equals(ID_Catch_Me)) {
			if (adapter.getItem(position).getCurrentAttempt() == 0)
				challenge = new Intent(getActivity().getApplicationContext(),
						CatchMeUI.class);
			else
				challenge = new Intent(getActivity().getApplicationContext(),
						CatchMeFinished.class);*/

		} else if (adapter.getItem(position).getChallengeId()
				.equals(ID_TextAndColor)) {
			if (adapter.getItem(position).getCurrentAttempt() == 0)
				challenge = new Intent(getActivity().getApplicationContext(),
						TextAndColorUI.class);
			else
				challenge = new Intent(getActivity().getApplicationContext(),
						TextAndColorFinished.class);
		} else if (adapter.getItem(position).getChallengeId()
				.equals(ID_SongComplete)) {
			if (adapter.getItem(position).getCurrentAttempt() == 0)
				challenge = new Intent(getActivity().getApplicationContext(),
						SongCompleteUI.class);
			else
				challenge = new Intent(getActivity().getApplicationContext(),
						SongCompleteFinished.class);

		}
		startActivity(challenge);
		this.getActivity().finish();
	}

	private class MyAdapter extends ArrayAdapter<DTState> {
		private final Context context;
		private final DTState[] objects;

		public MyAdapter(Context context, DTState[] objects) {
			super(context, R.layout.challenge_list_item, objects);
			this.context = context;
			this.objects = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.challenge_list_item,
					parent, false);

			TextView challengeName = (TextView) rowView
					.findViewById(R.id.challengeName_list);
			ImageView challengeIcon = (ImageView) rowView
					.findViewById(R.id.challengeIcon);

			DTState s = objects[position];
			if (s.getChallengeId().equals(ID_Usain_Bolt)) {
				challengeName.setText(getResources().getString(
						R.string.usain_bolt));
				challengeIcon.setImageResource(R.drawable.ic_usain_bolt);
			} else if (s.getChallengeId().equals(ID_Wake_Me_Up)) {
				challengeName.setText(getResources().getString(
						R.string.wake_me_up));
				challengeIcon
						.setImageResource(R.drawable.ic_despertame_a_tiempo);
			} else if (s.getChallengeId().equals(ID_Can_You_Play)) {
				challengeName.setText(getResources().getString(
						R.string.can_you_play));
				challengeIcon.setImageResource(R.drawable.ic_can_you_play);
			} else if (s.getChallengeId().equals(ID_Calla_Al_Perro)) {
				challengeName.setText(getResources().getString(
						R.string.shut_the_dog));
				challengeIcon.setImageResource(R.drawable.ic_calla_al_perro);
			} else if (s.getChallengeId().equals(ID_Bouncing_Game)) {
				challengeName.setText(getResources().getString(
						R.string.bouncing_game));
				challengeIcon.setImageResource(R.drawable.ic_bouncing_game);
			} else if (s.getChallengeId().equals(ID_Throw_The_Phone)) {
				challengeName.setText(getResources().getString(
						R.string.throw_the_phone));
				challengeIcon.setImageResource(R.drawable.ic_throw_the_phone);
			} else if (s.getChallengeId().equals(ID_Catch_Me)) {
				challengeName.setText(getResources().getString(
						R.string.catch_me));
				challengeIcon.setImageResource(R.drawable.ic_catch_me);
			} else if (s.getChallengeId().equals(ID_TextAndColor)) {
				challengeName.setText(getResources().getString(
						R.string.text_and_color));
				challengeIcon.setImageResource(R.drawable.ic_text_and_color);
			} else if (s.getChallengeId().equals(ID_SongComplete)) {
				challengeName.setText(getResources().getString(
						R.string.song_complete));
				challengeIcon.setImageResource(R.drawable.ic_song_complete);
			}

			if (s.getCurrentAttempt() == 0)
				rowView.setBackgroundColor(getResources().getColor(
						R.color.blanco));
			else {
				rowView.setBackgroundColor(getResources()
						.getColor(R.color.gris));
				((ImageView) rowView.findViewById(R.id.tick))
						.setVisibility(View.VISIBLE);
			}

			return rowView;
		}
	}

}
