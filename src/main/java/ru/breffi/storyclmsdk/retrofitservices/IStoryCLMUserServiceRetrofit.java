package ru.breffi.storyclmsdk.retrofitservices;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.breffi.storyclmsdk.Models.CreateUser;
import ru.breffi.storyclmsdk.Models.Group;
import ru.breffi.storyclmsdk.Models.Password;
import ru.breffi.storyclmsdk.Models.Presentation;
import ru.breffi.storyclmsdk.Models.SimpleUser;
import ru.breffi.storyclmsdk.Models.User;


public interface IStoryCLMUserServiceRetrofit {

	 @POST("users/")
	 public Call<CreateUser> Create(@Body CreateUser user);
	 
	 @PUT("users/")
	 public Call<User> Update(@Body User user);
	
	 @GET("users/")
	 public Call<SimpleUser[]> Get();
	 
	 @GET("users/exists")
	 public Call<User> Exists(@Query("username") String username);

	 @GET("users/{userId}")
	 public Call<User> Get(@Path("userId") String userId);
	 
	 @PUT("users/{userId}/password")
	 public Call<Void> UpdatePassword(@Path("userId") String userId,@Body Password password);

	 @PUT("users/{userId}/group/{groupId}")
	 public Call<Void> AddToGroup(@Path("userId") String userId,@Path("groupId") Integer groupId);
	 
	 @DELETE("users/{userId}/group/{groupId}")
	 public Call<Void> RemoveFromGroup(@Path("userId") String userId,@Path("groupId") Integer groupId);
	 
	 @PUT("users/{userId}/presentation/{presentationId}")
	 public Call<Void> AddToPresentation(@Path("userId") String userId,@Path("presentationId") Integer presentationId);

	 @DELETE("users/{userId}/presentation/{presentationId}")
	 public Call<Void> RemoveFromPresentation(@Path("userId") String userId,@Path("presentationId") Integer groupId);
	
	 @GET("users/{userId}/presentations")
	 public Call<Presentation[]> GetPresentations(@Path("userId") String userId);

	 
	 @GET("users/{userId}/groups")
	 public Call<Group[]> GetGroups(@Path("userId") String userId);

}
