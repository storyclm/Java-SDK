package ru.breffi.storyclmsdk;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.breffi.storyclmsdk.Models.ApiLog;
import ru.breffi.storyclmsdk.Models.ApiTable;

public interface IStoryCLMService{
	/**
	 * Получает список таблиц доступных пользователю
	 * @param clientid
	 * @return
	 */
	 @GET("tables/{clientid}/tables")
	 public Call<ApiTable[]> GetTables(@Path("clientid") int clientid);
	 
	 @POST("tables/{tableid}/insert")
	 public Call<JsonObject> Insert(@Path("tableid") int tableid,@Body Object record);
	 
	 @POST("tables/{tableid}/insertmany")
	 public  Call<JsonArray> InsertMany(@Path("tableid") int tableid, @Body Object[] records);
	 
	 @PUT("tables/{tableid}/update")
	 public Call<JsonObject> Update(@Path("tableid") int tableid, @Body Object record);

	 @PUT("tables/{tableid}/updatemany")
	 public Call<JsonArray> UpdateMany(@Path("tableid") int tableid,@Body Object[] record);
	 
	 @GET("tables/{tableid}/count")
	 public  Call<JsonObject> Count(@Path("tableid") int tableid);
	 
	 @GET("tables/{tableid}/countbyquery/{query}")
	 public  Call<JsonObject> CountByQuery(@Path("tableid") int tableid,@Path("query") String base64Query);
	 
	 @GET("tables/{tableid}/logcount/")
	 public Call<JsonObject> CountByLog(@Path("tableid") int tableid);
	 
	 @GET("tables/{tableid}/logcountbydate/{date}")
	 public  Call<JsonObject> CountByLog(@Path("tableid") int tableid,@Path("date") String unixdate);

	 @GET("tables/{tableid}/logcountbydate/{skip}/{take}")
	 public  Call<ApiLog[]> Log(@Path("tasbleid") int tableid,@Path("skip") int skip , @Path("take") int take);

	 @GET("tables/{tableid}/logbydate/{skip}/{take}/{date}")
	 public Call<ApiLog[]> Log(@Path("tableid") int tableid,@Path("date") String unixdate,@Path("skip") int skip , @Path("take") int take);

	 @GET("tables/{tableid}/findall/{skip}/{take}")
	 public  Call<JsonArray> Find(@Path("tableid") int tableid, @Path("skip") int skip , @Path("take") int take);
	 
	 @GET("tables/{tableid}/find/{query}/{sortfield}/{sort}/{skip}/{take}")
	 public Call<JsonArray> Find(@Path("tableid") int tableid, @Path("query") String query, @Path("sortfield") String sortfield, @Path("sort") int sort,  @Path("skip") int skip , @Path("take") int take);
	 
	 @GET("tables/{tableid}/findbyid/{id}")
	 public  Call<JsonArray> Find(@Path("tableid") int tableid, @Path("id") String id);
	 
	 @GET("tables/{tableid}/findbyids/")
	 public Call<JsonArray> Find(@Path("tableid") int tableid, String[] ids);
	 
	 @DELETE("tables/{tableid}/delete/{id}/")
	 public Call<JsonObject> Delete(@Path("tableid") int tableid, @Path("id") String id);
	
	 @DELETE("tables/{tableid}/deletemany/")
	 public Call<JsonObject> Delete(@Path("tableid") int tableid,  @Query("ids") String[] ids);




}
