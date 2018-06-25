package com.msl.common.utilities.message.rabbit;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class RabbitExpirationProcessor implements MessagePostProcessor {

	private Long time;
	/**
	 * @fieldName SH_ZONE
	 * @fieldType ZoneId
	 * @Description SH_ZONE
	 */
	private static final ZoneId SH_ZONE = ZoneId.of("Asia/Shanghai");

	public RabbitExpirationProcessor(Long notificationTime) {
		this.time = notificationTime;
	}

	@Override
	public Message postProcessMessage(Message arg0) throws AmqpException {
		Long nowTime = Timestamp.valueOf(LocalDateTime.now(SH_ZONE)).getTime();
		Long ttl = Math.max(2000, (time * 1000 - nowTime));//Expiration min 2000ms
		arg0.getMessageProperties().setExpiration(ttl.toString());
		return arg0;
	}

}
