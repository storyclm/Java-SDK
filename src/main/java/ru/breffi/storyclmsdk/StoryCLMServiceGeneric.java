package ru.breffi.storyclmsdk;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ru.breffi.storyclmsdk.AsyncResults.AsyncResult;
import ru.breffi.storyclmsdk.AsyncResults.Converter;
import ru.breffi.storyclmsdk.AsyncResults.FinalAsyncResult;
import ru.breffi.storyclmsdk.AsyncResults.FinalValue;
import ru.breffi.storyclmsdk.AsyncResults.IAsyncResult;
import ru.breffi.storyclmsdk.AsyncResults.MiddleCallBackResult;
import ru.breffi.storyclmsdk.AsyncResults.ProxyCallResult;
import ru.breffi.storyclmsdk.AsyncResults.SequanceCallResult;
import ru.breffi.storyclmsdk.AsyncResults.SequanceChainCallResult;
import ru.breffi.storyclmsdk.AsyncResults.SingleValueConverter;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;
import ru.breffi.storyclmsdk.Exceptions.AuthFaliException;
import ru.breffi.storyclmsdk.Models.ApiLog;
import ru.breffi.storyclmsdk.Models.ApiTable;



public class StoryCLMServiceGeneric<T> {

	Converter<JsonObject,Long> j2intConverter = new Converter<JsonObject, Long>() {
		@Override
		public Long Convert(JsonObject in) {
			return in.get("count").getAsLong();}
		};
		
	
	private Type classOfT;
	private GenericListType<T> genericListTypeOfT;
	private Gson gson;
	private IStoryCLMService service;
	private int tableid;
	
	protected StoryCLMServiceGeneric(Type classOfT, IStoryCLMService service, Gson gson, int tableId){
		
		this.classOfT = classOfT;
		this.service = service;
		this.gson = gson;
		genericListTypeOfT = new GenericListType<T>(classOfT);
		this.tableid = tableId;
	}
	
	public IAsyncResult<ApiTable[]> GetTables(int clientid){
		 return new ProxyCallResult<>(service.GetTables(clientid));
	 }	 
	
	 
	public IAsyncResult<T> Insert(T record){ 
		return new AsyncResult<T,JsonObject>(service.Insert(tableid, record), classOfT, gson);
	 }
	 
	public IAsyncResult<List<T>> InsertMany(T[] records){
		 return new AsyncResult<List<T>,JsonArray>(service.InsertMany(tableid, records), genericListTypeOfT, gson);
	 }
	 
	
	public  IAsyncResult<T> Update(T record){
		 return new AsyncResult<T,JsonObject>(service.Update(tableid, record), classOfT, gson);
	 }

	 public IAsyncResult<List<T>> UpdateMany(T[] records){
		 return new AsyncResult<List<T>,JsonArray>(service.UpdateMany(tableid, records), genericListTypeOfT, gson);
	 }
	 
	
	 public IAsyncResult<Long> Count(){
		 return new ProxyCallResult<>(service.Count(tableid), j2intConverter);
	 }
	 
	 public IAsyncResult<Long> CountByQuery(String query){
		 return new ProxyCallResult<>(service.CountByQuery(tableid,  query),j2intConverter);
	 }
	 
	 public IAsyncResult<Long> CountByLog(){
		 return new ProxyCallResult<>(service.CountByLog(tableid),j2intConverter);
	 }
	 
	 public IAsyncResult<Long> CountByLog(Date date){
		 return new ProxyCallResult<>(service.CountByLog(tableid, date.getTime()/1000),j2intConverter);
	 }


	 public IAsyncResult<ApiLog[]> Log(int skip , int take){
		 return new ProxyCallResult<>(service.Log(tableid, skip,take));
	 }

	
	 public IAsyncResult<ApiLog[]> Log(Date date, int skip , int take){
		 return  new ProxyCallResult<>(service.Log(tableid,date.getTime()/1000, skip,take));
	 }

	

	 
	 /*
	  * Все параметры могут быть null
	  * skip - по умолчанию 0 на сервере
	  * take - по умолчанию 100 или 1000 на сервере
	  */
	 public IAsyncResult<List<T>> Find(String query, String sortfield, Integer sort,   Integer skip , Integer take){
		 return new AsyncResult<>(service.Find(tableid, query, sortfield,sort,skip,take), genericListTypeOfT, gson);
	 }
	 
	 public List<T> FindAllSync(String query) throws AsyncResultException, AuthFaliException{
		 long count = CountByQuery(query).GetResult();
		 List<T> resultArray = new ArrayList<T>();
		 for(int i=0;i<count;i+=100){
				resultArray.addAll(Find(query, "_id", 1, i,  100).GetResult());
			}
		return resultArray;
	 }
	 
	 /*
	  * Функция асинхронного получения всех записей
	  * Используется когда нужно получить все не заморачиваясь на skip  и take
	  * В целях "защиты сервера" используется задержка между вызовами 200 мс
	  * Оба параметра опциональны
	  * В слуае не указания porion будет использовать значение по умолчанию указанное на сервере
	  */
	 public IAsyncResult<List<T>> FindAll(final String query, final Integer portion) throws AsyncResultException, AuthFaliException{
		 final List<T> result = new ArrayList<T>();
		 return new SequanceChainCallResult<List<T>>(null){
				@Override
				public IAsyncResult<List<T>> GetInnerAsyncResult(List<T> previouResult) {
					if (previouResult!=null) 
						{
							result.addAll(previouResult);
							if (previouResult.size()==0) 
								return new FinalAsyncResult<>(result);
						}
					return Find(query, "_id", 1, result.size(),portion);
				}};  

	 }
	 
	 public IAsyncResult<T> Find(String id){
		 return new AsyncResult<>(service.Find(tableid, id), classOfT, gson);
	 }
	 
	 
	 public IAsyncResult<List<T>> Find(String[] ids){
		 return new AsyncResult<>(service.Find(tableid, ids), genericListTypeOfT, gson);
	 }
	 
	
	 public IAsyncResult<T> Delete(String id){
		 return new AsyncResult<>(service.Delete(tableid, id), classOfT, gson);
	 }
	
	 final int  maxIdsInUrl = 5;
	 
	 public IAsyncResult<List<ApiLog>> Delete(List<String> ids){
		 if (ids.size()>maxIdsInUrl) return Delete(ids,maxIdsInUrl);
		 return new AsyncResult<>(service.Delete(tableid, ids), genericListTypeOfT, gson);
	 }
	 
	 
	 public IAsyncResult<List<ApiLog>> Delete(final List<String> ids, int portion){
		 
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
	 
	 
	
	 public <R> IAsyncResult<R> Max(String field,String query, Class<R> resultType){
		 return new ProxyCallResult<>(service.Max(tableid,field, query), new SingleValueConverter<R>(gson, resultType));
	 }
	 
	 
	 
	 
	/* public <R> IAsyncResult<R> MaxOrDefault(String field,String query, Class<R> resultType, final R defaultResult){
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

	 public <R> IAsyncResult<R> Min(String field,String query, Class<R> resultType){
		 return new ProxyCallResult<>(service.Min(tableid,field, query), new SingleValueConverter<R>(gson, resultType));
	 }
	 
	 public <R> IAsyncResult<R> MinOrDefault(String field,String query, Class<R> resultType,R defaultResult){
		 return new ProxyCallResult<>(service.Min(tableid,field, query), new SingleValueConverter<R>(gson, resultType), defaultResult);
	 }  
	 
	 public <R> IAsyncResult<R> MaxOrDefault(String field,String query, Class<R> resultType,R defaultResult){
		 return new ProxyCallResult<>(service.Max(tableid,field, query), new SingleValueConverter<R>(gson, resultType), defaultResult);
	 }  
	 
	 
	 public <R> IAsyncResult<R> SumOrDefault(String field,String query, Class<R> resultType,R defaultResult){
		 return new ProxyCallResult<>(service.Sum(tableid,field, query), new SingleValueConverter<R>(gson, resultType), defaultResult);
	 }
	 public <R> IAsyncResult<R> AvgOrDefault(String field,String query, Class<R> resultType, R defaultResult){
		 return new ProxyCallResult<>(service.Avg(tableid,field, query), new SingleValueConverter<R>(gson, resultType), defaultResult);
	 }
	 
	 public  IAsyncResult<T> FirstOrDefault(String query,  String sortfield, Integer sort, T defaultResult){
		 return new AsyncResult<>(service.First(tableid,query, sortfield, sort), classOfT, gson,defaultResult);
	 }
	 
	 public  IAsyncResult<T> LastOrDefault(String query,  String sortfield, Integer sort, T defaultResult){
		 return new AsyncResult<>(service.Last(tableid,query, sortfield, sort), classOfT, gson,defaultResult);
	 }
	
	
}
