package com.nttdata.customerservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection="customers")
public class Customer {
	
	@Id
	private Long id;
	private String firstname;
	private String lastname;
	private String documentNumber;
	private TypeDocument typeDocument;
	private TypeCustomer typeCustomer;
	private String emailAddress;
	private String phoneNumber;
	private String homeAddress;
	
}
