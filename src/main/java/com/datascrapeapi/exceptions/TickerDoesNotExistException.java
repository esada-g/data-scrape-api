package com.datascrapeapi.exceptions;

public class TickerDoesNotExistException extends RuntimeException{
    public  TickerDoesNotExistException(String msg){
        super(msg);
    }
}
