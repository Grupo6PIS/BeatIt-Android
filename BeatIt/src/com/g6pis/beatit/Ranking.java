//package com.g6pis.beatit;
//
//import java.util.ArrayList;
//import java.util.List;
//
////import com.g6pis.beatit.datatypes.DTRanking;
//
//
//import com.g6pis.beatit.datatypes.DTRanking;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.support.v4.app.NavUtils;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.app.ListActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.Toast;
//
//
//public class Ranking extends ListActivity {
//  public void onCreate(Bundle icicle) {
//    super.onCreate(icicle);
//    setContentView(R.layout.ranking);
//
//    List rankings = new ArrayList();
//    
//    rankings.add(new DTRanking("Felipe", 2000, 1, "imagen"));
//    rankings.add(new DTRanking("Alejandro", 10, 2, "imagen"));
//    rankings.add(new DTRanking("Luciana", 5, 3, "imagen"));
//    rankings.add(new DTRanking("Martin", 2, 4, "imagen"));
//
//    setListAdapter(new AdaptadorRanking(this, rankings));
//  }
//
//
//  @Override
//  protected void onListItemClick(ListView l, View v, int position, long id) {
//    String item = (String) getListAdapter().getItem(position);
//    Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
//  }
//} 
