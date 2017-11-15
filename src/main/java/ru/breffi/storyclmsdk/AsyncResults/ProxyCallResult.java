package ru.breffi.storyclmsdk.AsyncResults;

import retrofit2.Call;

public class ProxyCallResult<T> extends ProxyConvertCallResult<T,T>{

	public ProxyCallResult(Call<T> jcall) {
		super(jcall, r->r);
	}
	
}