package com.g6pis.beatit.tabs;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.g6pis.beatit.Home;
//import com.g6pis.beatit.AdaptadorRanking;
import com.g6pis.beatit.R;
import com.g6pis.beatit.connection.ImageLoadTask;
import com.g6pis.beatit.controllers.DataManager;
import com.g6pis.beatit.datatypes.DTRanking;

 
public class RankingTab extends Fragment implements OnItemClickListener, OnClickListener  {
	
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
		
        activity.refreshButton.setVisibility(View.VISIBLE);
        activity.refreshButton.setOnClickListener(this);
        
        
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
          ImageView profilePicture = (ImageView) rowView.findViewById(R.id.imageView_foto_ranking);
          
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
        	  new ImageLoadTask(activity.datamanager.getInstance().getUser()
      				.getImageURL(), profilePicture).execute(null, null);
        	  
          }else{
        	  user.setVisibility(View.INVISIBLE);
        	  rowView.setBackgroundColor(getResources().getColor(R.color.gris));
          }

          return rowView;
        }
    }


	@Override
	public void onClick(View v) {
		
		DataManager dm = (DataManager) DataManager.getInstance();
		List <DTRanking> rankings = dm.updateRanking();
        int total_rankings = rankings.size();
		DTRanking[] items=new DTRanking[total_rankings];
		

		for(int index=0;index<total_rankings;index++){
            items[index] = rankings.get(index);
        }
		
		adapter = new MyAdapter(getActivity().getApplicationContext(),items);
        ranking.setAdapter(adapter);
		
	}
}