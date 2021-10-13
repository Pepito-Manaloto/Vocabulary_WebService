package com.aaron.vocabularyapi.exception;

public class InvalidTokenPasswordException extends Exception
{
    private static final long serialVersionUID = 1L;

    public InvalidTokenPasswordException(String msg)
    {
        super(msg);
    }
}
