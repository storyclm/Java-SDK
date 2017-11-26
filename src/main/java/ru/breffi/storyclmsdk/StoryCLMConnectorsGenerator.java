package ru.breffi.storyclmsdk;

import com.google.gson.GsonBuilder;
import ru.breffi.storyclmsdk.OAuth.AccessTokenManager;
import ru.breffi.storyclmsdk.connectors.RetrofitConnector;
import ru.breffi.storyclmsdk.connectors.StoryCLMServiceConnector;

public class StoryCLMConnectorsGenerator {
	
	 	protected static final String API_OAUTH_REDIRECT = null;

	 	private static String AuthUrl = "https://auth.storyclm.com/";
	 	private static String ApiUrl = "https://api.storyclm.com/v1/";
    	
    	/**
    	 * @deprecated
    	 * Создание нового коннектора с аутентификационными данными.
    	 * 
    	 * @param client_id - идентификатор клиента
    	 * @param client_secret - секретный ключ клиента
    	 * @param gBuilder - билдер для настройки Gson конвертера (null - если настройка не требуется)
    	 * @return Коннектор используется для создания конкретных типизированных сервисов для работы с конкретными таблицами клиента.
    	*/
    	public static StoryCLMServiceConnector GetStoryCLMServiceConnector(String client_id, String client_secret,String user_name, String password, GsonBuilder gBuilder) 
    		{
    			return CreateStoryCLMServiceConnector(client_id, client_secret, user_name, password, gBuilder);
    		}
    	
    	/**
    	 * Создание нового коннектора с аутентификационными данными.
    	 * Возвращаемый коннектор является оберткой над сервисами Retrofit2, позволяющей использовать обобщенные параметры при создании табличных сервисов. 
    	 * 
    	 * 
    	 * @param client_id - идентификатор клиента
    	 * @param client_secret - секретный ключ клиента
    	 * @param user_name - имя пользователя (если нет - null)
    	 * @param password - пароль (если нет - null)
    	 * @param gBuilder - билдер для настройки Gson конвертера (null - если настройка не требуется)
    	 * @return Коннектор используется для создания конкретных типизированных сервисов для работы с конкретными таблицами клиента.

    	 */
    	public static StoryCLMServiceConnector CreateStoryCLMServiceConnector(String client_id, String client_secret,String user_name, String password, GsonBuilder gBuilder) {
    		return CreateStoryCLMServiceConnector(client_id, client_secret,user_name, password, gBuilder, AuthUrl, ApiUrl); 
    	}
    	
    	public static StoryCLMServiceConnector CreateStoryCLMServiceConnector(String client_id, String client_secret,String user_name, String password, GsonBuilder gBuilder, String authUrl, String apiurl) {
        		StoryCLMServiceConnector result = null ; 
        		AccessTokenManager accessTokenManager = new AccessTokenManager(client_id, client_secret, user_name, password, authUrl); 
                result = new StoryCLMServiceConnector(accessTokenManager,gBuilder,apiurl);
        		return result;
        	}
    	/**
    	 * Создание нового коннектора с аутентификационными данными. 
    	 * Возвращаемый коннектор предоставляет сервисы совместимые с сервисами Retrofit2. 
    	 * 
    	 * @param client_id - идентификатор клиента
    	 * @param client_secret - секретный ключ клиента
    	 * @param user_name - имя пользователя (если нет - null)
    	 * @param password - пароль (если нет - null)
    	 * @param gBuilder - билдер для настройки Gson конвертера (null - если настройка не требуется)
    	 * @return Коннектор используется для создания конкретных типизированных сервисов для работы с конкретными таблицами клиента.
    	 * 
    	 */
    	public static RetrofitConnector CreateRetrofitConnector(String client_id, String client_secret,String user_name, String password, GsonBuilder gBuilder, String authUrl, String apiUrl) {
    		AccessTokenManager accessTokenManager = new AccessTokenManager(client_id, client_secret, user_name, password, authUrl);
    		RetrofitConnector result = new RetrofitConnector(accessTokenManager, gBuilder,apiUrl);
    		return result; 	
    	}
    	public static RetrofitConnector CreateRetrofitConnector(String client_id, String client_secret,String user_name, String password, GsonBuilder gBuilder) {
    	 	return CreateRetrofitConnector(client_id, client_secret,user_name, password, gBuilder, AuthUrl, ApiUrl);
    	}
}
