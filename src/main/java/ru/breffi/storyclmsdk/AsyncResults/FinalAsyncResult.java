package ru.breffi.storyclmsdk.AsyncResults;

import ru.breffi.storyclmsdk.OnResultCallback;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;
import ru.breffi.storyclmsdk.Exceptions.AuthFaliException;

public class FinalAsyncResult<T> implements IAsyncResult<T>{

	T result;
	
	public FinalAsyncResult(T result) {
		this.result = result;
	}
	
	@Override
	public T GetResult() throws AsyncResultException, AuthFaliException {
		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public void OnResult(OnResultCallback<T> callback) {
		callback.OnSuccess(result);
		
	}


}
