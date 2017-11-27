package ru.breffi.storyclmsdk.Calls;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ValueCall<T> implements Call<T> {

	T value;
	public ValueCall(T value){
		this.value = value;
	}
	@Override
	public Response<T> execute() throws IOException {
		isExecuted = true;
		return Response.success(value);
	}

	@Override
	public void enqueue(Callback<T> callback) {
		callback.onResponse(this,Response.success(value));
	}
	boolean isExecuted = false;
	@Override
	public boolean isExecuted() {
		return isExecuted;
	}

	@Override
	public void cancel() {}

	@Override
	public boolean isCanceled() {
		return false;
	}

	@Override
	public Call<T> clone() {
			return new ValueCall<T>(value);
	}

	@Override
	public Request request() {
		return null;
	}

}
