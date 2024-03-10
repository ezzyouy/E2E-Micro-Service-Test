package ma.brahim.customerservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.brahim.customerservice.dto.CustomerDTO;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class CustomerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Container
    @ServiceConnection
    private static PostgreSQLContainer postgreSQLContainer=new PostgreSQLContainer("postgres:16");

    List<CustomerDTO> customers;

    @BeforeEach
    void setUp(){
        customers=List.of(
                CustomerDTO.builder().id(1L).firstName("Ahmed").lastName("Ezzyouy").email("ahmed@ezzyouy.com").build(),
                CustomerDTO.builder().id(1L).firstName("Yacine").lastName("Ezzyouy").email("yacine@ezzyouy.com").build(),
                CustomerDTO.builder().id(1L).firstName("Said").lastName("Ezzyouy").email("said@ezzyouy.com").build()
        );
    }

    @Test
    public void shouldGetAllCustomers(){
        ResponseEntity<CustomerDTO[]> response= testRestTemplate.exchange("/api/customers", HttpMethod.GET, null,CustomerDTO[].class);
        List<Object> content= Arrays.asList(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(content.size()).isEqualTo(3);
        assertThat(content).usingRecursiveComparison().isEqualTo(customers);
    }

    @Test
    public void shouldSearchCustomerByFirstName(){
        String keyword="z";
        ResponseEntity<CustomerDTO[]> response=testRestTemplate.exchange("/api/customers/search?keyword="+keyword, HttpMethod.GET,null,CustomerDTO[].class);
        List<Object> content=Arrays.asList(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(content.size()).isEqualTo(3);
        List<CustomerDTO> expected=customers.stream().filter(c->c.getFirstName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
        assertThat(content).usingRecursiveComparison().isEqualTo(expected);
    }
    @Test
    public void shouldGetCustomerById(){
        Long id=1L;
        ResponseEntity<CustomerDTO> response= testRestTemplate.exchange("/api/customer/"+id, HttpMethod.GET, null, CustomerDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(customers.get(0));
    }
    @Test
    @Rollback
    void shouldSaveValidCustomer(){
        CustomerDTO customerDTO=CustomerDTO.builder().firstName("Amel").lastName("Salane").email("amel@salane.com").build();
        ResponseEntity<CustomerDTO> response=testRestTemplate.exchange("/api/customers", HttpMethod.POST,new HttpEntity<>(customerDTO), CustomerDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(customerDTO);
    }
    @Test
    @Rollback
    void shouldNotSaveInValidCustomer() throws JsonProcessingException {
        CustomerDTO customerDTO=CustomerDTO.builder().firstName("").lastName("").email("").build();
        ResponseEntity<String> response=testRestTemplate.exchange("/api/customers", HttpMethod.POST,new HttpEntity<>(customerDTO), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, ArrayList<String>> errors=objectMapper.readValue(response.getBody(), HashMap.class);
        assertThat(errors.keySet().size()).isEqualTo(3);
        assertThat(errors.get("firstName").size()).isEqualTo(2);
        assertThat(errors.get("lastName").size()).isEqualTo(2);
        assertThat(errors.get("email").size()).isEqualTo(2);
    }
    @Test
    @Rollback
    void shouldUpdateValidCustomer() {
        Long customerId=2L;
        CustomerDTO customerDTO=CustomerDTO.builder().id(2L).firstName("Hanae").lastName("Bene").email("hanae@ben.com").build();
        ResponseEntity<CustomerDTO> response=testRestTemplate.exchange("/api/customer/"+customerId, HttpMethod.PUT,new HttpEntity<>(customerDTO), CustomerDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(customerDTO);
    }


}
