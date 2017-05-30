package ru.breffi.storyclmsdk.AsyncResults;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SingleValueConverter<T> implements Converter<JsonObject, T>{

	Class<T> classOfT;
	Gson gson;
	public  SingleValueConverter(Gson gson, Class<T> classOfT){
		this.classOfT = classOfT;
		this.gson = gson;
	}
	
	@Override
	public T Convert(JsonObject in) {
		return gson.fromJson(in.get("result"), classOfT);
	}

}
