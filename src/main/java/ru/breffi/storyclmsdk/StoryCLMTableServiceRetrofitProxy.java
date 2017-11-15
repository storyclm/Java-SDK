package ru.breffi.storyclmsdk;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import ru.breffi.storyclmsdk.Calls.ProxyConvertCall;
import ru.breffi.storyclmsdk.Models.ApiLog;
import ru.breffi.storyclmsdk.Models.ApiTable;
import ru.breffi.storyclmsdk.converters.Converter;
import ru.breffi.storyclmsdk.converters.SingleValueConverter;
import ru.breffi.storyclmsdk.retrofitservices.IStoryCLMTableServiceRetrofit;



public class StoryCLMTableServiceRetrofitProxy<T> {
	
	
	Converter<JsonObject,Long> jCount2intConverter = (in)-> in.get("count").getAsLong();
		
	
		
	
	private Gson gson;
	private IStoryCLMTableServiceRetrofit service;
	private int tableid;
	
	Converter<JsonObject,T> fromJsonObject;
	Converter<JsonArray,List<T>> fromJsonArray;
	public StoryCLMTableServiceRetrofitProxy(Type classOfT, IStoryCLMTableServiceRetrofit service, Gson gson, int tableId){	
		this.service = service;
		this.gson = gson;
		this.tableid = tableId;
		GenericListType<T> genericListTypeOfT = new GenericListType<T>(classOfT);
		fromJsonObject = (t)->gson.fromJson(t,classOfT);
		fromJsonArray =(t)->gson.fromJson(t,genericListTypeOfT);
	}
	
	public Call<ApiTable[]> GetTables(int clientid){
		 return service.GetTables(clientid);
	}	 
		 
	public Call<T> Insert(T record){ 
		return new ProxyConvertCall<>(service.Insert(tableid, record),fromJsonObject);
	 }
	 
	public Call<List<T>> InsertMany(T[] records){
		 return new ProxyConvertCall<>(service.InsertMany(tableid, records), fromJsonArray);
	 }
	 
	
	public  Call<T> Update(T record){
		 return new ProxyConvertCall<>(service.Update(tableid, record), fromJsonObject);
	 }

	 public Call<List<T>> UpdateMany(T[] records){
		 return new ProxyConvertCall<>(service.UpdateMany(tableid, records), fromJsonArray);
	 }
	 
	public Call<Long> Count(){
		 return new ProxyConvertCall<>(service.Count(tableid), jCount2intConverter);
	 }
	 
	public Call<Long> CountByQuery(String query){
		 return new ProxyConvertCall<>(service.CountByQuery(tableid,  query),jCount2intConverter);
	 }
	 
	 public Call<Long> CountByLog(){
		 return new ProxyConvertCall<>(service.CountByLog(tableid),jCount2intConverter);
	 }
	 
	 public Call<Long> CountByLog(Date date){
		 return new ProxyConvertCall<>(service.CountByLog(tableid, date.getTime()/1000),jCount2intConverter);
	 }


	 public Call<ApiLog[]> Log(int skip , int take){
		 return service.Log(tableid, skip,take);
	 }

	
	 public Call<ApiLog[]> Log(Date date, int skip , int take){
		 return  service.Log(tableid,date.getTime()/1000, skip,take);
	 }

	

	 
	 /*
	  * Все параметры могут быть null
	  * skip - по умолчанию 0 на сервере
	  * take - по умолчанию 100 или 1000 на сервере
	  */
	 public Call<List<T>> Find(String query, String sortfield, Integer sort,   Integer skip , Integer take){
		 return new ProxyConvertCall<>(service.Find(tableid, query, sortfield,sort,skip,take), fromJsonArray);
	 }
	 
	 public List<T> FindAllSync(String query) throws IOException {
		 long count = CountByQuery(query).execute().body();
		 List<T> resultArray = new ArrayList<T>();
		 for(int i=0;i<count;i+=100){
				resultArray.addAll(Find(query, "_id", 1, i,  100).execute().body());
			}
		return resultArray;
	 }
		 
	 public Call<T> Find(String id){
		 return new ProxyConvertCall<>(service.Find(tableid, id), fromJsonObject);
	 }
	 	 
	 public Call<List<T>> Find(String[] ids){
		 return new ProxyConvertCall<>(service.Find(tableid, ids), fromJsonArray);
	 }
	 	
	 public Call<T> Delete(String id){
		 return new ProxyConvertCall<>(service.Delete(tableid, id), fromJsonObject);
	 }
	
	 final int  maxIdsInUrl = 5;
	 
	 public Call<List<ApiLog>> Delete(List<String> ids){
		 if (ids.size()>maxIdsInUrl) throw new RuntimeException("Превышено макс количество идентификаторов для удаления, функциональность не реализована"); //Invalid return Delete(ids,maxIdsInUrl);
		 return service.Delete(tableid, ids);
	 }
	 	
	 //--------------------------Создать конвертируемы колл
	  public <R> Call<R> Max(String field,String query, Class<R> resultType){
		 return new ProxyConvertCall<>(service.Max(tableid,field, query), new SingleValueConverter<R>(gson, resultType));
	 }
	
	 public <R> Call<R> Min(String field,String query, Class<R> resultType){
		 return new ProxyConvertCall<>(service.Min(tableid,field, query), new SingleValueConverter<R>(gson, resultType));
	 }
	 
	 public <R> Call<R> MinOrDefault(String field,String query, Class<R> resultType,R defaultResult){
		 return new ProxyConvertCall<>(service.Min(tableid,field, query), new SingleValueConverter<R>(gson, resultType),defaultResult);
	 }  
	 
	 public <R> Call<R> MaxOrDefault(String field,String query, Class<R> resultType,R defaultResult){
		 return new ProxyConvertCall<>(service.Max(tableid,field, query), new SingleValueConverter<R>(gson, resultType), defaultResult);
	 }  
	 
	 
	 public <R> Call<R> SumOrDefault(String field,String query, Class<R> resultType,R defaultResult){
		 return new ProxyConvertCall<>(service.Sum(tableid,field, query), new SingleValueConverter<R>(gson, resultType), defaultResult);
	 }
	 public <R> Call<R> AvgOrDefault(String field,String query, Class<R> resultType, R defaultResult){
		 return new ProxyConvertCall<>(service.Avg(tableid,field, query), new SingleValueConverter<R>(gson, resultType), defaultResult);
	 }
	 
	 public  Call<T> FirstOrDefault(String query,  String sortfield, Integer sort, T defaultResult){
		 return new ProxyConvertCall<>(service.First(tableid,query, sortfield, sort),fromJsonObject,defaultResult);
	 }
	 
	 public  Call<T> LastOrDefault(String query,  String sortfield, Integer sort, T defaultResult){
		 return new ProxyConvertCall<>(service.Last(tableid,query, sortfield, sort), fromJsonObject,defaultResult);
	 }
	
	
	 
	 /*
	 
	  //-------------------------Создать последовательный колл
	   
	   
	  public <R> IAsyncResult<R> MaxOrDefault(String field,String query, Class<R> resultType, final R defaultResult){
	 
		 return new SequanceCallResult<Long,R>(
				 this.CountByQuery(query),
				 new ProxyCallResult<>(service.Max(tableid,field, query), new SingleValueConverter<R>(gson, resultType))
				 )
		 			{
			 			@Override
			 			public MiddleCallBackResult<R> MiddleCallBack(Long firstResult) {
			 				return new MiddleCallBackResult<R>(defaultResult, firstResult==0);
					}};
		 
		 
		
		 //return new ProxyCallResult<>(service.Max(tableid,field, query), new SingleValueConverter<R>(gson, resultType));
	 }*/
	 
	 /*public Call<List<ApiLog>> Delete(final List<String> ids, int portion){
	 
	 final List<ApiLog> result= new ArrayList<ApiLog>();
	 final Integer finalPortion = (portion >maxIdsInUrl)?maxIdsInUrl:portion; 
	 final FinalValue<Integer> i = new FinalValue<>(0);
	 return new SequanceChainCallResult<List<ApiLog>>(null){
			@Override
			public IAsyncResult<List<ApiLog>> GetInnerAsyncResult(List<ApiLog> previouResult) {
				if (previouResult!=null) //В начале мы не попадем под это условие
					{
						result.addAll(previouResult);
						if (i.Value>=ids.size()) 
							return new FinalAsyncResult<>(result);
					}
				int fromIndex = i.Value;
				int toIndex = i.Value+finalPortion>ids.size()?ids.size():i.Value+finalPortion;
				i.SetValue(toIndex);
				return Delete(ids.subList(fromIndex,toIndex));
			}}; 
 }
 */
 
	 
}
