package com.g6pis.beatitapp.tabs;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.g6pis.beatitapp.Home;
import com.g6pis.beatitapp.R;
import com.g6pis.beatitapp.connection.ImageLoadTask;
import com.g6pis.beatitapp.controllers.DataManager;
import com.g6pis.beatitapp.datatypes.DTRanking;
import com.g6pis.beatitapp.interfaces.Factory;
//import com.g6pis.beatit.AdaptadorRanking;

 
public class RankingTab extends Fragment implements OnItemClickListener, OnClickListener, OnRefreshListener, OnScrollListener  {
	
	private ListView ranking;
    private MyAdapter adapter;
    private Home activity;
    
    private SwipeRefreshLayout swipeLayout;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (Home) getActivity();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ranking_tab, container, false);
        
        
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(R.color.green, 
        		R.color.red, 
        		R.color.blue, 
        		R.color.yellow);
        
        
        ranking = (ListView) rootView.findViewById(R.id.ranking_list);
		ranking.setOnItemClickListener(this);
		ranking.setOnScrollListener(this);
		
		DataManager dm = (DataManager) Factory.getInstance().getIDataManager();
        List <DTRanking> rankings = dm.getRanking();
        int total_rankings = rankings.size();
		DTRanking[] items=new DTRanking[total_rankings];
		

		for(int index=0;index<total_rankings;index++){
            items[index] = rankings.get(index);
        }
		
		adapter = new MyAdapter(getActivity().getApplicationContext(),items);
        ranking.setAdapter(adapter);
		
        activity.refreshButton.setOnClickListener(this);
        
        
        return rootView;
    }
    

    @Override
   	public void onItemClick(AdapterView<?> parent, View view, int position,
   			long id) {
    	String item = adapter.getItem(position).getUserName();
    	
    	String username = Factory.getInstance().getIDataManager().getUser()
  				.getFirstName()
  				+ " "
  				+ Factory.getInstance().getIDataManager().getUser().getLastName();
    	
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
          TextView position = (TextView) rowView.findViewById(R.id.textView_position_ranking);
          TextView userName = (TextView) rowView.findViewById(R.id.textView_username_ranking);
          TextView score = (TextView) rowView.findViewById(R.id.textView_score_ranking);
          ImageView profilePicture = (ImageView) rowView.findViewById(R.id.imageView_ranking_photo);
          
          DTRanking ranking = objects[location];
          
          position.setText(Integer.toString(ranking.getPosition()));
          userName.setText(ranking.getUserName());
          score.setText(Integer.toString(ranking.getScore()));
          new ImageLoadTask(ranking.getImageURL(), profilePicture).execute(null, null);
          String username = Factory.getInstance().getIDataManager().getUser()
  				.getFirstName()
  				+ " "
  				+ Factory.getInstance().getIDataManager().getUser().getLastName();
          if(ranking.getUserName().equals(username)){
        	  user.setVisibility(View.VISIBLE);
        	  rowView.setBackgroundColor(getResources().getColor(R.color.blanco));
        	  
          }else{
        	  user.setVisibility(View.INVISIBLE);
        	  rowView.setBackgroundColor(getResources().getColor(R.color.gris));
          }

          return rowView;
        }
    }

	@Override
	public void onClick(View v) {
		
//		DataManager dm = (DataManager) Factory.getInstance().getIDataManager();
//		dm.updateRanking();
//		List <DTRanking> rankings = dm.getRanking();
//        int total_rankings = rankings.size();
//		DTRanking[] items=new DTRanking[total_rankings]; 
//		
//
//		for(int index=0;index<total_rankings;index++){
//            items[index] = rankings.get(index);
//        }
//		
//		adapter = new MyAdapter(getActivity().getApplicationContext(),items);
//        ranking.setAdapter(adapter);
		
		onRefresh();
		
	}

	@Override
	public void onRefresh() {
		swipeLayout.setRefreshing(true);
        Log.d("Swipe", "Refreshing Number");
        ( new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
            	DataManager dm = (DataManager) Factory.getInstance().getIDataManager();
    			dm.updateRanking();
    			List <DTRanking> rankings = dm.getRanking();
    	        int total_rankings = rankings.size();
    			DTRanking[] items=new DTRanking[total_rankings]; 
    			

    			for(int index=0;index<total_rankings;index++){
    	            items[index] = rankings.get(index);
    	        }
    			adapter = new MyAdapter(getActivity().getApplicationContext(),items);
    			ranking.setAdapter(adapter);
    	        swipeLayout.setRefreshing(false);
            }
        }, 3000);

	        

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (firstVisibleItem == 0)
            swipeLayout.setEnabled(true);
        else
            swipeLayout.setEnabled(false);
		
	}
}