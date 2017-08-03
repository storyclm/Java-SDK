package ru.breffi.storyclmsdk.AsyncResults;

import ru.breffi.storyclmsdk.OnResultCallback;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;
import ru.breffi.storyclmsdk.Exceptions.AuthFaliException;

public interface IAsyncResult<T> {

	public T GetResult()  throws AsyncResultException, AuthFaliException;

	public void OnResult(OnResultCallback<T> callback);
	
}
