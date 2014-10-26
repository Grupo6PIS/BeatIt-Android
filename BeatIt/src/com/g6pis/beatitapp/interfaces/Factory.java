package com.g6pis.beatitapp.interfaces;

import com.g6pis.beatitapp.controllers.DataManager;

public class Factory {
	
	private Factory() {
    }
    
    public static Factory getInstance() {
        return FactoryHolder.INSTANCE;
    }
    
    private static class FactoryHolder {

        private static final Factory INSTANCE = new Factory();
    }
    
    public IDataManager getIDataManager(){
        IDataManager dataManager = DataManager.getInstance();
        return dataManager;
    }
}
