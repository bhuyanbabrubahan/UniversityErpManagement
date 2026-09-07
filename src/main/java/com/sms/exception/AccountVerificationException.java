package com.sms.exception;

import lombok.Getter;

@Getter
public class AccountVerificationException extends RuntimeException {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Object data;


    public AccountVerificationException(
            String message,
            Object data
    ) {

        super(message);

        this.data = data;
    }

}