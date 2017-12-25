package ru.breffi.storyclmsdk.AsyncResults;
@FunctionalInterface
public interface SuccessCallback<T> {
	public void OnSuccess(T result);
}
