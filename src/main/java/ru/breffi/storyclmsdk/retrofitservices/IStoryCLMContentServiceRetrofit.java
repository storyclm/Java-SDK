package ru.breffi.storyclmsdk.retrofitservices;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.breffi.storyclmsdk.Models.Client;
import ru.breffi.storyclmsdk.Models.PresentationUser;
import ru.breffi.storyclmsdk.Models.StoryContentPackage;
import ru.breffi.storyclmsdk.Models.StoryMediafile;
import ru.breffi.storyclmsdk.Models.StoryPresentation;
import ru.breffi.storyclmsdk.Models.StorySlide;

public interface IStoryCLMContentServiceRetrofit {

	 @GET("clients/")
	 public Call<Client[]> GetClients();
	 
	 @GET("clients/{clientid}")
	 public Call<Client> GetClient(@Path("clientid") int clientid);
	 
	 @GET("presentations/{presentationId}")
	 public Call<StoryPresentation> GetPresentation(@Path("presentationId") int presentationid);
	
	 @GET("mediafiles/{mediafileId}")
	 public Call<StoryMediafile> GetMediafile(@Path("mediafileId") int mediafileId);
	
	 @GET("slides/{slideId}")
	 public Call<StorySlide> GetSlide(@Path("slideId") int slideId);
	 
	 @GET("contentpackages/{presentationId}")
	 public Call<StoryContentPackage> GetСontentpackage(@Path("presentationId") int presentationId);

	 @GET("presentations/{presentationId}/users")
	 public Call<PresentationUser[]> GetPresentationUsers(@Path("presentationId") int presentationId);

	 @POST("presentations/{presentationId}/users")
	 public Call<PresentationUser[]> AddPresentationUsers(@Path("presentationId") int presentationId, @Body String[] usersIds);

	 @DELETE("presentations/{presentationId}/users")
	 public Call<PresentationUser[]> RemovePresentationUsers(@Path("presentationId") int presentationId,  @Query("users")  String[] usersIds);

	 @PUT("presentations/{presentationId}/users")
	 public Call<PresentationUser[]> SetPresentationUsers(@Path("presentationId") int presentationId,  @Body  String[] usersIds);

}
