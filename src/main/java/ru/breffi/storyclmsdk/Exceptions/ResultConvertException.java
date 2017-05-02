package ru.breffi.storyclmsdk.Exceptions;

import com.google.gson.JsonSyntaxException;

@SuppressWarnings("serial")
public class ResultConvertException extends AsyncResultException{
	public ResultConvertException(String message, JsonSyntaxException e) {
		super(message,e);
}
}
