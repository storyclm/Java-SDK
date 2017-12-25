package ru.breffi.storyclmsdk.Calls;

import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ProxyCheckerCall<T> extends ProxyCall<T,T>{

	public ProxyCheckerCall(Call<T> call) {
		super(call);
	}
	
	public abstract T returnThisIfNotNull();
	
	@Override
	public Response<T> execute() throws IOException {
		T result = returnThisIfNotNull();
		return (result==null)?super.execute(): Response.success(result); 
	}
	
	@Override
	public void enqueue(Callback<T> callback) {
		T result = returnThisIfNotNull();
		if (result==null)
			super.enqueue(callback);
		else callback.onResponse(this,Response.success(result));
	}
}
