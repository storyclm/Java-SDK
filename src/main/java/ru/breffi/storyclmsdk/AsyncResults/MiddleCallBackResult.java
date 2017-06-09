package ru.breffi.storyclmsdk.AsyncResults;

public class MiddleCallBackResult<T> {

	public final boolean useResult;
	public final T result;
	public MiddleCallBackResult(T result,boolean useResult){
		this.result = result;
		this.useResult = useResult;
	}
	
}
