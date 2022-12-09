package com.sid.web;

import java.nio.file.Path;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sid.services.BankAccountService;
import com.sid.dtos.CustomerDTO;
import com.sid.entities.Customer;
import com.sid.exceptions.CustomerNotFoundException;
import com.sid.repositories.BankAccountRepository;

@RestController
//@RequestMapping()
public class CustomerRestController {

	private BankAccountService  bankAccountService;

	
	public CustomerRestController(BankAccountService bankAccountService) {
		this.bankAccountService = bankAccountService;
	}


	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	
	@GetMapping("/customers")
	public List<CustomerDTO> customers(){
		return bankAccountService.listCustomer();
	}
	
	@GetMapping("/customers/{id}")
	public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
	return bankAccountService.getCustomer(customerId);	
	}
	
	@PostMapping("/customers")
	public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
		
	return	bankAccountService.saveCustomer(customerDTO);
	}
	
	@PutMapping("customers/{customerId}")
	public CustomerDTO updateCustomer(@PathVariable Long customerId,@RequestBody CustomerDTO customerDTO) {
		//puisque l'attribut porte le mm nom de PathVariable, on n'a pas besoin d'ecrire name=customerId
	customerDTO.setId(customerId);
	return bankAccountService.updateCustomer(customerDTO);
	}
	
	@DeleteMapping("customers/{id}")
	public void deleteCustomer(@PathVariable Long id) {
		bankAccountService.deleteCustomer(id);
	}
	
}
