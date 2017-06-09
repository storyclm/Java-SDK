package ru.breffi.storyclmsdk.AsyncResults;

import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.breffi.storyclmsdk.OnResultCallback;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;
import ru.breffi.storyclmsdk.Exceptions.ResultConnectionException;
import ru.breffi.storyclmsdk.Exceptions.ResultConvertException;
import ru.breffi.storyclmsdk.Exceptions.ResultServerException;

public class AsyncResult<T,J extends JsonElement> implements IAsyncResult<T> {
	
private Call<J> jcall;
private Gson gson;  
private Type classOfT;
T defaultResult = null;

/**
 * 
 * @param jcall
 * @param classOfT
 * @param gson 
 * Передаем сюда тот же (уже настроенный) экземпляр gson для единообразной конвертации
 */
public AsyncResult(Call<J> jcall, Type classOfT, Gson gson,T defaultResult){
	this.jcall = jcall;
	this.classOfT = classOfT;
	this.defaultResult = defaultResult;
	this.gson = (gson==null)? new Gson():gson;
}
public AsyncResult(Call<J> jcall, Type classOfT, Gson gson){
	this.jcall = jcall;
	this.classOfT = classOfT;
	this.gson = (gson==null)? new Gson():gson;
}

public T GetResult() throws AsyncResultException {
	try {
		Response<J> response = jcall.execute();
		if (!response.raw().isSuccessful()) throw new ResultServerException("Ошибка сервера: " + response.errorBody().string(), response.code());
		if (response.code()==204)
			return defaultResult;
		return gson.fromJson(response.body(), classOfT);
	} catch (JsonSyntaxException e) {
		   e.printStackTrace();
		    throw new ResultConvertException("Ошибка конвертации JSON!", e);
	}
	catch  (IOException e){
		throw new ResultConnectionException("Ошибка соединения!", e);
	}
}

public void OnResult(final OnResultCallback<T> callback){
	this.jcall.enqueue(new Callback<J>() {  
				    @SuppressWarnings("unchecked")
					@Override
				    public void onResponse(Call<J> call, Response<J> response) {
				        if (response.isSuccessful()) {
				        	 T result = (response.code()==204)?defaultResult:(T)gson.fromJson(response.body(),classOfT);
					         
					         callback.OnSuccess(result);
					         
				         
				        } else {
				        	String errorMessage = "невозможно прочитать сообщение об ошибке.";
				        	try {errorMessage=response.errorBody().string();} catch (IOException e) {}
				        	callback.OnFail(new ResultServerException("Ошибка сервера: " + errorMessage, response.code()));
				        }
				    }
				    @Override
				    public void onFailure(Call<J> call, Throwable t) {
				        callback.OnFail(t);

				    }
				});
}



}
