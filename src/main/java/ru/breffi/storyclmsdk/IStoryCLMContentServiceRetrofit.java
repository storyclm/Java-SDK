package ru.breffi.storyclmsdk;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.breffi.storyclmsdk.Models.Client;
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

}
