/*
 * @(#) Order.java 2020. 05. 08.
 *
 * Copyright 2020. PlayD Corp. All rights Reserved.
 */
package com.playd.msserver.model;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author 박준영
 */

@Data
public class Order {
	private String id;
	private LocalDate timestamp;

	public Order() {
	}

	@Override
	public String toString() {
		return "Order{" +
				"id='" + id + '\'' +
				", timestamp=" + timestamp +
				'}';
	}
}
