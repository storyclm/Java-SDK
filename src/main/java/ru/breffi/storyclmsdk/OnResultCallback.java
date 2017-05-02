package ru.breffi.storyclmsdk;

public interface OnResultCallback<T> {

	public void OnSuccess(T result);
	public void OnFail(Throwable t);
	
}
