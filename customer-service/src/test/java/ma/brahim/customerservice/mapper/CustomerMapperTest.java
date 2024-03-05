package ma.brahim.customerservice.mapper;

import ma.brahim.customerservice.dto.CustomerDTO;
import ma.brahim.customerservice.entities.Customer;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    CustomerMapper underTest= new CustomerMapper();

    public void shouldMapCustomerToCustomerDTO(){
        Customer customer= Customer.builder().id(1L).firstName("Ass").lastName("Ho").email("ass@ho.com").build();

        CustomerDTO expect= CustomerDTO.builder().id(1L).firstName("Ass").lastName("Ho").email("ass@ho.com").build();

        CustomerDTO result= underTest.fromCustomer(customer);


    }

}