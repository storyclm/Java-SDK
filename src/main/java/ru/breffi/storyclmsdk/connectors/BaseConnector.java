package ru.breffi.storyclmsdk.connectors;

import java.util.Date;
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
import ru.breffi.storyclmsdk.TypeAdapters.StoryDateTypeAdapter;
/**
 * Базовый коннектор, предоставляющий функциональность аутентицикации/авторизации, а также информацию о ней.
 * @author tselo
 *	Публичные свойства класса позволяют разработчикам работать с другими ресурсами, использующих аутентификацию StoryCLM 
 */
public abstract class BaseConnector {
	public static final String API_BASE_URL = "https://api.storyclm.com/v1/";
	protected Gson _gson;
	protected final AccessTokenManager _accessTokenManager;
	
	OkHttpClient.Builder _okHttpClientBuilder;
	/**
	 * предоставляет OkHttpClient.Builder  для  построения OkHttpClient с включенной функциональностью аутентификации/авторизации к StoryCLM
	 * @return
	 */
	public OkHttpClient.Builder getOkHttpClientBuilder(){
		return  _okHttpClientBuilder==null?_okHttpClientBuilder =getHttpClientBuilder(_accessTokenManager):_okHttpClientBuilder;
	};
	
	/**
	 * предоставляет менеджер аутентификации/авторизации
	 * @return
	 */
	public AccessTokenManager getAccessTokenManager(){
		return this._accessTokenManager;
	}
		
	public BaseConnector(AccessTokenManager accessTokenManager, GsonBuilder gBuilder){
		this._accessTokenManager = accessTokenManager;
		this._gson = gBuilder==null?_defaultGson:getGson(gBuilder);
		
	}
	
	
	private static OkHttpClient.Builder getHttpClientBuilder(AccessTokenManager accessTokenManager ){
		OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(new OAuthInterceptor(accessTokenManager));
        httpClientBuilder.authenticator(new OAuthAuthenticator(accessTokenManager));
        httpClientBuilder.readTimeout(60, TimeUnit.SECONDS);
        httpClientBuilder.connectTimeout(60, TimeUnit.SECONDS);
        return httpClientBuilder;
	}
	
	private Retrofit.Builder _builder;
	private Retrofit.Builder getbuilder(){
    	if (_builder==null)  {
    	_builder = new Retrofit.Builder()
       .addConverterFactory(GsonConverterFactory.create(this._gson))
       .client(getOkHttpClientBuilder().build());
    	}
    	return _builder;
    }
	
	protected Retrofit getRetrofit(String baseUrl){
		return this.getbuilder().baseUrl(baseUrl).build();
	}
	
	protected Retrofit getStoryCLMRetrofit(){
		return this.getRetrofit(API_BASE_URL);
	}
	protected Gson getGson(){
		return this._gson;
	}
	static Gson _defaultGson = getGson(new GsonBuilder());
	private static Gson getGson(GsonBuilder gsonBuilder){
	    	gsonBuilder.registerTypeAdapter(new TypeToken<Map<String, Object>>(){}.getType(),  new MapDeserializerDoubleAsIntFix());
	    	gsonBuilder.registerTypeAdapter(Date.class,  new StoryDateTypeAdapter());
	    	Gson gson = gsonBuilder.serializeNulls().setPrettyPrinting().create();
	     	return gson;
	
	}
}
