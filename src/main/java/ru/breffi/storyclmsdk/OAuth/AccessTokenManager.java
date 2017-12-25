package ru.breffi.storyclmsdk.OAuth;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeoutException;


import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;
import ru.breffi.storyclmsdk.Calls.ProxyCheckerCall;
import ru.breffi.storyclmsdk.Calls.ProxyConvertCall;
import ru.breffi.storyclmsdk.Calls.ValueCall;
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

 private ServiceGenerator serviceGenerator;
 
 
 public AccessTokenManager(String client_id,String client_secret, String user_name, String password){
	 this(client_id,client_secret, user_name, password, null);
 }
 public AccessTokenManager(String client_id,String client_secret, String user_name, String password, String baseUrl){
	 this.client_id = client_id;
	 this.client_secret = client_secret;
	 this.user_name = user_name;
	 this.password = password;
	 this.serviceGenerator = new ServiceGenerator(baseUrl);
 }
    private OAuthService _oauthService;
	private OAuthService getOAuthService(){
		if (_oauthService == null){
			_oauthService = serviceGenerator.createService(OAuthService.class);
		}
		return _oauthService;
	}

	public Call<AuthEntity> checkAndReturnAuthEntityAsync() {
	 return checkAndReturnAuthEntityAsync(false);
	}
	/**
	 * Отдает данные для авторизации пользователя (AuthEntity). В том числе токен доступа
	 * Если пользователь еще не аутентифицирован, аутентифицирует его
	 * Если срок действия токена вышел, обновляет его либо заново аутентифицируется
	 * * @param forceRefresh 
	 * force refresh token 
	 * @return AuthEntity 
	 */
	public Call<AuthEntity> checkAndReturnAuthEntityAsync(boolean forceRefresh) {
		AccessTokenManager self = this;
		Call<AuthEntity> call = null;
		if (_authEntity == null) 
			call = (getOAuthService().getNewAuthEntity(client_id, client_secret, user_name, password, getGrantType()));
		else if (forceRefresh || authExpired()) 
			if (_authEntity.refresh_token != null)
				call = getOAuthService().getRefreshedAuthEntity(client_id, client_secret, _authEntity.refresh_token,"refresh_token");
			else 
				call = (getOAuthService().getNewAuthEntity(client_id, client_secret, user_name, password, getGrantType()));
		if (call != null)
			return new ProxyCheckerCall<AuthEntity>(call)
					{
						@Override
						public AuthEntity returnThisIfNotNull() {
							synchronized (self) {
								int i=0;
								while (self.busy)
								{
									try { Thread.sleep(500); } catch (InterruptedException e) {throw new RuntimeException(e);}
									if (++i>240) throw new RuntimeException(new TimeoutException("Can't allow access to AccessTokenManager busy."));
								}
								if (forceRefresh || _authEntity==null ||
									_authEntity.expires_date!=null && _authEntity.expires_date.before(new Date()))
								{
									self.busy = true;
									return null;
								}
								else return _authEntity; 
						}}
			
						@Override
						protected Response<AuthEntity> middleHandler(Response<AuthEntity> response) throws IOException 
						{
							self._authEntity = extractAuthEntity(response);
							self.busy = false;
							return 	Response.success(self._authEntity);
						}
					};
		else 
			{
				busy = false;
				return new ValueCall<AuthEntity>(_authEntity);
			}
	}
	

	
	private Boolean authExpired(){
		//return false;
		return  (_authEntity.expires_date!=null) && _authEntity.expires_date.before(new Date());
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
	Boolean busy = false;

	
    public Call<String>  getAccessTokenAsync()  {
    	 return  new ProxyConvertCall<AuthEntity, String>(
    			 checkAndReturnAuthEntityAsync(),
    			 authentity->authentity.access_token);
    }

    public String getAccessToken() throws IOException{
    	return getAccessTokenAsync().execute().body();
    }
    
    
    public Call<String> getTokenTypeAsync() {
        return  new ProxyConvertCall<AuthEntity, String>(
        		checkAndReturnAuthEntityAsync(),
        		authentity-> Character.toString(authentity.token_type.charAt(0)).toUpperCase() + authentity.token_type.substring(1)
        		);
        }
    public String getTokenType() throws IOException{
    	return getTokenTypeAsync().execute().body();
    }


  



}