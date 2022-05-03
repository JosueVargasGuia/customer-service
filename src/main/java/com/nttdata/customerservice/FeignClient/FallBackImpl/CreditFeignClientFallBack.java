package com.nttdata.customerservice.FeignClient.FallBackImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nttdata.customerservice.FeignClient.CreditFeignClient;
import com.nttdata.customerservice.model.ConsolidatedCustomerProducts;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CreditFeignClientFallBack implements CreditFeignClient {
	@Value("${api.credit-service.uri}")
	private String service;

	public List<ConsolidatedCustomerProducts> findProductByIdCustomer(Long idCustomer) {
		// TODO Auto-generated method stub
		log.info(service + "/findProductByIdCustomer/" + idCustomer);
		return new ArrayList<>();
	}
}
