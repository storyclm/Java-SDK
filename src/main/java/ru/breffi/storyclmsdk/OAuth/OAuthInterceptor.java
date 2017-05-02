package ru.breffi.storyclmsdk.OAuth;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OAuthInterceptor implements Interceptor {
	final private AccessTokenManager accessTokenManager;
	
	public OAuthInterceptor(AccessTokenManager accessTokenManager) {
		this.accessTokenManager = accessTokenManager;
		// TODO Auto-generated constructor stub
	}
	   @Override
       public Response intercept(Chain chain) throws IOException {
           Request original = chain.request();
           Request.Builder requestBuilder = original.newBuilder()
                   .header("Accept", "application/json")
                   .header("Content-type", "application/json")
                   .header("Authorization",
                		  // "")
                		   accessTokenManager.getTokenType() + " " + accessTokenManager.getAccessToken())
                   .method(original.method(), original.body());

           Request request = requestBuilder.build();
         /*  try{
        	   
           
          System.out.println(original.url());
           System.out.println(original.body());
     
           System.out.println(original.body().contentLength());
           System.out.println(original.body().contentType());
           }
           catch(Exception ex){
        	   
           }
           //System.out.println(original.body);*/
           return chain.proceed(request);
       }
}
