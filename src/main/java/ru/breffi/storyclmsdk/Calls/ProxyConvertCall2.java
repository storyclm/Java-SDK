package ru.breffi.storyclmsdk.Calls;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.breffi.storyclmsdk.converters.Converter;

/*
 * Позволяет производить дополнительные преобразования над результатом используя конвертеры.
 * Использует возможность возвращать сервисом JsonObject, который можно затем преобразовать в любой тип.
 * Класс позволяет создавать обобщенные retrofit сервисы. 
 *   
 */
public class ProxyConvertCall2<Tin,Tout> implements Call<Tout> {
	Converter<Tin,Tout> converter;
	Call<Tin> innerCall;
	
	public ProxyConvertCall2(Call<Tin> jcall, Converter<Tin,Tout> converter){
		this.innerCall = jcall;
		this.converter = converter;
		}
	
	@Override
	public Response<Tout> execute() throws IOException {
		Response<Tin> response = innerCall.execute();
		if (response.isSuccessful()){
			return Response.success(converter.Convert(response.body()),response.raw());
		}
		else return Response.error(response.code(), response.errorBody());
	}

	@Override
	public void enqueue(final Callback<Tout> callback) {
		final Call<Tout> self = this;
		this.innerCall.enqueue(new Callback<Tin>() {  
		    @Override
		    public void onResponse(Call<Tin> call, Response<Tin> response) {
		    	if (response.isSuccessful()){
					callback.onResponse(self, Response.success(converter.Convert(response.body()), response.raw()));
				}
				else callback.onResponse(self, Response.error(response.code(), response.errorBody()));
		    }
		    @Override
		    public void onFailure(Call<Tin> call, Throwable t) {
		        callback.onFailure(self,t);
		    }
		});
		
	}

	@Override
	public boolean isExecuted() {
		return innerCall.isExecuted();
	}

	@Override
	public void cancel() {
		innerCall.cancel();
		
	}

	@Override
	public boolean isCanceled() {
		return innerCall.isCanceled();
	}

	@Override
	public Call<Tout> clone() {
		return new ProxyConvertCall2<>(innerCall,converter);
	}

	@Override
	public Request request() {
		return innerCall.request();
	}

}
