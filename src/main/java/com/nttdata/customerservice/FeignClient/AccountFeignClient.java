package com.nttdata.customerservice.FeignClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nttdata.customerservice.FeignClient.FallBackImpl.AccountFeignClientFallBack;
import com.nttdata.customerservice.model.ConsolidatedCustomerProducts;

@FeignClient(name = "${api.account-service.uri}", fallback = AccountFeignClientFallBack.class)
public interface AccountFeignClient {

	@GetMapping("/findProductByIdCustomer/{idCustomer}")
	List<ConsolidatedCustomerProducts> findProductByIdCustomer(@PathVariable("idCustomer") Long idCustomer);

}
