package ru.breffi.storyclmsdk.Calls;

import retrofit2.Call;
import retrofit2.Response;
import ru.breffi.storyclmsdk.converters.Converter;

/*
 * Позволяет производить дополнительные преобразования над результатом используя конвертеры.
 * Использует возможность возвращать сервисом JsonObject, который можно затем преобразовать в любой тип.
 * Класс позволяет создавать обобщенные retrofit сервисы. 
 *   
 */
public class ProxyConvertCall<Tin,Tout> extends ProxyCall<Tin, Tout>{
	Converter<Tin,Tout> converter;
	
	Tout defaultResult = null;
	
	public ProxyConvertCall(Call<Tin> jcall, Converter<Tin,Tout> converter,Tout defaultResult){
		super(jcall);
		this.converter = converter;
		this.defaultResult = defaultResult;
	}
	
	public ProxyConvertCall(Call<Tin> jcall, Converter<Tin,Tout> converter){
		super(jcall);
		this.converter = converter;
	}
	
	@Override
	protected
	Response<Tout> middleHandler(Response<Tin> response) {
		if (response.isSuccessful()){
			if (response.code()==204)
				return Response.success(defaultResult, response.raw());
			return Response.success(converter.Convert(response.body()),response.raw());
		}
		else return Response.error(response.code(), response.errorBody());
	}

}
