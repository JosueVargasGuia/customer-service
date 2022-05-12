package com.nttdata.customerservice.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.nttdata.customerservice.entity.Customer;
import com.nttdata.customerservice.entity.TypeCustomer;
import com.nttdata.customerservice.service.CustomerService;
import com.nttdata.purchaserequest.model.PurchaseRequestKafka;
import com.nttdata.wallet.model.CustomerWallet;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class PurchaseConsumer {
	@Autowired
	CustomerService customerService;
	@Autowired
	KafkaTemplate<String, PurchaseRequestKafka> kafkaTemplate;
	@Value("${api.kafka-uri.purchase-topic-respose}")
	private String purchaseTopicRespose;

	/** Registrando un customer desde purchaseRequest */
	@KafkaListener(topics = "${api.kafka-uri.purchase-topic}", groupId = "group_id")
	public void purchaseConsumer(PurchaseRequestKafka purchaseRequestKafka) {
		log.info("Mensaje recivido[purchaseConsumer]:" + purchaseRequestKafka.toString());
		Customer customer = new Customer();
		customer.setPhoneNumber(purchaseRequestKafka.getCustomerOrigin().getPhoneNumber());
		customer.setTypeCustomer(TypeCustomer.personal);
		customer.setTypeDocument(purchaseRequestKafka.getCustomerOrigin().getTypeDocument());
		customer.setDocumentNumber(purchaseRequestKafka.getCustomerOrigin().getDocumentNumber());
		customer.setEmailAddress(purchaseRequestKafka.getCustomerOrigin().getEmailAddress());
		log.info("customer[findByOne]:" + customer.toString());
		customer = this.customerService.findByOne(customer).blockOptional().orElse(null);
		if (customer == null) {
			customer = new Customer();
			customer.setDocumentNumber(purchaseRequestKafka.getCustomerOrigin().getDocumentNumber());
			customer.setEmailAddress(purchaseRequestKafka.getCustomerOrigin().getEmailAddress());
			customer.setImeiPhone(purchaseRequestKafka.getCustomerOrigin().getImeiPhone());
			customer.setPhoneNumber(purchaseRequestKafka.getCustomerOrigin().getPhoneNumber());
			customer.setTypeDocument(purchaseRequestKafka.getCustomerOrigin().getTypeDocument());
			customer.setTypeCustomer(TypeCustomer.personal);
			customer.setFirstname(purchaseRequestKafka.getCustomerOrigin().getFirstname());
			customer.setLastname(purchaseRequestKafka.getCustomerOrigin().getLastname());
			customer = this.customerService.save(customer).blockOptional().get();
			purchaseRequestKafka.setCustomerOrigin(customer);
			log.info("purchaseConsumer[save]:" + purchaseRequestKafka.toString());
		} else {
			purchaseRequestKafka.setCustomerOrigin(customer);
			log.info("purchaseConsumer[find]:" + purchaseRequestKafka.toString());
			log.info("customer[find]:" + customer.toString());
		}
		kafkaTemplate.send(purchaseTopicRespose, purchaseRequestKafka);
	}
}
