package com.g6pis.beatit.controllers;

import java.util.List;

import com.g6pis.beatit.datatypes.DTChallenge;
import com.g6pis.beatit.datatypes.DTDateTime;
import com.g6pis.beatit.entities.Challenge;
import com.g6pis.beatit.interfaces.IChallengeController;



public class ChallengeController implements IChallengeController {

	@Override
	public List<Challenge> listarDesafiosDisponibles(DTDateTime date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Challenge> listarDesafiosIniciados(DTDateTime date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void iniciarDesafio(int challengId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelarDeafio(int challengeId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void completarDesafio(int challengeId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DTChallenge verDesafioFinalizado(int challengeId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	

}
