package ru.breffi.storyclmsdk.Exceptions;

@SuppressWarnings("serial")
public class ResultServerException extends AsyncResultException {

	public int Code;
	public ResultServerException(String message, int code) {
		super(message,null);
		Code = code;
		// TODO Auto-generated constructor stub
	}

}
