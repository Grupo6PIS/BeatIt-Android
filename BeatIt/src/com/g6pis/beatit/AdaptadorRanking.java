/*package com.g6pis.beatit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdaptadorRanking extends ArrayAdapter<String> {
  private final Context context;
  private final String[] posiciones;

  public AdaptadorRanking(Context context, String[] posiciones, String[] nombresDeUsuario, String[] puntajes) {
    super(context, R.layout.ranking_row, posiciones);
    this.context = context;
    this.posiciones = posiciones;
  }

  @SuppressLint("ViewHolder") @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    
	  LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
    View rowView = inflater.inflate(R.layout.ranking_row, parent, false);
    
    LinearLayout l = (LinearLayout) rowView.findViewById(R.id.linear_indicador_soyyo);
    ImageView imageFoto = (ImageView) rowView.findViewById(R.id.imageView_foto_ranking);
    TextView posicion = (TextView) rowView.findViewById(R.id.textView_posicion);
    TextView nombreUsuario = (TextView) rowView.findViewById(R.id.textView_nombre_usuario);
    TextView puntaje = (TextView) rowView.findViewById(R.id.textView_puntaje);
    
    posicion.setText(posiciones[position]);
    
    // Change the icon for Windows and iPhone
    String s = posiciones[position];
    if (s.startsWith("Windows7") || s.startsWith("iPhone")
        || s.startsWith("Solaris")) {
      imageFoto.setImageResource(R.drawable.ic_launcher);
    } else {
      imageFoto.setImageResource(R.drawable.ic_usain_bolt);
    }

    return rowView;
  }

@Override
public int getCount() {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public Object getItem(int position) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public long getItemId(int position) {
	// TODO Auto-generated method stub
	return 0;
}
} */