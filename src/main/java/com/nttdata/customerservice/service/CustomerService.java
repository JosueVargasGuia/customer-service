package com.nttdata.customerservice.service;

 
import com.nttdata.customerservice.entity.Customer;
 
import com.nttdata.customerservice.model.ConsolidatedCustomerProducts;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

	Flux<Customer> findAll();

	Mono<Customer> save(Customer customer);

	Mono<Customer> update(Customer customer);

	Mono<Customer> findById(Long id);

	Mono<Void> delete(Long idCustomer);

	// Long generateKey(String nameTable);

	Flux<ConsolidatedCustomerProducts> summaryForProduct(Long idCustomer);

	
}
