package ru.breffi.storyclmsdk.converters;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SingleValueConverter<T> implements Converter<JsonObject, T>{

	Class<T> classOfT;
	Gson gson;
	T defaultResult = null;
	public  SingleValueConverter(Gson gson, Class<T> classOfT){
		this.classOfT = classOfT;
		this.gson = gson;
	}
	
	public  SingleValueConverter(Gson gson, Class<T> classOfT, T defaultResult){
		this(gson,classOfT);
		this.defaultResult = defaultResult;
	}
	
	@Override
	public T Convert(JsonObject in) {
		return gson.fromJson(in.get("result"), classOfT);
	}

}
