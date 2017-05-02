package ru.breffi.storyclmsdk;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.breffi.storyclmsdk.OAuth.AccessTokenManager;
import ru.breffi.storyclmsdk.OAuth.OAuthAuthenticator;
import ru.breffi.storyclmsdk.OAuth.OAuthInterceptor;
import ru.breffi.storyclmsdk.TypeAdapters.MapDeserializerDoubleAsIntFix;

public class StoryCLMConnectorsGenerator {
	 	public static final String API_BASE_URL = "https://api.storyclm.com/v1/";

	 	protected static final String API_OAUTH_REDIRECT = null;

	    private static Retrofit.Builder _builder;
    	private static Gson _gson;
    	private static Map<String,StoryCLMServiceConnector> _storyCLMServiceConnectorsMap = new HashMap<String, StoryCLMServiceConnector>();

    	
    	/**
    	 * Получение коннектора для данного клиента (client_id).
    	 * Возможно использование нескольких клиентов одном процессе.
    	 * 
    	 * @param client_id - идентификатор клиента
    	 * @param client_secret - секретный ключ клиента
    	 * @param gBuilder - билдер для настройки Gson конвертера (null - если настройка не требуется)
    	 * @return Коннектор используется для создания конкретных типизированных сервисов для работы с конкретными таблицами клиента.
    	 * @throws IOException
    	 */
    	public static StoryCLMServiceConnector GetStoryCLMServiceConnector(String client_id, String client_secret,GsonBuilder gBuilder) {
		/*
		 * Коннекторы хранятся в словаре по client_id.
		 * Для каждого клиента (client_id) создается 
		 * AccessTokenManager - для управления токенами (получение, обновление)
		 * OkHttpClient - осуществляющий аутентификацию с использнованием указанного AccessTokenManager
		 * IStoryCLMService - использующий указанный OkHttpClient.Builder для транспорта
    	 */
    		StoryCLMServiceConnector result = _storyCLMServiceConnectorsMap.getOrDefault(client_id, null);
    		if (result == null)
    		{ 
    			AccessTokenManager accessTokenManager = new AccessTokenManager(client_id, client_secret); 
    			OkHttpClient okHttpClient = getHttpClient(accessTokenManager);
    		
    			Retrofit retrofit = getbuilder().client(okHttpClient).build();
	            _storyCLMServiceConnectorsMap
	            .put(client_id,  result = new StoryCLMServiceConnector(retrofit.create(IStoryCLMService.class),(gBuilder==null)?getGson():getGson(gBuilder)));
    		}
    		return result;
    	
    	}
    	
    	private static OkHttpClient getHttpClient(AccessTokenManager accessTokenManager ){
    		OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.addInterceptor(new OAuthInterceptor(accessTokenManager));
            httpClientBuilder.authenticator(new OAuthAuthenticator(accessTokenManager));
            httpClientBuilder.readTimeout(60, TimeUnit.SECONDS);
            httpClientBuilder.connectTimeout(60, TimeUnit.SECONDS);
            return httpClientBuilder.build();
    	}
    	
    	private static Gson getGson(){
    		if (_gson ==null)	
    			{_gson = getGson(new GsonBuilder());}
    		return _gson;
    	}
    	private static Gson getGson(GsonBuilder gsonBuilder){
    	    	gsonBuilder.registerTypeAdapter(new TypeToken<Map<String, Object>>(){}.getType(),  new MapDeserializerDoubleAsIntFix());
    	    	return gsonBuilder.serializeNulls().create();
    	
    	}
    	
	    private static Retrofit.Builder getbuilder(){
	    	if (_builder==null)  {
	    	_builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(getGson()));
	    	}
	    	return _builder;
	    }
	        
	
	    public static boolean isEmpty(CharSequence str) {
	    	         if (str == null || str.length() == 0)
	    	            return true;
	    	         else
	    	             return false;
	    	     }

}
