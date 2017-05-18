package ru.breffi.storyclmsdk;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ru.breffi.storyclmsdk.AsyncResults.AsyncResult;
import ru.breffi.storyclmsdk.AsyncResults.Converter;
import ru.breffi.storyclmsdk.AsyncResults.IAsyncResult;
import ru.breffi.storyclmsdk.AsyncResults.ProxyCallResult;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;
import ru.breffi.storyclmsdk.Exceptions.AuthFaliException;
import ru.breffi.storyclmsdk.Models.ApiLog;
import ru.breffi.storyclmsdk.Models.ApiTable;



public class StoryCLMServiceGeneric<T> {

	Converter<JsonObject,Integer> j2intConverter = new Converter<JsonObject, Integer>() {
		@Override
		public Integer Convert(JsonObject in) {
			return in.get("count").getAsInt();}
		};
	
	private Type classOfT;
	private GenericListType<T> genericListTypeOfT;
	private Gson gson;
	private IStoryCLMService service;
	private int tableid;
	
	/*public StoryCLMServiceGeneric(Class<T> classOfT, StoryCLMService service, Gson gson){
		this.classOfT = classOfT;
		this.service = service;
		genericListTypeOfT = new GenericListType<T>(classOfT);
		this.gson = gson;
	}*/

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
	 
	
	 public IAsyncResult<Integer> Count(){
		 return new ProxyCallResult<>(service.Count(tableid), j2intConverter);
	 }
	 
	 public IAsyncResult<Integer> CountByQuery(String query){
		 return new ProxyCallResult<>(service.CountByQuery(tableid,  Base64.getEncoder().encodeToString(query.getBytes())),j2intConverter);
	 }
	 
	 public IAsyncResult<Integer> CountByLog(){
		 return new ProxyCallResult<>(service.CountByLog(tableid),j2intConverter);
	 }
	 
	 public IAsyncResult<Integer> CountByLog(Date date){
		 SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");
		 String unixdate = sdf.format(date);
		 return new ProxyCallResult<>(service.CountByLog(tableid, unixdate),j2intConverter);
	 }


	 public IAsyncResult<ApiLog[]> Log(int skip , int take){
		 return new ProxyCallResult<>(service.Log(tableid, skip,take));
	 }

	
	 public IAsyncResult<ApiLog[]> Log(Date date, int skip , int take){
		 SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");
		 String unixdate = sdf.format(date);
		 return  new ProxyCallResult<>(service.Log(tableid,unixdate, skip,take));
	 }

	
	 public IAsyncResult<List<T>>  Find(int skip , int take){
		 return new AsyncResult<List<T>,JsonArray>(service.Find(tableid, skip,take), genericListTypeOfT, gson);
	 }
	
	 
	 public IAsyncResult<List<T>> Find(String query, String sortfield, int sort,   int skip , int take){
		 return new AsyncResult<>(service.Find(tableid, query, sortfield,sort,skip,take), genericListTypeOfT, gson);
	 }
	 
	 public List<T> FindAllSync(String query) throws AsyncResultException, AuthFaliException{
		 int count = CountByQuery(query).GetResult();
		 List<T> resultArray = new ArrayList<T>();
		 for(int i=0;i<count;i+=100){
				resultArray.addAll(Find(query, "Id", 1, i,  100).GetResult());
			}
		return resultArray;
	 }
	 
	 public IAsyncResult<List<T>> Find(String id){
		 return new AsyncResult<>(service.Find(tableid, id), genericListTypeOfT, gson);
	 }
	 
	 
	 public IAsyncResult<List<T>> Find(String[] ids){
		 return new AsyncResult<>(service.Find(tableid, ids), genericListTypeOfT, gson);
	 }
	 
	
	 public IAsyncResult<T> Delete(String id){
		 return new AsyncResult<>(service.Delete(tableid, id), classOfT, gson);
	 }
	
	 
	 public IAsyncResult<ApiLog[]> Delete(String[] ids){
		 return new AsyncResult<>(service.Delete(tableid, ids), genericListTypeOfT, gson);
	 }

	
}
