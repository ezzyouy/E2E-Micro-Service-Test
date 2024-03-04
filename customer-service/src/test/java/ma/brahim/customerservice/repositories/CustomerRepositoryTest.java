package ma.brahim.customerservice.repositories;

import ma.brahim.customerservice.entities.Customer;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

// annotation for containers
//@Testcontainers
@ActiveProfiles("test")
@DataJpaTest
//add this annotation when test containers
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest {

    //@Container
    //@ServiceConnection
    //private static PostgreSQLContainer postgreSQLContainer= new PostgreSQLContainer("Postgres:16");
    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp(){
        customerRepository.save(Customer.builder().firstName("Med").lastName("Moha").email("med@med.com").build());
        customerRepository.save(Customer.builder().firstName("Aed").lastName("Aoha").email("aed@aed.com").build());
        customerRepository.save(Customer.builder().firstName("Mer").lastName("Mona").email("mer@mona.com").build());

    }

    //@Test
    public void connectionEstablishedTest(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }
    @Test
    public void shouldFindCustomerByEmail(){

        String givenEmail="aed@aed.com";
        Optional<Customer> result = customerRepository.findByEmail(givenEmail);
        assertThat(result).isPresent();
    }
    @Test
    public void shouldNotFindCustomerByEmail(){

        String givenEmail="xxx@xxx.com";
        Optional<Customer> result = customerRepository.findByEmail(givenEmail);
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldFindCustomerByFirstName(){
        String keyword="M";
        List<Customer> expected=List.of(
                Customer.builder().firstName("Med").lastName("Moha").email("med@med.com").build(),
                Customer.builder().firstName("Mer").lastName("Mona").email("mer@mona.com").build() );
        List<Customer> result= customerRepository.findByFirstNameContainsIgnoreCase(keyword);
        System.out.println(result);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(expected.size());
        assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);

    }


}