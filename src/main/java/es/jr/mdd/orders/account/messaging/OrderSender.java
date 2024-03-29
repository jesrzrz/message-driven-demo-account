package es.jr.mdd.orders.account.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import es.jr.mdd.orders.model.Order;

@Service
public class OrderSender {

	@Autowired
	private Source source;
	
	public boolean send(Order order) {
        return this.source.output().send(MessageBuilder.withPayload(order).build());
   }
}
