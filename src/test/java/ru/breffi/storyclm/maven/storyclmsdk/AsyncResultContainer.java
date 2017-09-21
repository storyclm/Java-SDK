package ru.breffi.storyclm.maven.storyclmsdk;

public class AsyncResultContainer<T> {	
	public boolean completed = false;
	public T result;
	public boolean fail =false; 
	public void Completed(){
		completed = true;
	}
	public void Failed(){
		fail = true;
	}
}
