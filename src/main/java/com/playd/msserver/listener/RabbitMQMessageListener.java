/*
 * @(#) RabbitMQMessageListener.java 2020. 05. 07.
 *
 * Copyright 2020. PlayD Corp. All rights Reserved.
 */
package com.playd.msserver.listener;

import com.playd.msserver.model.Constants;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.playd.msserver.model.Constants.*;

/**
 * @author 박준영
 */

@Component
public class RabbitMQMessageListener {

	private final String LOG_PREFIX = "[MessageListener]";
	private Logger logger = LoggerFactory.getLogger(RabbitMQMessageListener.class);

	private RabbitTemplate rabbitTemplate;

	public RabbitMQMessageListener(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void listen(@Payload String msg, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Message message , Channel channel) throws IOException {
		try {
			Thread.sleep(ThreadLocalRandom.current().nextLong(2000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@RabbitListener(queues = INCOMING_QUEUE_NAME, concurrency = "4")
	public void receiveMessage(final Message message, @Header(required = false, name = "x-death") List<Map<String, Object>> xDeath) {
		logger.info(">> receiveMessage " + message);
		long retryCount = 0;
		if (xDeath != null) {
			Optional<Long> count = xDeath.stream()
					.flatMap(m -> m.entrySet().stream())
					.filter(e -> e.getKey().equals("count"))
					.findFirst().map(e -> (Long) e.getValue());
			if (count.isPresent()) {
				retryCount = count.get().longValue();
			}
		}
		if (retryCount < 3) {
			if (new Random().nextBoolean()) {
				rabbitTemplate.convertAndSend(WAITING_EXCHANGE_NAME, WAITING_QUEUE_NAME, message);
			}
		} else {
			logger.info("Received <" + message + ">");
		}

	}

	/**
	 * Message listener for testApp2
	 * */
	@RabbitListener(queues = Constants.WAITING_QUEUE_NAME, concurrency = "1")
	public void waitingMessage(Message message) {
		logger.info(">> waitingMessage  " + message);
		try {

			if (new Random().nextBoolean()) {
				putIntoDead(message);
				return;
			}

			// Note : 추후 프로그램 로직이 들어갈 부분

			logger.info(">> 메세지 수신 성공 " + message);

		} catch(Exception e) {
			System.out.println("Internal server error occurred in API call. Bypassing message requeue {} " + e);
			throw new AmqpRejectAndDontRequeueException(e);
		}
	}

	private void putIntoDead(Message failedMessage) {
		logger.info("Retries exeeded putting into dead-letter");
		this.rabbitTemplate.convertAndSend(Constants.DEAD_LETTER_EXCHANGE_NAME, Constants.DEAD_LETTER_QUEUE_NAME, failedMessage);
	}

}
