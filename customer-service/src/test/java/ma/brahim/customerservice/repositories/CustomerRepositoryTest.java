package ma.brahim.customerservice.repositories;

import ma.brahim.customerservice.entities.Customer;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp(){
        customerRepository.save(Customer.builder().firstName("Med").lastName("Moha").email("med@med.com").build());
        customerRepository.save(Customer.builder().firstName("Aed").lastName("Aoha").email("aed@aed.com").build());
        customerRepository.save(Customer.builder().firstName("Mer").lastName("Mona").email("mer@mona.com").build());

    }
    @Test
    public void shouldFindCustomerByEmail(){

        String givenEmail="aed@aed.com";
        Optional<Customer> result = customerRepository.findByEmail(givenEmail);
        AssertionsForClassTypes.assertThat(result).isPresent();
    }
    @Test
    public void shouldNotFindCustomerByEmail(){

        String givenEmail="xxx@xxx.com";
        Optional<Customer> result = customerRepository.findByEmail(givenEmail);
        AssertionsForClassTypes.assertThat(result).isEmpty();
    }


}