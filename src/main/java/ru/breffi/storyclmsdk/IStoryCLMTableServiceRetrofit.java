package ru.breffi.storyclmsdk;

import java.util.List;

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

public interface IStoryCLMTableServiceRetrofit{
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
	 
	 @GET("tables/{tableid}/count/")
	 public  Call<JsonObject> CountByQuery(@Path("tableid") int tableid,@Query("query") String query);
	 
	 @GET("tables/{tableid}/logcount/")
	 public Call<JsonObject> CountByLog(@Path("tableid") int tableid);
	
	 @GET("tables/{tableid}/logcount/")
	 public  Call<JsonObject> CountByLog(@Path("tableid") int tableid, @Query("date") long unixdate);

	 @GET("tables/{tableid}/log/")
	 public  Call<ApiLog[]> Log(@Path("tasbleid") int tableid,@Query("skip") int skip , @Query("take") int take);

	 @GET("tables/{tableid}/log/")
	 public Call<ApiLog[]> Log(@Path("tableid") int tableid,@Query("date") long unixdate,@Query("skip") int skip , @Query("take") int take);

		 
	 @GET("tables/{tableid}/find/")
	 public Call<JsonArray> Find(@Path("tableid") int tableid, @Query("query") String query, @Query("sortfield") String sortfield, @Query("sort") Integer sort,  @Query("skip") Integer skip , @Query("take") Integer take);
	 

	 @GET("tables/{tableid}/findbyid/{id}")
	 public  Call<JsonObject> Find(@Path("tableid") int tableid, @Path("id") String id);
	 
	 @GET("tables/{tableid}/findbyids/")
	 public Call<JsonArray> Find(@Path("tableid") int tableid, @Query("ids") String[] ids);
	 
	 @DELETE("tables/{tableid}/delete/{id}/")
	 public Call<JsonObject> Delete(@Path("tableid") int tableid, @Path("id") String id);
	
	 @DELETE("tables/{tableid}/deletemany/")
	 public Call<JsonArray> Delete(@Path("tableid") int tableid,  @Query("ids") List<String> ids);

	 //Aggregation

	 @GET("tables/{tableid}/max/{field}")
	 public Call<JsonObject> Max(@Path("tableid") int tableid,  @Path("field") String field, @Query("query") String query);

	 @GET("tables/{tableid}/min/{field}")
	 public Call<JsonObject> Min(@Path("tableid") int tableid,  @Path("field") String field, @Query("query") String query);

	 @GET("tables/{tableid}/sum/{field}")
	 public Call<JsonObject> Sum(@Path("tableid") int tableid,  @Path("field") String field, @Query("query") String query);

	 @GET("tables/{tableid}/avg/{field}")
	 public Call<JsonObject> Avg(@Path("tableid") int tableid,  @Path("field") String field, @Query("query") String query);

	 @GET("tables/{tableid}/first/")
	 public Call<JsonObject> First(@Path("tableid") int tableid,  @Query("query") String query, @Query("sortfield") String sortfield, @Query("sort") Integer sort);

	 @GET("tables/{tableid}/last/")
	 public Call<JsonObject> Last(@Path("tableid") int tableid,  @Query("query") String query, @Query("sortfield") String sortfield, @Query("sort") Integer sort);

	 
	 
	 

	 
	 
	 
	 
	 
}
