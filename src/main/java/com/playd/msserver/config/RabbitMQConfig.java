/*
 * @(#) RabbitMQConfig.java 2020. 04. 17.
 *
 * Copyright 2020. PlayD Corp. All rights Reserved.
 */
package com.playd.msserver.config;

import com.playd.msserver.model.Constants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 박준영
 */
@EnableRabbit
@Configuration
public class RabbitMQConfig {

	/**
	 *  주어진 패턴과 일치하는 Queue에 메시지를 전달
	 *  설정할 수 있는 Exchange에는 Direct, Fanout, Topic, Headers
	 * @return
	 */
	@Bean
	DirectExchange exchange() {
		return new DirectExchange(Constants.EXCHANGE_NAME);
	}

	@Bean
	DirectExchange waitingExchange() {
		return new DirectExchange(Constants.WAITING_EXCHANGE_NAME);
	}

	@Bean
	DirectExchange deadLetterExchange() {
		return new DirectExchange(Constants.DEAD_LETTER_EXCHANGE_NAME);
	}

	@Bean
	Queue incomingQueue() {
		return QueueBuilder.durable(Constants.INCOMING_QUEUE_NAME)
				.withArgument("x-dead-letter-exchange", "")
				.withArgument("x-dead-letter-routing-key", Constants.WAITING_QUEUE_NAME)
				.build();
	}

	@Bean
	Queue waitingQueue() {
		return QueueBuilder.durable(Constants.WAITING_QUEUE_NAME)
				.withArgument("x-dead-letter-exchange", "")
				.withArgument("x-dead-letter-routing-key", Constants.DEAD_LETTER_QUEUE_NAME)
				.withArgument("x-message-ttl", Constants.RETRY_DELAY)
				.build();
	}

	@Bean
	Queue deadLetterQueue() {
		return QueueBuilder.durable(Constants.DEAD_LETTER_QUEUE_NAME).build();
	}

	/**
	 * Exchange가 Queue에게 메시지를 전달하기 위한 룰입니다.
	 * 빈으로 등록한 Queue와 Exchange를 바인딩하면서 Exchange에서 사용될 패턴을 설정
	 * @return
	 */
	@Bean
	Binding deadLetterBinding() {
		return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(Constants.DEAD_LETTER_QUEUE_NAME);
	}

	@Bean
	Binding waitingBinding() {
		return BindingBuilder.bind(waitingQueue()).to(waitingExchange()).with(Constants.WAITING_QUEUE_NAME);
	}

	@Bean
	public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}


}
