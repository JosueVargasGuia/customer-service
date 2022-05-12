package com.nttdata.customerservice.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.nttdata.customerservice.entity.Customer;
import com.nttdata.customerservice.entity.TypeCustomer;
import com.nttdata.customerservice.service.CustomerService;
import com.nttdata.wallet.bootcoin.model.CustomerWalletBootcoin;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class WalletBootcoinConsumer {
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	KafkaTemplate<String, CustomerWalletBootcoin> kafkaTemplate;
	
	@Value("${api.kafka-uri.customer-topic-respose-bootcoin}")
	private String customerTopicRespose;
	
	/** Metodo para registrar un customer desde WalletBootcoin */
	@KafkaListener(topics = "${api.kafka-uri.customer-topic-bootcoin}", groupId = "group_id")
	public void walletBootcoinConsumer(CustomerWalletBootcoin customerWalletBootcoin) {
		Customer customer = new Customer();
		customer.setPhoneNumber(customerWalletBootcoin.getPhoneNumber());
		customer.setTypeCustomer(TypeCustomer.personal);
		customer.setTypeDocument(customerWalletBootcoin.getTypeDocument());		
		customer.setDocumentNumber(customerWalletBootcoin.getDocumentNumber());
		customer.setEmailAddress(customerWalletBootcoin.getEmail());
		log.info("customer[findByOne]:" + customer.toString());
		customer = this.customerService.findByOne(customer).blockOptional().orElse(null);
		if (customer == null) {
			customer = new Customer();
			customer.setDocumentNumber(customerWalletBootcoin.getDocumentNumber());
			customer.setEmailAddress(customerWalletBootcoin.getEmail());
			customer.setPhoneNumber(customerWalletBootcoin.getPhoneNumber());
			customer.setTypeDocument(customerWalletBootcoin.getTypeDocument());
			customer.setTypeCustomer(TypeCustomer.personal);
			customer.setFirstname(customerWalletBootcoin.getFirstname());
			customer.setLastname(customerWalletBootcoin.getLastname());
			customer = this.customerService.save(customer).blockOptional().get();
			customerWalletBootcoin.setIdCustomer(customer.getIdCustomer());
			log.info("CustomerWalletBootcoin[save]:" + customerWalletBootcoin.toString());
		} else {
			customerWalletBootcoin.setIdCustomer(customer.getIdCustomer());
			log.info("CustomerWalletBootcoin[find]:" + customerWalletBootcoin.toString());
			log.info("customer[find]:" + customer.toString());
		}
		log.info("Send kafka:" + customerTopicRespose);
		kafkaTemplate.send(customerTopicRespose, customerWalletBootcoin);
	}

}
