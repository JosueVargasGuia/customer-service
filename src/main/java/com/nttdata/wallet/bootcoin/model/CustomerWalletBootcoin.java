package com.nttdata.wallet.bootcoin.model;

import java.io.Serializable;

import com.nttdata.customerservice.entity.TypeDocument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CustomerWalletBootcoin implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idWalletBootcoin;
	private Long idCustomer;
	private String firstname;
	private String lastname;
	private TypeDocument typeDocument;
	private String documentNumber;
	private String email;
	private String phoneNumber;
}
