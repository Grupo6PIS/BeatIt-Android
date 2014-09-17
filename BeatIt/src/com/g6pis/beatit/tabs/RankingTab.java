package com.g6pis.beatit.tabs;

import java.util.ArrayList;
import java.util.List;



//import com.g6pis.beatit.AdaptadorRanking;
import com.g6pis.beatit.R;

import com.g6pis.beatit.datatypes.DTRanking;







import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Fragment;
import android.content.Context;

 
public class RankingTab extends Fragment implements OnItemClickListener  {
	
	private ListView ranking;
    private MyAdapter adapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ranking_tab, container, false);
        
        ranking = (ListView) rootView.findViewById(R.id.Ranking_list);
		ranking.setOnItemClickListener(this);
		
        List<DTRanking> rankings = new ArrayList<DTRanking>();
        
        rankings.add(new DTRanking("Felipe", 2000, 1, "imagen"));
        rankings.add(new DTRanking("Alejandro", 10, 2, "imagen"));
        rankings.add(new DTRanking("Luciana", 5, 3, "imagen"));
        rankings.add(new DTRanking("Martin", 2, 4, "imagen"));

        //setListAdapter(new AdaptadorRanking(this, rankings));
        
        adapter = new MyAdapter(getActivity().getApplicationContext(),rankings);
        ranking.setAdapter(adapter);
        
        return rootView;
    }
    

    @Override
   	public void onItemClick(AdapterView<?> parent, View view, int position,
   			long id) {
    	String item = adapter.getItem(position).toString();
    	Toast.makeText(getActivity().getApplicationContext(), item + " selected", Toast.LENGTH_LONG).show();

   		
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
          //View rowView = inflater.inflate(R.layout.ranking_row, parent, false);
          
          TextView position = (TextView) rowView.findViewById(R.id.textView_posicion);
          TextView userName = (TextView) rowView.findViewById(R.id.textView_nombre_usuario);
          TextView score = (TextView) rowView.findViewById(R.id.textView_puntaje);
          
          DTRanking ranking = objects.get(location);
          
          position.setText(Integer.toString(ranking.getPosicion()));
          userName.setText(ranking.getNombreUsuario());
          score.setText(Integer.toString(ranking.getPuntaje()));

          return rowView;
        }
    }
}