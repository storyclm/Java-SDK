package ru.breffi.storyclmsdk.OAuth;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OAuthService {
		@FormUrlEncoded
	    @POST("/connect/token")
	    Call<AuthEntity> getNewAuthEntity(
	            @Field("client_id") String clientId,
	            @Field("client_secret") String clientSecret,
	            @Field("username") String userName,
	            @Field("password") String password,
	            @Field("grant_type") String grantType);
		
		@FormUrlEncoded
	    @POST("/connect/token")
	    Call<AuthEntity> getRefreshedAuthEntity(
	            @Field("client_id") String clientId,
	            @Field("client_secret") String clientSecret,
	            @Field("refresh_token") String refresh_token,
	            @Field("grant_type") String grantType);
}
