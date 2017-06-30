package ru.breffi.storyclmsdk.AsyncResults;

public class FinalValue<T> {
	public T Value;
	public FinalValue(T value){
		this.Value = value;
	}
	public T SetValue(T newValue){
		return Value = newValue;
	}
}
