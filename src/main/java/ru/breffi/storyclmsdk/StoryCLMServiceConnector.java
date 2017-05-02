package ru.breffi.storyclmsdk;

import java.lang.reflect.Type;

import com.google.gson.Gson;

public class StoryCLMServiceConnector {
	final private IStoryCLMService _storyCLMService;
	final private Gson gson;
	public StoryCLMServiceConnector(IStoryCLMService storyCLMService, Gson gson)
	{
		_storyCLMService = storyCLMService;
		this.gson  = gson;
	}
	
	public <T> StoryCLMServiceGeneric<T> GetService(Type entityType, int tableId){
		return new StoryCLMServiceGeneric<>(entityType, _storyCLMService, gson, tableId);
	}

	
}
