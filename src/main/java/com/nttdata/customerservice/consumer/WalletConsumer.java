package com.nttdata.customerservice.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.nttdata.customerservice.entity.Customer;
import com.nttdata.customerservice.entity.TypeCustomer;
import com.nttdata.customerservice.service.CustomerService;
import com.nttdata.wallet.model.CustomerWallet;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class WalletConsumer {
	@Autowired
	CustomerService customerService;
	@Autowired
	KafkaTemplate<String, CustomerWallet> kafkaTemplate;
	@Value("${api.kafka-uri.customer-topic-respose}")
	private String customerTopicRespose;

	/** Registrando un customer desde wallet */
	@KafkaListener(topics = "${api.kafka-uri.customer-topic}", groupId = "group_id")
	public void walletCustomer(CustomerWallet customerWallet) {
		Customer customer = new Customer();
		customer.setPhoneNumber(customerWallet.getPhone_number());
		customer.setTypeCustomer(TypeCustomer.personal);
		customer.setTypeDocument(customerWallet.getTypeDocument());		
		customer.setDocumentNumber(customerWallet.getDocumentNumber());
		customer.setEmailAddress(customerWallet.getEmail_address());
		log.info("customer[findByOne]:" + customer.toString());
		customer = this.customerService.findByOne(customer).blockOptional().orElse(null);	 
		if (customer == null) {
			customer = new Customer();
			customer.setDocumentNumber(customerWallet.getDocumentNumber());
			customer.setEmailAddress(customerWallet.getEmail_address());
			customer.setImeiPhone(customerWallet.getImeiPhone());
			customer.setPhoneNumber(customerWallet.getPhone_number());
			customer.setTypeDocument(customerWallet.getTypeDocument());
			customer.setTypeCustomer(TypeCustomer.personal);
			customer.setFirstname(customerWallet.getFirstname());
			customer.setLastname(customerWallet.getLastname());
			customer = this.customerService.save(customer).blockOptional().get();
			customerWallet.setIdCustomer(customer.getIdCustomer());
			log.info("WalletConsumer[save]:" + customerWallet.toString());
		} else {
			customerWallet.setIdCustomer(customer.getIdCustomer());
			log.info("WalletConsumer[find]:" + customerWallet.toString());
			log.info("customer[find]:" + customer.toString());
		}
		log.info("Send kafka:" + customerTopicRespose);
		kafkaTemplate.send(customerTopicRespose, customerWallet);
	}
}
