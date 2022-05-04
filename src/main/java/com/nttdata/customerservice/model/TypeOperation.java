package com.nttdata.customerservice.model;

/**
 * tipo de operacion a realizar payment:pago withdrawal:retiro
 * thirdPartyPayment:el pago de cualquier producto de crédito de terceros
 */
public enum TypeOperation {
	payment, withdrawal, thirdPartyPayment;
}
