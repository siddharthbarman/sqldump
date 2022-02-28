package com.sbytestream;

public class ApplicationException extends Exception{
    public  ApplicationException() {
        super();
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Exception innerException) {
        super(message, innerException);
    }
}
