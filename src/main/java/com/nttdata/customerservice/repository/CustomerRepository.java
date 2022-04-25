package com.nttdata.customerservice.repository;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.customerservice.entity.Customer;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer, Long>{

}
