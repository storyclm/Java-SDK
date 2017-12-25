package ru.breffi.storyclmsdk.Calls;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/*
 * Позволяет производить дополнительные преобразования над результатом используя метод middleHandler.  
 */
public abstract class ProxyCall<Tin, Tout> implements Call<Tout> {

	/*
	 * Абстрактный метод - вызывется в момент получения базового ответа Response.
	 */
	protected abstract Response<Tout> middleHandler(Response<Tin> response) throws IOException;
	
	Call<Tin> innerCall;
	public ProxyCall(Call<Tin> call)
	{
		innerCall = call;
	}
	
	@Override
	public Response<Tout> execute() throws IOException {
			return middleHandler(innerCall.execute());
	}

	@Override
	public void enqueue(Callback<Tout> callback) {
		Call<Tout> self = this;
		innerCall.enqueue(new Callback<Tin>() {
			@Override
			public void onResponse(Call<Tin> call, Response<Tin> response) {
				try{
					
				callback.onResponse(self, middleHandler(response));
				}
				catch(Exception e){
					callback.onFailure(self, e);
				}
			}

			@Override
			public void onFailure(Call<Tin> call, Throwable t) {
				callback.onFailure(self, t);
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
		return new ProxyCall<Tin, Tout>(this.innerCall.clone()) {
			@Override
			protected Response<Tout> middleHandler(Response<Tin> response) {
				return middleHandler(response);
			}
		};
	}

	@Override
	public Request request() {
		return innerCall.request();
	}

}
