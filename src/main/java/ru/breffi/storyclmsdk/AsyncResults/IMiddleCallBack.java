package ru.breffi.storyclmsdk.AsyncResults;

public interface IMiddleCallBack<Tin,Tout> {

	public Tout Execute(Tin in);
	
}
