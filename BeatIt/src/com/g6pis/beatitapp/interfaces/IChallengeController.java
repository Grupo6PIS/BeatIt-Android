package com.g6pis.beatitapp.interfaces;

import java.util.List;

import com.g6pis.beatitapp.datatypes.DTChallenge;
import com.g6pis.beatitapp.datatypes.DTDateTime;
import com.g6pis.beatitapp.entities.Challenge;

public interface IChallengeController {
	
	public abstract List<Challenge> listarDesafiosDisponibles(DTDateTime date);
	public abstract List<Challenge> listarDesafiosIniciados(DTDateTime date);
	public abstract void iniciarDesafio(int challengeId);
	public abstract void cancelarDeafio(int challengId);
	public abstract void completarDesafio(int challengId);
	public DTChallenge verDesafioFinalizado(int challengeId);
	
}
