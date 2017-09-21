package ru.breffi.storyclmsdk.connectors;

import java.lang.reflect.Type;

import com.google.gson.GsonBuilder;

import ru.breffi.storyclmsdk.StoryCLMContentService;
import ru.breffi.storyclmsdk.StoryCLMTableService;
import ru.breffi.storyclmsdk.StoryCLMUserService;
import ru.breffi.storyclmsdk.AsyncResults.IAsyncResult;
import ru.breffi.storyclmsdk.AsyncResults.ProxyCallResult;
import ru.breffi.storyclmsdk.Models.ApiTable;
import ru.breffi.storyclmsdk.OAuth.AccessTokenManager;
import ru.breffi.storyclmsdk.retrofitservices.IStoryCLMContentServiceRetrofit;
import ru.breffi.storyclmsdk.retrofitservices.IStoryCLMTableServiceRetrofit;
import ru.breffi.storyclmsdk.retrofitservices.IStoryCLMUserServiceRetrofit;

/**
 * Коннектор предоставляющий сервисы-обертки над сервисами Retrofit2.
 * Методы сервисов возвращают интерфейс IAsyncResult, предоставляющий интерфейс асинхронного вызова методов доступа. 
 * @author tselo
 *
 */
public class StoryCLMServiceConnector extends BaseConnector{
	final private IStoryCLMTableServiceRetrofit _storyCLMService;
	final private StoryCLMContentService contentService;
	final private StoryCLMUserService userService;

	public StoryCLMServiceConnector(AccessTokenManager accessTokenManager, GsonBuilder gBuilder) 
	{
		super(accessTokenManager,gBuilder);
		_storyCLMService = getStoryCLMRetrofit().create(IStoryCLMTableServiceRetrofit.class);
		contentService = new StoryCLMContentService( getStoryCLMRetrofit().create(IStoryCLMContentServiceRetrofit.class));
		userService = new StoryCLMUserService(getStoryCLMRetrofit().create(IStoryCLMUserServiceRetrofit.class));
	} 
	
	public <T> StoryCLMTableService<T> GetTableService(Type entityType, int tableId){
		return new StoryCLMTableService<T>(entityType, _storyCLMService, getGson(), tableId);
	}

	public StoryCLMContentService GetContentService(){
		return contentService;
	}
	
	public StoryCLMUserService GetUserService(){
		return userService;
	}
	
	public IAsyncResult<ApiTable[]> GetTables(int clientId){
		return new ProxyCallResult<>(_storyCLMService.GetTables(clientId));
	}
	 
	
	
	        
}
