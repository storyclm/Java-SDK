package ru.breffi.storyclmsdk.connectors;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.function.Supplier;

import com.google.gson.GsonBuilder;

import retrofit2.Call;
import ru.breffi.storyclmsdk.StoryCLMContentService;
import ru.breffi.storyclmsdk.StoryCLMTableService;
import ru.breffi.storyclmsdk.StoryCLMUserService;
import ru.breffi.storyclmsdk.AsyncResults.IAsyncResult;
import ru.breffi.storyclmsdk.AsyncResults.LinkedChainCallResult;
import ru.breffi.storyclmsdk.AsyncResults.ProxyCallResult;
import ru.breffi.storyclmsdk.AsyncResults.SequanceCallResult;
import ru.breffi.storyclmsdk.Calls.ProxyConvertCall;
import ru.breffi.storyclmsdk.Calls.SyncCall;
import ru.breffi.storyclmsdk.Exceptions.TableNotFoundException;
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
	
	public <T> IAsyncResult<StoryCLMTableService<T>> GetTableService(Type entityType, String tableName){
		return LinkedChainCallResult
				.AtFirst(new ProxyCallResult<>(getClientId()))
				.Then(clientid->new ProxyCallResult<>(_storyCLMService.GetTables(clientid)))
				.<StoryCLMTableService<T>>ThenResult(tables-> Arrays.stream(tables)
						.filter(t -> t.name.equals(tableName))
				        .findAny()
				       //.map(a->GetTableService<T>(entityType, a.id))
				         .map(a->new StoryCLMTableService<T>(entityType, _storyCLMService, getGson(), a.id))
				        .orElse(null));
				        
							
							
							
		
	}
	 
	public IAsyncResult<ApiTable[]> GetTables() {	
		return LinkedChainCallResult
		.AtFirst(new ProxyCallResult<>(getClientId()))
		.Then(clientid->new ProxyCallResult<>(_storyCLMService.GetTables(clientid)));
				
		
	}
	
	        
}
