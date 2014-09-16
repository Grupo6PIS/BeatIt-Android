package com.g6pis.beatit;

import java.util.List;

import com.g6pis.beatit.datatypes.DTRanking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdaptadorRanking extends ArrayAdapter<DTRanking> {
  private final Context context;
  private final List rs;

  public AdaptadorRanking(Context context, List rs) {
    super(context, R.layout.ranking_row);
    this.context = context;
    this.rs = rs;
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
    
    // Agrego los datos a los elementos
    DTRanking dtr = (DTRanking) rs.get(position);
    //imageFoto.setImageResource();
    posicion.setText(dtr.getPosicion());
    nombreUsuario.setText(dtr.getNombreUsuario());
    puntaje.setText(dtr.getPuntaje());

    return rowView;
  }
} 