package ru.breffi.storyclmsdk.AsyncResults;
@FunctionalInterface
public interface FailCallback {
	public void OnFail(Throwable t);
}
