package es.jr.mdd.orders.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.jr.mdd.orders.account.messaging.OrderSender;
import es.jr.mdd.orders.account.model.Account;
import es.jr.mdd.orders.account.repository.AccountRepository;
import es.jr.mdd.orders.model.Order;
import es.jr.mdd.orders.model.OrderStatus;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AccountService {

private ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	OrderSender orderSender;
	
	public void process(final Order order) throws JsonProcessingException {
		log.info("Order processed: {}", mapper.writeValueAsString(order));
		List<Account> accounts =  accountRepository.findByCustomer(order.getCustomerId());
		Account account = accounts.get(0);
		log.info("Account found: {}", mapper.writeValueAsString(account));
		if (order.getPrice() <= account.getBalance()) {
			order.setStatus(OrderStatus.ACCEPTED);
			account.setBalance(account.getBalance() - order.getPrice());
		} else {
			order.setStatus(OrderStatus.REJECTED);
		}
		orderSender.send(order);
		log.info("Order response sent: {}", mapper.writeValueAsString(order));
	}
}
