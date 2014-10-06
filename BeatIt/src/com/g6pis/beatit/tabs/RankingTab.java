package com.g6pis.beatit.tabs;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.g6pis.beatit.Home;
//import com.g6pis.beatit.AdaptadorRanking;
import com.g6pis.beatit.R;
import com.g6pis.beatit.controllers.DataManager;
import com.g6pis.beatit.datatypes.DTRanking;
import com.g6pis.beatit.datatypes.DTState;

 
public class RankingTab extends Fragment implements OnItemClickListener  {
	
	private ListView ranking;
    private MyAdapter adapter;
    private Home activity;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (Home) getActivity();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ranking_tab, container, false);
        
        ranking = (ListView) rootView.findViewById(R.id.Ranking_list);
		ranking.setOnItemClickListener(this);
		
		DataManager dm = (DataManager) DataManager.getInstance();
        List <DTRanking> rankings = dm.getRanking();
        int total_rankings = rankings.size();
		DTRanking[] items=new DTRanking[total_rankings];
		

		for(int index=0;index<total_rankings;index++){
            items[index] = rankings.get(index);
        }
		
		adapter = new MyAdapter(getActivity().getApplicationContext(),items);
        ranking.setAdapter(adapter);
		
		
		/*
        List<DTRanking> rankings = new ArrayList<DTRanking>();
        
        rankings.add(new DTRanking("Felipe Garc�a", 2000, 1, "imagen"));
        rankings.add(new DTRanking("Juan P�rez", 1500, 2, "image"));
        rankings.add(new DTRanking("Alejandro Brusco", 1000, 3, "imagen"));
        rankings.add(new DTRanking("Luciana Mart�nez", 500, 4, "imagen"));
        rankings.add(new DTRanking("Mart�n Steglich", 402, 5, "imagen"));
        rankings.add(new DTRanking("Mart�n Berguer", 302, 6, "imagen"));
        rankings.add(new DTRanking("Mart�n Alay�n", 122, 7, "imagen"));
        rankings.add(new DTRanking("Gonzalo Javiel", 102, 8, "imagen"));
        rankings.add(new DTRanking("Pablo Olivera", 92, 9, "imagen"));
        rankings.add(new DTRanking("Cristian Bauza", 82, 10, "imagen"));
        rankings.add(new DTRanking("Emiliano Vazquez", 80, 11, "imagen"));
        rankings.add(new DTRanking("Raul Speroni", 60, 12, "imagen"));

        //setListAdapter(new AdaptadorRanking(this, rankings));
        
        adapter = new MyAdapter(getActivity().getApplicationContext(),rankings);
        ranking.setAdapter(adapter);
        */
        
        return rootView;
    }
    

    @Override
   	public void onItemClick(AdapterView<?> parent, View view, int position,
   			long id) {
    	String item = adapter.getItem(position).getUserName();
    	
    	String username = activity.datamanager.getInstance().getUser()
  				.getFirstName()
  				+ " "
  				+ activity.datamanager.getInstance().getUser().getLastName();
    	
    	if(item.equals(username)){
    		((Home)this.getActivity()).goToProfileFragment();
    		
    	}
    	
   		
   	}
    
    
    private class MyAdapter extends ArrayAdapter<DTRanking>{
		private final Context context;
		private final DTRanking[] objects;

        public MyAdapter(Context context, DTRanking[] objects) {
            super(context, R.layout.ranking_row, objects);
            this.context = context;
            this.objects = objects;   
        }
        
        @Override
        public View getView(int location, View convertView, ViewGroup parent) {
          LayoutInflater inflater = (LayoutInflater) context
              .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          View rowView = inflater.inflate(R.layout.ranking_row,parent,false);

          LinearLayout user = (LinearLayout)rowView.findViewById(R.id.linear_indicador_soyyo);
          TextView position = (TextView) rowView.findViewById(R.id.textView_posicion);
          TextView userName = (TextView) rowView.findViewById(R.id.textView_nombre_usuario);
          TextView score = (TextView) rowView.findViewById(R.id.textView_puntaje);
          ProfilePictureView profilePictureView = (ProfilePictureView) rowView.findViewById(R.id.imageView_foto_ranking);
          profilePictureView.setCropped(true);
          
          DTRanking ranking = objects[location];
          
          position.setText(Integer.toString(ranking.getPosition()));
          userName.setText(ranking.getUserName());
          score.setText(Integer.toString(ranking.getScore()));
          String username = activity.datamanager.getInstance().getUser()
  				.getFirstName()
  				+ " "
  				+ activity.datamanager.getInstance().getUser().getLastName();
          if(ranking.getUserName().equals(username)){
        	  user.setVisibility(View.VISIBLE);
        	  rowView.setBackgroundColor(getResources().getColor(R.color.blanco));
        	  profilePictureView.setProfileId(activity.datamanager.getInstance()
      				.getUser().getFbId()); 
        	  
          }else{
        	  user.setVisibility(View.INVISIBLE);
        	  rowView.setBackgroundColor(getResources().getColor(R.color.gris));
          }

          return rowView;
        }
    }
}