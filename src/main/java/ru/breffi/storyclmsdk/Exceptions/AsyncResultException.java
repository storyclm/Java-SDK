package ru.breffi.storyclmsdk.Exceptions;

@SuppressWarnings("serial")
public class AsyncResultException extends Exception
{
	//Parameterless Constructor
      public AsyncResultException() {}

      //Constructor that accepts a message
      public AsyncResultException(String message, Throwable cause)
      {
         super(message,cause);
      }
 }