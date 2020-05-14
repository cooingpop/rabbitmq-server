/*
 * @(#) Constans.java 2020. 05. 12.
 *
 * Copyright 2020. PlayD Corp. All rights Reserved.
 */
package com.playd.msserver.model;

/**
 * @author 박준영
 */
public class Constants {
	private static final String QUEUE_NAME = "q-playd";
	public static final String EXCHANGE_NAME = QUEUE_NAME + ".exchange";
	public static final String INCOMING_QUEUE_NAME = QUEUE_NAME + ".incoming";
	public static final String WAITING_QUEUE_NAME = QUEUE_NAME + ".waiting";
	public static final String WAITING_EXCHANGE_NAME = WAITING_QUEUE_NAME + ".exchange";
	public static final String DEAD_LETTER_QUEUE_NAME = QUEUE_NAME + ".dead-letter";
	public static final String DEAD_LETTER_EXCHANGE_NAME = DEAD_LETTER_QUEUE_NAME + ".exchange";
	public static final int RETRY_DELAY = 2000; // in ms (1 min)
}
