package ma.brahim.customerservice.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.brahim.customerservice.dto.CustomerDTO;
import ma.brahim.customerservice.entities.Customer;
import ma.brahim.customerservice.exception.CustomerNotFoundException;
import ma.brahim.customerservice.exception.EmailAlreadyExistException;
import ma.brahim.customerservice.mapper.CustomerMapper;
import ma.brahim.customerservice.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private CustomerMapper customerMapper;
    private CustomerRepository customerRepository;

    @Override
    public CustomerDTO save(CustomerDTO customerDTO) throws EmailAlreadyExistException {
        log.info("Save new customer => %s", customerDTO.toString());
        Optional<Customer> byEmail=customerRepository.findByEmail(customerDTO.getEmail());
        if(byEmail.isPresent()){
            log.error(String.format("this email %s already exist",customerDTO.getEmail()));
            throw  new EmailAlreadyExistException();
        }
        Customer customerToSave= customerMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer= customerRepository.save(customerToSave);
        CustomerDTO result= customerMapper.fromCustomer(savedCustomer);
        return result;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> allCustomers=customerRepository.findAll();
        return customerMapper.fromListCustomers(allCustomers);
    }

    @Override
    public CustomerDTO findCustomerById(Long id) throws CustomerNotFoundException {
        Optional<Customer> customer=customerRepository.findById(id);
        if(customer.isEmpty()) throw  new CustomerNotFoundException();
        return customerMapper.fromCustomer(customer.get());
    }

    @Override
    public List<CustomerDTO> searchCustomer(String keyword) {
        List<Customer> customers= customerRepository.findByFirstNameContainsIgnoreCase(keyword);
        return customerMapper.fromListCustomers(customers);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) throws CustomerNotFoundException {
        Optional<Customer> customer= customerRepository.findById(id);
        if (customer.isEmpty()) throw new CustomerNotFoundException();
        customerDTO.setId(id);
        Customer customerToUpdate=customerMapper.fromCustomerDTO(customerDTO);
        Customer updateCustomer=customerRepository.save(customerToUpdate);
        return customerMapper.fromCustomer(updateCustomer);
    }

    @Override
    public void deleteCustomer(Long id) throws CustomerNotFoundException {
        Optional<Customer> customer= customerRepository.findById(id);
        if (customer.isEmpty()) throw new CustomerNotFoundException();
        customerRepository.deleteById(id);
    }
}
