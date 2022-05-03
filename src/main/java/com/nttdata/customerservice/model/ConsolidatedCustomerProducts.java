package com.nttdata.customerservice.model;

 
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
public class ConsolidatedCustomerProducts {
	private Long idAccount;
	private Long idCustomer;
	private TypeAccount typeAccount;
	private Long idBankAccount;
	private Long idCreditAccount;
	private Product product;
}
