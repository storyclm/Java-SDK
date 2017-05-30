package ru.breffi.storyclmsdk;

public class SingleValueResult<T> {

	T value;
	public T getValue(){
		return value;
	}
	
	public void setValue(T value){
		this.value = value;
	}
}
