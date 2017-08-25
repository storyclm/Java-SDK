package ru.breffi.storyclmsdk.Calls;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.breffi.storyclmsdk.converters.Converter;

public class ProxyConvertCall<Tin,Tout> implements Call<Tout> {
	Converter<Tin,Tout> converter;
	Call<Tin> jcall;
	Tout defaultResult = null;
	
	public ProxyConvertCall(Call<Tin> jcall, Converter<Tin,Tout> converter){
		this.jcall = jcall;
		this.converter = converter;
		}
	
	public ProxyConvertCall(Call<Tin> jcall, Converter<Tin,Tout> converter, Tout defaultResult){
		this(jcall,converter);
		this.defaultResult = defaultResult;
		}

	@Override
	public Response<Tout> execute() throws IOException {
		Response<Tin> response = jcall.execute();
		if (response.isSuccessful()){
			if (response.code()==204)
				return Response.success(defaultResult, response.raw());
			return Response.success(converter.Convert(response.body()),response.raw());
		}
		else return Response.error(response.code(), response.errorBody());
	}

	@Override
	public void enqueue(final Callback<Tout> callback) {
		final Call<Tout> self = this;
		this.jcall.enqueue(new Callback<Tin>() {  
		    @Override
		    public void onResponse(Call<Tin> call, Response<Tin> response) {
		    	if (response.isSuccessful()){
					if (response.code()==204)
						callback.onResponse(self, Response.success(defaultResult, response.raw()));
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
		return jcall.isExecuted();
	}

	@Override
	public void cancel() {
		jcall.cancel();
		
	}

	@Override
	public boolean isCanceled() {
		return jcall.isCanceled();
	}

	@Override
	public Call<Tout> clone() {
		return new ProxyConvertCall<>(jcall,converter);
	}

	@Override
	public Request request() {
		return jcall.request();
	}

}
