package ru.breffi.storyclmsdk.Exceptions;

import java.io.IOException;


@SuppressWarnings("serial")
public class AuthFaliException extends IOException {
public int Code;
public String ErrorBody;

public AuthFaliException(int code, String errorBody, Exception httpException) {
	super(httpException);
	Code = code;
	ErrorBody = errorBody;
	
}
}
