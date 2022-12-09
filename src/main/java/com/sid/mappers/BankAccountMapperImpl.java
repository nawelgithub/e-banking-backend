package com.sid.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.sid.dtos.CustomerDTO;
import com.sid.entities.Customer;

@Service 
public class BankAccountMapperImpl {
	
	public CustomerDTO fromCustomer(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();
		BeanUtils.copyProperties(customer, customerDTO);
		//customerDTO.setId(customer.getId());
		//customerDTO.setName(customer.getEmail());
		//customerDTO.setEmail(customer.getEmail());
		return customerDTO;
	}
	
	
	public Customer fromCustomerDTO(CustomerDTO customerDTO) {
		Customer customer =new Customer();
		BeanUtils.copyProperties(customerDTO, customer);
		return customer;
	}

}
