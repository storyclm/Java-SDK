package ru.breffi.storyclmsdk.Exceptions;

@SuppressWarnings("serial")
public class ResultServerException extends AsyncResultException {

	public int Code;
	public String ErrorBody;
	public ResultServerException(String message, int code, String errorBody) {
		super(message,null);
		Code = code;
		ErrorBody = errorBody;
		// TODO Auto-generated constructor stub
	}

}
