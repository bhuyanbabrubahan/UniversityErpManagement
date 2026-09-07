package com.sms.exception;

public class DuplicateDepartmentException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	public DuplicateDepartmentException(String message) {
		super (message);
	}
}
