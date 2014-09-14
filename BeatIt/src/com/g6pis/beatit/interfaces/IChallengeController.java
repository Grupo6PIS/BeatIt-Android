package com.g6pis.beatit.interfaces;

import java.util.List;

import com.g6pis.beatit.datatypes.DTChallenge;
import com.g6pis.beatit.datatypes.DTDateTime;
import com.g6pis.beatit.entities.Challenge;

public interface IChallengeController {
	
	public abstract List<Challenge> listarDesafiosDisponibles(DTDateTime date);
	public abstract List<Challenge> listarDesafiosIniciados(DTDateTime date);
	public abstract void iniciarDesafio(int challengeId);
	public abstract void cancelarDeafio(int challengId);
	public abstract void completarDesafio(int challengId);
	public DTChallenge verDesafioFinalizado(int challengeId);
	
}
