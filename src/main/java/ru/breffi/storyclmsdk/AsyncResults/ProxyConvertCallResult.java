package ru.breffi.storyclmsdk.AsyncResults;

import java.io.IOException;


import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Response;
import ru.breffi.storyclmsdk.OnResultCallback;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;
import ru.breffi.storyclmsdk.Exceptions.AuthFaliException;
import ru.breffi.storyclmsdk.Exceptions.ResultServerException;
import ru.breffi.storyclmsdk.converters.Converter;

public class ProxyConvertCallResult<Tin,Tout> implements IAsyncResult<Tout> {
	private Call<Tin> jcall;
	Converter<Tin,Tout> converter;
	Tout defaultResult = null;
	public ProxyConvertCallResult(Call<Tin> jcall, Converter<Tin,Tout> converter){
		this.jcall = jcall;
		this.converter = converter;
	}
	
	public ProxyConvertCallResult(Call<Tin> jcall, Converter<Tin,Tout> converter, Tout defaultResult){
		this.jcall = jcall;
		this.converter = converter;
		this.defaultResult = defaultResult;
	}
	
	
	public ProxyConvertCallResult(Call<Tin> jcall){
		this(jcall,new Converter<Tin, Tout>(){
			@SuppressWarnings("unchecked")
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
			if (!response.raw().isSuccessful()) throw new ResultServerException("Ошибка сервера: " + response.code() + ", "+ response.errorBody().string(), response.code(), response.errorBody().string());
			if (response.code()==204)
				return defaultResult;
			return converter.Convert(response.body());
		} 
		catch (AuthFaliException e){
			if (attempts> 4) throw e;
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
		         Tout result = (response.code()==204)?defaultResult:converter.Convert(response.body());
		         
		         callback.OnSuccess(result);
		        } else {
		        	String errorBody = "";
		        	try {
						errorBody = response.errorBody().string();
					} catch (IOException e) {
						errorBody = "IOException during read errorbody";
					}
		        	Throwable error = new ResultServerException("Ошибка сервера: " + response.code() + ", "+ errorBody, response.code(), errorBody);
					callback.OnFail(error);
		        }
		    }
		    @Override
		    public void onFailure(Call<Tin> call, Throwable t) {
		        callback.OnFail(t);
		    }
		});
		
	}

}
