package com.g6pis.beatit.tabs;

import java.util.ArrayList;
import java.util.List;







import com.facebook.widget.ProfilePictureView;
//import com.g6pis.beatit.AdaptadorRanking;
import com.g6pis.beatit.R;
import com.g6pis.beatit.datatypes.DTRanking;
import com.g6pis.beatit.Home;











import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Fragment;
import android.content.Context;

 
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
		
        List<DTRanking> rankings = new ArrayList<DTRanking>();
        
        rankings.add(new DTRanking("Felipe García", 2000, 1, "imagen"));
        rankings.add(new DTRanking("Juan Pérez", 1500, 2, "image"));
        rankings.add(new DTRanking("Alejandro Brusco", 1000, 3, "imagen"));
        rankings.add(new DTRanking("Luciana Martínez", 500, 4, "imagen"));
        rankings.add(new DTRanking("Martín Steglich", 402, 5, "imagen"));
        rankings.add(new DTRanking("Martín Berguer", 302, 6, "imagen"));
        rankings.add(new DTRanking("Martín Alayón", 122, 7, "imagen"));
        rankings.add(new DTRanking("Gonzalo Javiel", 102, 8, "imagen"));
        rankings.add(new DTRanking("Pablo Olivera", 92, 9, "imagen"));
        rankings.add(new DTRanking("Cristian Bauza", 82, 10, "imagen"));
        

        //setListAdapter(new AdaptadorRanking(this, rankings));
        
        adapter = new MyAdapter(getActivity().getApplicationContext(),rankings);
        ranking.setAdapter(adapter);
        
        return rootView;
    }
    

    @Override
   	public void onItemClick(AdapterView<?> parent, View view, int position,
   			long id) {
    	String item = adapter.getItem(position).getNombreUsuario();
    	
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
		private final List<DTRanking> objects;

        public MyAdapter(Context context, List<DTRanking> objects) {
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
          
          DTRanking ranking = objects.get(location);
          
          position.setText(Integer.toString(ranking.getPosicion()));
          userName.setText(ranking.getNombreUsuario());
          score.setText(Integer.toString(ranking.getPuntaje()));
          String username = activity.datamanager.getInstance().getUser()
  				.getFirstName()
  				+ " "
  				+ activity.datamanager.getInstance().getUser().getLastName();
          if(ranking.getNombreUsuario().equals(username)){
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