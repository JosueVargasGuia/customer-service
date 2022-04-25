package com.nttdata.customerservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.customerservice.entity.Customer;
import com.nttdata.customerservice.service.CustomerService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private CustomerService service;
	
	@GetMapping
	public Flux<Customer> findAll(){
		return service.findAll();
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Customer>> findById(@PathVariable("id") Long id){
		return service.findById(id).map(_customer -> ResponseEntity.ok().body(_customer))
				.onErrorResume(e -> {
					log.info("Error:" + e.getMessage());
					return Mono.just(ResponseEntity.badRequest().build());
				}).defaultIfEmpty(ResponseEntity.noContent().build());
	}
	
	@PostMapping
	public Mono<ResponseEntity<Customer>> saveCustomer(@RequestBody Customer customer){
		return service.save(customer).map(_customer -> ResponseEntity.ok().body(_customer)).onErrorResume(e -> {
			log.info("Error:" + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
		});
	}
	
	@PutMapping
	public Mono<ResponseEntity<Customer>> updateCustomer(@RequestBody Customer customer){
		Mono<Customer> objCustomer = service.findById(customer.getId()).flatMap(_customer -> {
			log.info("Update: [new] " + customer + " [Old]: " + _customer);
			return service.update(customer);
		});

		return objCustomer.map(_cust -> {
			log.info("Status: " + HttpStatus.OK);
			return ResponseEntity.ok().body(_cust);
		}).onErrorResume(e -> {
			log.info("Status: " + HttpStatus.BAD_REQUEST + " Message:  " + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
		}).defaultIfEmpty(ResponseEntity.noContent().build());
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable("id") Long id){
		return service.findById(id).flatMap(customer -> {
			return service.delete(customer.getId()).then(Mono.just(ResponseEntity.ok().build()));
		});
	}
	

}