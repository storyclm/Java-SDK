package ru.breffi.storyclmsdk.connectors;

import java.lang.reflect.Type;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import ru.breffi.storyclmsdk.StoryCLMTableServiceRetrofitProxy;
import ru.breffi.storyclmsdk.OAuth.AccessTokenManager;
import ru.breffi.storyclmsdk.retrofitservices.IStoryCLMContentServiceRetrofit;
import ru.breffi.storyclmsdk.retrofitservices.IStoryCLMTableServiceRetrofit;
import ru.breffi.storyclmsdk.retrofitservices.IStoryCLMUserServiceRetrofit;

/**
 * Коннектор, предоставляющий сервисы работы с ресурсами StoryCLM, совместимые с сервисами Retrofit2
 * Методы сервисов возвращают интерфейс retrofit2.Call, предоставляющий функциональность асинхронного вызова.
 * Коннектор позволяет подключаться к другим сервисам, использующим OAuth аутентификацию StoryCLM (см. метод createResourceService)
 * @author tselo
 *
 */
public class RetrofitConnector extends BaseConnector {
	 
	 Retrofit.Builder retrofitBuilder;
	 Retrofit getResourceRetorfit(String resourceBaseUrl){
		 return retrofitBuilder.baseUrl(resourceBaseUrl).build();
	 }
	 
	
	 
	 public RetrofitConnector(AccessTokenManager accessTokenManager, GsonBuilder gbuilder, String apiUrl)
	{
		super(accessTokenManager, gbuilder, apiUrl);
	} 
	 IStoryCLMTableServiceRetrofit _storyCLMService;
	 IStoryCLMTableServiceRetrofit getStoryCLMService()
	 {
		 return _storyCLMService==null?_storyCLMService=getStoryCLMRetrofit().create(IStoryCLMTableServiceRetrofit.class):_storyCLMService;
	 }
	 
	 //------------------------------ public --------------------------
	 
	 public <T> StoryCLMTableServiceRetrofitProxy<T> createStoryCLMTableService(Type entityType, int tableId){
		 return new StoryCLMTableServiceRetrofitProxy<T>(entityType, getStoryCLMService(), getGson(), tableId);
	 }
	 
	 public IStoryCLMContentServiceRetrofit createStoryCLMContentService(){
		 return getStoryCLMRetrofit().create(IStoryCLMContentServiceRetrofit.class); 
	 }
	
	 public IStoryCLMUserServiceRetrofit createStoryCLMUserService(){
		 return getStoryCLMRetrofit().create(IStoryCLMUserServiceRetrofit.class); 
	 }
	
	 /**
	  * Возвращает retrofit2 сервис для доступа к ресурсам исользующим аутентификацию StoryCLM
	  * @param service 
	  * интерфейс-сервис, построенный по правилам retrofit2 
	  * @param resourceUrl
	  * адрес ресурса
	  * @return
	  */
	 public <T> T createResourceService(final Class<T> service, String resourceUrl) {
		 return getResourceRetorfit(resourceUrl).create(service);
	 }
	 
	 
	 
}
