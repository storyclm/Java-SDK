package ru.breffi.storyclmsdk.Exceptions;

import java.io.IOException;

@SuppressWarnings("serial")
public class ResultConnectionException extends AsyncResultException{
	public ResultConnectionException(String message, IOException e) {
		super(message, e);
	}

}
