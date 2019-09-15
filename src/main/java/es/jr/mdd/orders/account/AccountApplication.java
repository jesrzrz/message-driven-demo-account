package es.jr.mdd.orders.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.jr.mdd.orders.account.model.Account;
import es.jr.mdd.orders.account.repository.AccountRepository;
import es.jr.mdd.orders.account.service.AccountService;
import es.jr.mdd.orders.model.Order;
import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@EnableBinding(Processor.class)
@Log4j2
public class AccountApplication {

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	AccountService service;

	public static void main(String[] args) {
		SpringApplication.run(AccountApplication.class, args);
	}

	@StreamListener(Processor.INPUT)
	public void receiveOrder(Order order) throws JsonProcessingException {
		log.info("Order received: {}", mapper.writeValueAsString(order));
		service.process(order);
	}

	@Bean
	AccountRepository repository() {
		AccountRepository repository = new AccountRepository();
		repository.add(new Account("1234567890", 50000, 1L));
		repository.add(new Account("1234567891", 50000, 1L));
		repository.add(new Account("1234567892", 0, 1L));
		repository.add(new Account("1234567893", 50000, 2L));
		repository.add(new Account("1234567894", 0, 2L));
		repository.add(new Account("1234567895", 50000, 2L));
		repository.add(new Account("1234567896", 0, 3L));
		repository.add(new Account("1234567897", 50000, 3L));
		repository.add(new Account("1234567898", 50000, 3L));
		return repository;
	}

//	@Bean
//	public Sampler defaultSampler() {
//		return new AlwaysSampler();
//	}
}
