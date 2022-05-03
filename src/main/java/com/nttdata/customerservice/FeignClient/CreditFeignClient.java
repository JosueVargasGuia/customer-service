package com.nttdata.customerservice.FeignClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nttdata.customerservice.FeignClient.FallBackImpl.CreditFeignClientFallBack;
import com.nttdata.customerservice.model.ConsolidatedCustomerProducts;

@FeignClient(name = "${api.credit-service.uri}", fallback = CreditFeignClientFallBack.class)
public interface CreditFeignClient {

	@GetMapping("/findProductByIdCustomer/{idCustomer}")
	List<ConsolidatedCustomerProducts> findProductByIdCustomer(@PathVariable("idCustomer") Long idCustomer);
}
