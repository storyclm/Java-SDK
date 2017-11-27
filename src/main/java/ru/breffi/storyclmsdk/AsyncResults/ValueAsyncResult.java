package ru.breffi.storyclmsdk.AsyncResults;

import ru.breffi.storyclmsdk.OnResultCallback;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;
import ru.breffi.storyclmsdk.Exceptions.AuthFaliException;

public class ValueAsyncResult<T> implements IAsyncResult<T>{

	T value;
	public ValueAsyncResult(T value) {
		this.value = value;
	}
	@Override
	public T GetResult() throws AsyncResultException, AuthFaliException {
		return value;
	}

	@Override
	public void OnResult(OnResultCallback<T> callback) {
		callback.OnSuccess(value);
	}

}
