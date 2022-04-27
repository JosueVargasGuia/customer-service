package com.nttdata.customerservice.service.impl;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nttdata.customerservice.FeignClient.TableIdFeignClient;
import com.nttdata.customerservice.entity.Customer;
import com.nttdata.customerservice.repository.CustomerRepository;
import com.nttdata.customerservice.service.CustomerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository repository;

	@Value("${api.tableId-service.uri}")
	String tableIdService;

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	TableIdFeignClient tableIdFeignClient;

	@Override
	public Flux<Customer> findAll() {
		return repository.findAll();
	}

	@Override
	public Mono<Customer> findById(Long idCustomer) {
		return repository.findById(idCustomer);
	}

	@Override
	public Mono<Customer> save(Customer customer) {

		Long key = generateKey(Customer.class.getSimpleName());
		if (key >= 1) {
			customer.setIdCustomer(key);
			customer.setCreationDate(Calendar.getInstance().getTime());
			// log.info("SAVE[product]:"+customer.toString());
		} else {
			return Mono.error(new InterruptedException("Servicio no disponible:" + Customer.class.getSimpleName()));
		}
		return repository.save(customer);
	}

	@Override
	public Mono<Customer> update(Customer customer) {		
		customer.setDateModified(Calendar.getInstance().getTime());
		return repository.save(customer);
	}

	@Override
	public Mono<Void> delete(Long idCustomer) {
		return repository.deleteById(idCustomer);

	}

	@Override
	public Long generateKey(String nameTable) {
		// log.info(tableIdService + "/generateKey/" + nameTable);
		/*
		 * ResponseEntity<Long> responseGet = restTemplate.exchange(tableIdService +
		 * "/generateKey/" + nameTable, HttpMethod.GET, null, new
		 * ParameterizedTypeReference<Long>() { }); if (responseGet.getStatusCode() ==
		 * HttpStatus.OK) { //log.info("Body:"+ responseGet.getBody()); return
		 * responseGet.getBody(); } else { return Long.valueOf(0); }
		 */
		return tableIdFeignClient.generateKey(nameTable);
	}
}
