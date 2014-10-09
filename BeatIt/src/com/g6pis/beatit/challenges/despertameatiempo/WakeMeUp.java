package com.g6pis.beatit.challenges.despertameatiempo;

import com.g6pis.beatit.controllers.DataManager;
import com.g6pis.beatit.entities.Challenge;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")

public class WakeMeUp extends Challenge {
	private static final String CHALLENGE_ID = "2";
	
	private static final int SHAKE_THRESHOLD = 300;
	
	private Integer cant_repeticiones;
	private static final Integer CANT_REPETICIONES_LEVEL1 = 3;
	private static final Integer CANT_REPETICIONES_LEVEL2 = 4;
	private static final long TIME_LEVEL1_1 = 5;
	private static final long TIME_LEVEL1_2 = 4;
	private static final long TIME_LEVEL1_3 = 3;
	private static final long TIME_LEVEL2_1 = 9;
	private static final long TIME_LEVEL2_2 = 7;
	private static final long TIME_LEVEL2_3 = 5;
	private static final long TIME_LEVEL2_4 = 3;

/*
	private static final long TOLERANCIA = 500; // En milisegundos
	private static final long TOPE = 10000; // Es el límite para detener el contador automáticamente porque ya perdió por mucho
	private float last_x, last_y, last_z; // Almacena la posición en los ejes X, Y, Z
	private long lastUpdate; // Almacenará el momento en el que se actualizaron los valores de posición		
	private long time; // Cuenta regresiva real
	private long g_millis; // Cuenta regresiva ficticia porque incluye el valor del tope
	private long valor_inicial_contador; // En milisegundos
*/
	
	private long tolerancia; // En milisegundos
	private long tope; // Es el límite para detener el contador automáticamente porque ya perdió por mucho
	private long segs_ocultos; // Este valor se puede modificar para cambiar la dificultad
	private long veces_exito;
	
/*
	Button btnStart, btnStop;
	TextView textViewTime;
	TextView textViewDescription;
	TextView textViewResult;
*/
		
	public WakeMeUp(String challengeId, String name, Integer level, int maxAttempt) {
		super(challengeId, name,level, maxAttempt);

		this.segs_ocultos = 0;
		this.veces_exito = 0;
		
/*		switch (level) {
			case 1: {
				segs_ocultos = TIME_LEVEL1_3;
				cant_repeticiones = CANT_REPETICIONES_LEVEL1;
			}
				break;
			case 2: {
				segs_ocultos = TIME_LEVEL2_4;
				cant_repeticiones = CANT_REPETICIONES_LEVEL2;
			}
				break;	
		}
*/
			
	}
	
	public long getSegs_ocultos() {
		return segs_ocultos;
	}


	public void setSegs_ocultos(long segs_ocultos) {
		this.segs_ocultos = segs_ocultos;
	}


	public long getVeces_exito() {
		return veces_exito;
	}


	public void setVeces_exito(long veces_exito) {
		this.veces_exito = veces_exito;
	}

	public void finishChallenge(){
		DataManager.getInstance().saveScore(CHALLENGE_ID, calculateScore());
		
	}

	//@Override
	public double calculateScore(){
		return (veces_exito)*20;
	}
		
}
