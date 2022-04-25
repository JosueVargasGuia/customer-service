package com.nttdata.customerservice.service;


import com.nttdata.customerservice.entity.Customer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
public interface CustomerService {
	

	Flux<Customer> findAll();
	Mono<Customer> save(Customer customer);
	Mono<Customer> update(Customer customer);
	Mono<Customer> findById(Long id);
	Mono<Void> delete(Long id);

	Long generateKey(String nameTable);

}
