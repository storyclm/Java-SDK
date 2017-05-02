package ru.breffi.storyclmsdk.Exceptions;

import java.io.IOException;

@SuppressWarnings("serial")
public class InvalidClientException extends IOException{
	public InvalidClientException(Exception ex){
		super(ex);
	}
}
