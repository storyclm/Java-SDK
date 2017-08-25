package ru.breffi.storyclmsdk.converters;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class FromJsonGenericConverter<Tin extends JsonElement, Tout> implements Converter<Tin,Tout>{

	private Gson gson;
	private Type classOfT;
	
	public FromJsonGenericConverter(Gson gson, Type classOfT){
		this.classOfT = classOfT;
		this.gson = gson;
	}
	
	@Override
	public Tout Convert(Tin in) {
		// TODO Auto-generated method stub
		return gson.fromJson(in,classOfT);
	}

}
