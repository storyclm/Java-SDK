package ru.breffi.storyclmsdk.connectors;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.breffi.storyclmsdk.Calls.ProxyConvertCall;
import ru.breffi.storyclmsdk.OAuth.AccessTokenManager;
import ru.breffi.storyclmsdk.OAuth.OAuthAuthenticator;
import ru.breffi.storyclmsdk.OAuth.OAuthInterceptor;
import ru.breffi.storyclmsdk.TypeAdapters.MapDeserializerDoubleAsIntFix;
import ru.breffi.storyclmsdk.TypeAdapters.StoryDateTypeAdapter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Базовый коннектор, предоставляющий функциональность аутентицикации/авторизации, а также информацию о ней.
 * @author tselo
 *	Публичные свойства класса позволяют разработчикам работать с другими ресурсами, использующих аутентификацию StoryCLM 
 */
public abstract class BaseConnector {
	private final String API_BASE_URL;// = "https://api.storyclm.com/v1/";
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
	
	protected Call<Integer> getClientId() {
		return new ProxyConvertCall<String,Integer>(
				getAccessTokenManager().getAccessTokenAsync(),
				accesstoken->
				{
					
					
					
					byte[] decoded = BaseEncoding.base64().decode(accesstoken.split("\\.")[1]);
					
					//byte[] decoded = Base64.getDecoder().decode(accesstoken.split("\\.")[1]);
					String str = new String(decoded, StandardCharsets.UTF_8);
					int res = Integer.parseInt(getGson().fromJson(str, JsonObject.class).get("client_id").getAsString().split("_")[1]);
					return res;
				});
		}
		
	public BaseConnector(AccessTokenManager accessTokenManager, GsonBuilder gBuilder, String api_base_url){
		this._accessTokenManager = accessTokenManager;
		this._gson = gBuilder==null?_defaultGson:getGson(gBuilder);
		this.API_BASE_URL = api_base_url;
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
