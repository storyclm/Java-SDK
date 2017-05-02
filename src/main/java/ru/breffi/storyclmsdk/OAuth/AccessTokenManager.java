package ru.breffi.storyclmsdk.OAuth;

import java.io.IOException;

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
	String grantType = "client_credentials";

 public AccessTokenManager(String client_id,String client_secret){
	 this.client_id = client_id;
	 this.client_secret = client_secret;
 }
    private static OAuthService _oauthService;
	private static OAuthService getOAuthService(){
		if (_oauthService == null){
			_oauthService = ServiceGenerator.createService(OAuthService.class);
		}
		return _oauthService;
	}
	private AuthEntity getAuthEntity() throws IOException{
		_authEntity = (_authEntity == null)?newAuthEntity():_authEntity;
		return _authEntity;
	}
	
	private AuthEntity newAuthEntity() throws IOException{
		Response<AuthEntity> response =  getOAuthService().getNewAccessToken(client_id, client_secret, grantType).execute();
		if (response.isSuccessful()) return response.body();
		if (response.code()==400 & response.errorBody().string().contains("invalid_client")) throw new InvalidClientException(new HttpException(response));
		throw new AuthFaliException(response.code(), response.errorBody().string(),new HttpException(response)) ;
	}

	public void RefreshToken() throws IOException{
		_authEntity = newAuthEntity();
	}
	
    public String  getAccessToken() throws IOException {
    	return getAuthEntity().access_token;
    }
public AccessTokenManager UpdateSecretId(String client_secret) throws IOException{
	if (client_secret!= this.client_secret){
		this.client_secret = client_secret;
		this.RefreshToken();
	}
	return this;
}
    
    public String getTokenType() throws IOException{
        // OAuth requires uppercase Authorization HTTP header value for token type
        return  Character.toString(getAuthEntity().token_type.charAt(0)).toUpperCase() + getAuthEntity().token_type.substring(1);
}



  



}