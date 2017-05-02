package ru.breffi.storyclmsdk.AsyncResults;

import java.io.IOException;


import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Response;
import ru.breffi.storyclmsdk.OnResultCallback;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;
import ru.breffi.storyclmsdk.Exceptions.AuthFaliException;
import ru.breffi.storyclmsdk.Exceptions.ResultServerException;

public class ProxyCallResult<Tin,Tout> implements IAsyncResult<Tout> {
	private Call<Tin> jcall;
	Converter<Tin,Tout> converter;
	public ProxyCallResult(Call<Tin> jcall, Converter<Tin,Tout> converter){
		this.jcall = jcall;
		this.converter = converter;
	}
	
	public ProxyCallResult(Call<Tin> jcall){
		this(jcall,new Converter<Tin, Tout>(){@SuppressWarnings("unchecked")
			@Override
			public Tout Convert(Tin in) {
				return (Tout)in;
			}
		});
	}
	
	int attempts=0;
	@Override
	public Tout GetResult() throws AsyncResultException, AuthFaliException {
		try {
			Response<Tin> response = jcall.execute();
			if (!response.raw().isSuccessful()) throw new ResultServerException("Ошибка сервера: " + response.code() + ", "+ response.errorBody().string(), response.code());
			return converter.Convert(response.body());
		} 
		catch (AuthFaliException e){
			if (attempts>3) throw e;
			attempts++;
			//попробуем еще раз
			return this.GetResult();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new AsyncResultException("Ошибка при получении данных!", e);
		}
		finally{
			attempts=0;
		}
	} 

	@Override
	 public void OnResult(final OnResultCallback<Tout> callback) {
		this.jcall.enqueue(new Callback<Tin>() {  
		    @Override
		    public void onResponse(Call<Tin> call, Response<Tin> response) {
		        if (response.isSuccessful()) {
		         callback.OnSuccess(converter.Convert(response.body()));
		        } else {
		          callback.OnFail(null);
		        }
		    }
		    @Override
		    public void onFailure(Call<Tin> call, Throwable t) {
		        callback.OnFail(t);
		    }
		});
		
	}

}
