package ru.breffi.storyclmsdk.OAuth;


import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class OAuthAuthenticator implements Authenticator {

	final private AccessTokenManager accessTokenManager;
    public OAuthAuthenticator(AccessTokenManager accessTokenManager) {
    	this.accessTokenManager = accessTokenManager;
		// TODO Auto-generated constructor stub
	}

	private static int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
    
	
	
	@Override
	public Request authenticate(Route route, Response response) throws IOException {
			  if(responseCount(response) >= 3) {
				  return null;
			  }
			  
              if(responseCount(response) >= 2) {
                  // If both the original call and the call with refreshed token failed,
                  // it will probably keep failing, so don't try again.
                  accessTokenManager.checkAndReturnAuthEntityAsync(true).execute();
              }
              return response.request().newBuilder()
                              .header("Authorization", accessTokenManager.getTokenType() + " " + accessTokenManager.getAccessToken())
                              .build();
	}
	

	
	}


