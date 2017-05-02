package ru.breffi.storyclmsdk.OAuth;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OAuthService {
		@FormUrlEncoded
	    @POST("/connect/token")
	    Call<AuthEntity> getNewAccessToken(
	            @Field("client_id") String clientId,
	            @Field("client_secret") String clientSecret,
	            @Field("grant_type") String grantType);
}
