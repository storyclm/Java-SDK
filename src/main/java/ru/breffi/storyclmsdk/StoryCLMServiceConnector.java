package ru.breffi.storyclmsdk;

import java.lang.reflect.Type;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import ru.breffi.storyclmsdk.AsyncResults.IAsyncResult;
import ru.breffi.storyclmsdk.AsyncResults.ProxyCallResult;
import ru.breffi.storyclmsdk.Models.ApiTable;

public class StoryCLMServiceConnector {
	final private IStoryCLMTableServiceRetrofit _storyCLMService;
	final private StoryCLMContentService contentService;
	final private Gson gson;
	public StoryCLMServiceConnector(Retrofit retorfit, Gson gson)
	{
		_storyCLMService = retorfit.create(IStoryCLMTableServiceRetrofit.class);
		contentService = new StoryCLMContentService(retorfit.create(IStoryCLMContentServiceRetrofit.class));
		this.gson  = gson;
	} 
	
	public <T> StoryCLMTableService<T> GetTableService(Type entityType, int tableId){
		return new StoryCLMTableService<>(entityType, _storyCLMService, gson, tableId);
	}

	public StoryCLMContentService GetContentService(){
		return contentService;
	}
	
	public IAsyncResult<ApiTable[]> GetTables(int clientId){
		return new ProxyCallResult<>(_storyCLMService.GetTables(clientId));
	}
	
}
