package com.g6pis.beatit.challenges.despertameatiempo;

import com.g6pis.beatit.entities.Challenge;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")

public class WakeMeUp extends Challenge {

	private static final int SHAKE_THRESHOLD = 600;

	private float last_x, last_y, last_z; // Almacena la posición en los ejes X, Y, Z
	private long lastUpdate; // Almacenará el momento en el que se actualizaron los valores de posición	
	private long tolerancia; // En milisegundos
	private long segs_ocultos; // Este valor se puede modificar para cambiar la dificultad
	private long tope; // Es el límite para detener el contador automáticamente porque ya perdió por mucho	
	private long time; // Cuenta regresiva real
	private long g_millis; // Cuenta regresiva ficticia porque incluye el valor del tope
	private long valor_inicial_contador; // En milisegundos
	private long veces_exito = 3;
	
	Button btnStart, btnStop;
	TextView textViewTime;
	TextView textViewDescription;
	TextView textViewResult;
		
	public WakeMeUp(String challengeId, String name, Integer level, int maxAttempt) {
		super(challengeId, name,level, maxAttempt);

		this.lastUpdate = 0;
		this.tolerancia = 500;
		this.segs_ocultos = 3;
		this.tope = 10000;	
		this.time = 0;
		this.g_millis = 0;
		this.valor_inicial_contador = 10000;
			
	}
	
	
	//@Override
	public double calculateScore(){
		return (veces_exito)*20;
	}
		
}
