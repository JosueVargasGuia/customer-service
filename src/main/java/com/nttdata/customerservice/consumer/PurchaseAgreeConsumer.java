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
public class PurchaseAgreeConsumer {
	@Autowired
	CustomerService customerService;
	@Autowired
	KafkaTemplate<String, PurchaseRequestKafka> kafkaTemplate;
	@Value("${api.kafka-uri.purchase-agree-topic-respose}")
	private String purchaseAgreeTopicRespose;

	/** Registrando un customer desde purchaseRequest */
	@KafkaListener(topics = "${api.kafka-uri.purchase-agree-topic}", groupId = "group_id")
	public void purchaseAgreeConsumer(PurchaseRequestKafka purchaseRequestKafka) {
		log.info("Mensaje recivido[purchaseAgreeConsumer]:" + purchaseRequestKafka.toString());
		Customer customer = new Customer();
		customer.setPhoneNumber(purchaseRequestKafka.getCustomerDestiny().getPhoneNumber());
		customer.setTypeCustomer(TypeCustomer.personal);
		customer.setTypeDocument(purchaseRequestKafka.getCustomerDestiny().getTypeDocument());
		customer.setDocumentNumber(purchaseRequestKafka.getCustomerDestiny().getDocumentNumber());
		customer.setEmailAddress(purchaseRequestKafka.getCustomerDestiny().getEmailAddress());
		log.info("customer[findByOne]:" + customer.toString());
		customer = this.customerService.findByOne(customer).blockOptional().orElse(null);
		if (customer == null) {
			customer = new Customer();
			customer.setDocumentNumber(purchaseRequestKafka.getCustomerDestiny().getDocumentNumber());
			customer.setEmailAddress(purchaseRequestKafka.getCustomerDestiny().getEmailAddress());
			customer.setImeiPhone(purchaseRequestKafka.getCustomerDestiny().getImeiPhone());
			customer.setPhoneNumber(purchaseRequestKafka.getCustomerDestiny().getPhoneNumber());
			customer.setTypeDocument(purchaseRequestKafka.getCustomerDestiny().getTypeDocument());
			customer.setTypeCustomer(TypeCustomer.personal);
			customer.setFirstname(purchaseRequestKafka.getCustomerDestiny().getFirstname());
			customer.setLastname(purchaseRequestKafka.getCustomerDestiny().getLastname());
			customer = this.customerService.save(customer).blockOptional().get();
			purchaseRequestKafka.setCustomerDestiny(customer);
			log.info("purchaseAgreeConsumer[save]:" + purchaseRequestKafka.toString());
		} else {
			purchaseRequestKafka.setCustomerDestiny(customer);
			log.info("purchaseAgreeConsumer[find]:" + purchaseRequestKafka.toString());
			log.info("customer[find]:" + customer.toString());
		}
		 kafkaTemplate.send(purchaseAgreeTopicRespose, purchaseRequestKafka);
	}
}
