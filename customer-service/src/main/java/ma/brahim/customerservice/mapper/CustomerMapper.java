package ma.brahim.customerservice.mapper;

import ma.brahim.customerservice.dto.CustomerDTO;
import ma.brahim.customerservice.entities.Customer;
import org.modelmapper.ModelMapper;

import java.util.List;

public class CustomerMapper {

    private ModelMapper modelMapper= new ModelMapper();


    public CustomerDTO fromCustomer(Customer customer){
        return modelMapper.map(customer,CustomerDTO.class);
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        return modelMapper.map(customerDTO,Customer.class);
    }

    public List<CustomerDTO> fromListCustomers(List<Customer> customerList){
        return customerList.stream().map(c->
            modelMapper.map(c, CustomerDTO.class)
        ).toList();
    }
}
