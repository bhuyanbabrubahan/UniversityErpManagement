package com.sms.audit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sms.audit.enums.AuditAction;

@Target(ElementType.METHOD)

@Retention(RetentionPolicy.RUNTIME)

public @interface AuditLog {

	/**
	 * Audit action type
	 */
	AuditAction action();

	/**
	 * Module name
	 */
	String module();

	/**
	 * Description
	 */
	String description() default "";

}