package ma.brahim.customerservice.services;

import ma.brahim.customerservice.dto.CustomerDTO;
import ma.brahim.customerservice.entities.Customer;
import ma.brahim.customerservice.exception.CustomerNotFoundException;
import ma.brahim.customerservice.exception.EmailAlreadyExistException;

import java.util.List;

public interface CustomerService {

    CustomerDTO save(CustomerDTO customerDTO) throws EmailAlreadyExistException;
    List<CustomerDTO> getAllCustomers();
    CustomerDTO findCustomerById(Long id) throws CustomerNotFoundException;
    List<CustomerDTO> searchCustomer(String keyword);
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) throws CustomerNotFoundException;
    void deleteCustomer(Long id) throws CustomerNotFoundException;
}
