package ma.brahim.customerservice.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.brahim.customerservice.dto.CustomerDTO;
import ma.brahim.customerservice.exception.CustomerNotFoundException;
import ma.brahim.customerservice.services.CustomerService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@WebMvcTest(CustomerRestController.class)
class CustomerRestControllerTest {

    @MockBean
    private CustomerService customerService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    List<CustomerDTO> customers;

    @BeforeEach
    void setUp(){
        customers=List.of(
                CustomerDTO.builder().id(1L).firstName("Sal").lastName("Wan").email("sal@wan.com").build(),
                CustomerDTO.builder().id(2L).firstName("Kal").lastName("Zan").email("kal@zan.com").build(),
                CustomerDTO.builder().id(3L).firstName("Fal").lastName("Man").email("fal@man.com").build()
        );
    }

    @Test
    public void shouldGetAllCustomer() throws Exception{
        Mockito.when(customerService.getAllCustomers()).thenReturn(customers);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customers)));
    }

    @Test
    public void shouldGetCustomerById() throws Exception{
        Long givenId=2L;
        Mockito.when(customerService.findCustomerById(givenId)).thenReturn(customers.get(1));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customer/{id}",givenId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customers.get(1))));
    }
    @Test
    public void shouldNotGetCustomerByIdInvalid() throws Exception{
        Long givenId=8L;
        Mockito.when(customerService.findCustomerById(givenId)).thenThrow(CustomerNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customer/{id}",givenId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }
    @Test
    public void searchCustomer() throws Exception {
        String keyword="s";
        Mockito.when(customerService.searchCustomer(keyword)).thenReturn(customers);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customer/search?keyword="+keyword))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customers)));
    }
    @Test
    public void shouldSaveCustomer() throws  Exception{
        CustomerDTO customerDTO=customers.get(0);
        String expected= """
                {
                "id":1,"firstName":"Sal","lastName":"Wan","email":"sal@wan.com"
                }
                """;
        Mockito.when(customerService.save(Mockito.any())).thenReturn(customers.get(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }

    @Test
    public void deleteCustomer() throws Exception{
        Long givenId=1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/customer/{id}", givenId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}