package ma.brahim.customerservice.mapper;

import ma.brahim.customerservice.dto.CustomerDTO;
import ma.brahim.customerservice.entities.Customer;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    CustomerMapper underTest= new CustomerMapper();

    @Test
    public void shouldMapCustomerToCustomerDTO(){
        Customer customer= Customer.builder().id(1L).firstName("Ass").lastName("Ho").email("ass@ho.com").build();

        CustomerDTO expect= CustomerDTO.builder().id(1L).firstName("Ass").lastName("Ho").email("ass@ho.com").build();

        CustomerDTO result= underTest.fromCustomer(customer);

        assertThat(result).isNotNull();
        assertThat(expect).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    public void shouldMapCustomerDTOToCustomer(){
        CustomerDTO customerDTO= CustomerDTO.builder().id(1L).firstName("Ass").lastName("Ho").email("ass@ho.com").build();
        Customer expect= Customer.builder().id(1L).firstName("Ass").lastName("Ho").email("ass@ho.com").build();

        Customer result= underTest.fromCustomerDTO(customerDTO);

        assertThat(result).isNotNull();
        assertThat(expect).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    public void shouldMapListOfCustomerToListOfCustomerDTO(){
        List<Customer> customerList= List.of(
                Customer.builder().id(1L).firstName("Ass").lastName("Ho").email("ass@ho.com").build(),
                Customer.builder().id(1L).firstName("Oss").lastName("Bo").email("oss@bo.com").build(),
                Customer.builder().id(1L).firstName("Iss").lastName("Ro").email("iss@ro.com").build()
        );

        List<CustomerDTO> expect= List.of(
                CustomerDTO.builder().id(1L).firstName("Ass").lastName("Ho").email("ass@ho.com").build(),
                CustomerDTO.builder().id(1L).firstName("Oss").lastName("Bo").email("oss@bo.com").build(),
                CustomerDTO.builder().id(1L).firstName("Iss").lastName("Ro").email("iss@ro.com").build()
        );

        List<CustomerDTO> result= underTest.fromListCustomers(customerList);

        assertThat(result).isNotNull();
        assertThat(expect).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    public void shouldNotMapNullCustomerToCustomerDTO(){
        Customer customer= null;

        CustomerDTO expect= CustomerDTO.builder().id(1L).firstName("Ass").lastName("Ho").email("ass@ho.com").build();

        assertThatThrownBy(()->underTest.fromCustomer(customer)).isInstanceOf(IllegalArgumentException.class);
    }

}