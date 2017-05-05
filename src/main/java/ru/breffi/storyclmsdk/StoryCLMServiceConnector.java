package ru.breffi.storyclmsdk;

import java.lang.reflect.Type;

import com.google.gson.Gson;

import ru.breffi.storyclmsdk.AsyncResults.IAsyncResult;
import ru.breffi.storyclmsdk.AsyncResults.ProxyCallResult;
import ru.breffi.storyclmsdk.Models.ApiTable;

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

	
	public IAsyncResult<ApiTable[]> GetTables(int clientId){
		return new ProxyCallResult<>(_storyCLMService.GetTables(clientId));
	}
	
}
