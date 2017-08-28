package ru.breffi.storyclmsdk.OAuth;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import retrofit2.HttpException;
import retrofit2.Response;
import ru.breffi.storyclmsdk.Exceptions.AuthFaliException;
import ru.breffi.storyclmsdk.Exceptions.InvalidClientException;

public class AccessTokenManager {
class Error{
	public String error;
}
	private AuthEntity _authEntity;
	
  
    private String client_id;
    private String client_secret;
    private String user_name;
    private String password;
	String getGrantType(){
		return !(user_name == null || user_name.isEmpty() || password==null || password.isEmpty())? "password" : "client_credentials";
	}

 public AccessTokenManager(String client_id,String client_secret, String user_name, String password){
	 this.client_id = client_id;
	 this.client_secret = client_secret;
	 this.user_name = user_name;
	 this.password = password;
 }
    private static OAuthService _oauthService;
	private static OAuthService getOAuthService(){
		if (_oauthService == null){
			_oauthService = ServiceGenerator.createService(OAuthService.class);
		}
		return _oauthService;
	}
	
	/**
	 * Отдает данные для авторизации пользователя (AuthEntity). В том числе токен доступа
	 * Если пользователь еще не аутентифицирован, аутентифицирует его
	 * Если срок действия токена вышел, обновляет его либо заново аутентифицируется
	 * @return AuthEntity 
	 * @throws IOException
	 */
	public AuthEntity getAuthEntity() throws IOException{
		//Создать если нет
		_authEntity = (_authEntity == null)?newAuthEntity():_authEntity;
		
		//Проверим на срок
		if ((_authEntity.expires_date!=null) && _authEntity.expires_date.before(new Date())) RefreshToken();
		
		
		return _authEntity;
	}
	
	private AuthEntity newAuthEntity() throws IOException{
		Response<AuthEntity> response =  getOAuthService().getNewAccessToken(client_id, client_secret, user_name, password, getGrantType()).execute();
		return extractAuthEntity(response);
	}

	
	private AuthEntity extractAuthEntity(Response<AuthEntity> response) throws InvalidClientException, IOException{
		if (response.isSuccessful())
		{
			AuthEntity aentity = response.body();
			
			if (aentity.expires_in!=null){
				
				Calendar c = Calendar.getInstance(); 
				c.setTime(new Date()); 
				c.add(Calendar.SECOND, aentity.expires_in-5);	
				aentity.expires_date =c.getTime();
				
			}
			return aentity;
		}
		if (response.code()==400 & response.errorBody().string().contains("invalid_client")) throw new InvalidClientException(new HttpException(response));
		throw new AuthFaliException(response.code(), response.errorBody().string(),new HttpException(response)) ;

	}
	
	//ОБновляет токен, используя refreshtoken или изначальные данные
	public void RefreshToken() throws IOException{
		Response<AuthEntity> response;
		if (_authEntity!=null && _authEntity.refresh_token!=null){
			response =  getOAuthService().getNewAccessToken(client_id, client_secret, _authEntity.refresh_token, "refresh_token").execute();
			_authEntity = extractAuthEntity(response);
		}
		else 
			_authEntity = newAuthEntity();
	}
	
    public String  getAccessToken() throws IOException {
    	return getAuthEntity().access_token;
    }

    
    public String getTokenType() throws IOException{
        // OAuth requires uppercase Authorization HTTP header value for token type
        return  Character.toString(getAuthEntity().token_type.charAt(0)).toUpperCase() + getAuthEntity().token_type.substring(1);
}



  



}