package es.jr.mdd.orders.account.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.jr.mdd.orders.account.model.Account;
import es.jr.mdd.orders.account.repository.AccountRepository;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class AccountController {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	AccountRepository repository;

	@PostMapping
	public Account add(@RequestBody Account account) {
		return repository.add(account);
	}

	@PutMapping
	public Account update(@RequestBody Account account) {
		return repository.update(account);
	}

	@PutMapping("/withdraw/{id}/{amount}")
	public Account withdraw(@PathVariable("id") Long id, @PathVariable("amount") int amount) throws JsonProcessingException {
		Account account = repository.findById(id);
		log.info("Account found: {}", mapper.writeValueAsString(account));
		account.setBalance(account.getBalance() - amount);
		log.info("Current balance: {}", mapper.writeValueAsString(Collections.singletonMap("balance", account.getBalance())));
		return repository.update(account);
	}

	@GetMapping("/{id}")
	public Account findById(@PathVariable("id") Long id) {
		return repository.findById(id);
	}

	@GetMapping("/customer/{customerId}")
	public List<Account> findByCustomerId(@PathVariable("customerId") Long customerId) {
		return repository.findByCustomer(customerId);
	}

	@PostMapping("/ids")
	public List<Account> find(@RequestBody List<Long> ids) {
		return repository.find(ids);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		repository.delete(id);
	}
}
