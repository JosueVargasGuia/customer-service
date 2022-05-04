package com.nttdata.customerservice.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.nttdata.customerservice.FeignClient.AccountFeignClient;
import com.nttdata.customerservice.FeignClient.CreditFeignClient;
import com.nttdata.customerservice.FeignClient.TableIdFeignClient;
import com.nttdata.customerservice.entity.Customer;
import com.nttdata.customerservice.model.ConsolidatedCustomerProducts;
import com.nttdata.customerservice.repository.CustomerRepository;
import com.nttdata.customerservice.service.CustomerService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository repository;

	@Value("${api.tableId-service.uri}")
	String tableIdService;

	// @Autowired
	// RestTemplate restTemplate;
	@Autowired
	TableIdFeignClient tableIdFeignClient;

	@Autowired
	AccountFeignClient accountFeignClient;
	@Autowired
	CreditFeignClient creditFeignClient;

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
		Long count = this.findAll().collect(Collectors.counting()).blockOptional().get();
		Long idCustomer;
		if (count != null) {
			if (count <= 0) {
				idCustomer = Long.valueOf(0);
			} else {
				idCustomer = this.findAll().collect(Collectors.maxBy(Comparator.comparing(Customer::getIdCustomer)))
						.blockOptional().get().get().getIdCustomer();
			}

		} else {
			idCustomer = Long.valueOf(0);

		}
		customer.setIdCustomer(idCustomer + 1);
		customer.setCreationDate(Calendar.getInstance().getTime());
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

	// @Override
	// public Long generateKey(String nameTable) {
	// log.info(tableIdService + "/generateKey/" + nameTable);
	/*
	 * ResponseEntity<Long> responseGet = restTemplate.exchange(tableIdService +
	 * "/generateKey/" + nameTable, HttpMethod.GET, null, new
	 * ParameterizedTypeReference<Long>() { }); if (responseGet.getStatusCode() ==
	 * HttpStatus.OK) { //log.info("Body:"+ responseGet.getBody()); return
	 * responseGet.getBody(); } else { return Long.valueOf(0); }
	 */
	// return tableIdFeignClient.generateKey(nameTable);
	// }
	/**
	 * resumen consolidado de un cliente con todos los productos que pueda tener en
	 * el banco
	 */
	@Override
	public Flux<ConsolidatedCustomerProducts> summaryForProduct(Long idCustomer) {
		/** Cuentas de corrientes o productos tipo pasivos */
		List<ConsolidatedCustomerProducts> listaAccount = accountFeignClient.findProductByIdCustomer(idCustomer);
		/** Cuentas de credito o productos tipo activos */
		List<ConsolidatedCustomerProducts> listaCredit = creditFeignClient.findProductByIdCustomer(idCustomer);

		List<ConsolidatedCustomerProducts> listaConso = new ArrayList<ConsolidatedCustomerProducts>();
		listaConso.addAll(listaAccount);
		listaConso.addAll(listaCredit);
		listaConso.forEach(e -> log.info("ConsolidatedCustomerProducts:" + e.toString()));
		return Flux.fromIterable(listaConso);
	}

}
