package ma.brahim.customerservice.services;

import ma.brahim.customerservice.dto.CustomerDTO;
import ma.brahim.customerservice.entities.Customer;
import ma.brahim.customerservice.exception.CustomerNotFoundException;
import ma.brahim.customerservice.exception.EmailAlreadyExistException;
import ma.brahim.customerservice.mapper.CustomerMapper;
import ma.brahim.customerservice.repositories.CustomerRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerServiceImpl underTest;


    @Test
    public void shouldSaveNewCustomer(){
        CustomerDTO customerDTO= CustomerDTO.builder().firstName("sal").lastName("san").email("sal@san.com").build();
        Customer customer= Customer.builder().firstName("sal").lastName("san").email("sal@san.com").build();
        Customer customerSaved= Customer.builder().id(1L).firstName("sal").lastName("san").email("sal@san.com").build();
        CustomerDTO expected= CustomerDTO.builder().id(1L).firstName("sal").lastName("san").email("sal@san.com").build();

        Mockito.when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(Optional.empty());
        Mockito.when(customerMapper.fromCustomerDTO(customerDTO)).thenReturn(customer);
        Mockito.when(customerRepository.save(customer)).thenReturn(customerSaved);
        Mockito.when(customerMapper.fromCustomer(customerSaved)).thenReturn(expected);

        CustomerDTO result= underTest.save(customerDTO);

        assertThat(result).isNotNull();
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    public void shouldNotSaveNewCustomerWhenEmailExist(){
        CustomerDTO customerDTO=CustomerDTO.builder().firstName("sal").lastName("san").email("xx@xx.com").build();

        Customer customer= Customer.builder().id(5L).firstName("sal").lastName("san").email("xx@xx.com").build();

        Mockito.when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(Optional.of(customer));
        assertThatThrownBy(()->underTest.save(customerDTO)).isInstanceOf(EmailAlreadyExistException.class);
    }

    @Test
    public void shouldGetAllCustomers(){
        List<Customer> customers=List.of(
                Customer.builder().firstName("sal").lastName("san").email("sal@san.com").build(),
                Customer.builder().firstName("nas").lastName("kas").email("nas@kas.com").build()
        );
        List<CustomerDTO> expected=List.of(
                CustomerDTO.builder().firstName("sal").lastName("san").email("sal@san.com").build(),
                CustomerDTO.builder().firstName("nas").lastName("kas").email("nas@kas.com").build()
        );

        Mockito.when(customerRepository.findAll()).thenReturn(customers);
        Mockito.when(customerMapper.fromListCustomers(customers)).thenReturn(expected);
        List<CustomerDTO> result= underTest.getAllCustomers();
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }
    @Test
    public void shouldGetCustomerById(){
        Long id=1L;
        Customer customer= Customer.builder().id(1L).firstName("sal").lastName("san").email("sal@san.com").build();
        CustomerDTO expected= CustomerDTO.builder().id(1L).firstName("sal").lastName("san").email("sal@san.com").build();

        Mockito.when(customerRepository.findById(id)).thenReturn(Optional.ofNullable(customer));
        Mockito.when(customerMapper.fromCustomer(customer)).thenReturn(expected);

        CustomerDTO result= underTest.findCustomerById(id);
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }
    @Test
    public void shouldNotFindCustomerById(){
        Long id=8L;
        Mockito.when(customerRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(()->underTest.findCustomerById(id)).isInstanceOf(CustomerNotFoundException.class).hasMessage(null);

    }

    @Test
    public void shouldSearchCustomers(){
        String keyword="a";
        List<Customer> customers= List.of(
                Customer.builder().firstName("sal").lastName("san").email("sal@san.com").build(),
                Customer.builder().firstName("sam").lastName("ham").email("sam@ham.com").build()
        );
        List<CustomerDTO> expected= List.of(
                CustomerDTO.builder().firstName("sal").lastName("san").email("sal@san.com").build(),
                CustomerDTO.builder().firstName("sam").lastName("ham").email("sam@ham.com").build()
        );

        Mockito.when(customerRepository.findByFirstNameContainsIgnoreCase(keyword)).thenReturn(customers);
        Mockito.when(customerMapper.fromListCustomers(customers)).thenReturn(expected);

        List<CustomerDTO> result= underTest.searchCustomer(keyword);
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);

    }

    @Test
    public void updateCustomer(){
        Long customerId=6L;
        Customer customer= Customer.builder().id(6L).firstName("ahe").lastName("ezz").email("ahe@ezz.com").build();
        CustomerDTO customerDTO= CustomerDTO.builder().id(6L).firstName("ahe").lastName("ezz").email("ahe@ezz.com").build();
        Customer customerUpdated=Customer.builder().id(6L).firstName("ahe").lastName("ezz").email("ahe@ezz.com").build();
        CustomerDTO expected= CustomerDTO.builder().id(6L).firstName("ahe").lastName("ezz").email("ahe@ezz.com").build();

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(customerMapper.fromCustomerDTO(customerDTO)).thenReturn(customer);
        Mockito.when(customerRepository.save(customer)).thenReturn(customerUpdated);
        Mockito.when(customerMapper.fromCustomer(customerUpdated)).thenReturn(expected);
        CustomerDTO result= underTest.updateCustomer(customerId, customerDTO);
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);

    }

    @Test
    public void shouldDeleteCustomer(){
        Long id= 6L;
        Customer customer= Customer.builder().id(6L).firstName("ahe").lastName("ezz").email("ahe@ezz.com").build();
        Mockito.when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        underTest.deleteCustomer(id);
        Mockito.verify(customerRepository).deleteById(id);
    }
    @Test
    public void shouldNotDeleteCustomerNotEist(){
        Long id= 1L;
        Mockito.when(customerRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(()->underTest.deleteCustomer(id)).isInstanceOf(CustomerNotFoundException.class);
    }

}