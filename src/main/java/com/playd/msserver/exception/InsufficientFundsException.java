/*
 * @(#) InsufficientFundsException.java 2020. 05. 12.
 *
 * Copyright 2020. PlayD Corp. All rights Reserved.
 */
package com.playd.msserver.exception;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;

/**
 * @author 박준영
 */
public class InsufficientFundsException  extends AmqpRejectAndDontRequeueException {
	public InsufficientFundsException(String message) {
		super(message);
	}
}
