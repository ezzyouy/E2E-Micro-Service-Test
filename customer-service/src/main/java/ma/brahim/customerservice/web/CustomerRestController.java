package ma.brahim.customerservice.web;

import jakarta.validation.Valid;
import ma.brahim.customerservice.dto.CustomerDTO;
import ma.brahim.customerservice.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

    private CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public List<CustomerDTO> getAllCustomer(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/customer/{id}")
    public CustomerDTO getCustomer(@PathVariable Long id){
        return customerService.findCustomerById(id);
    }

    @GetMapping("/customer/search")
    public List<CustomerDTO> searchCustomer(@RequestParam String keyword){
        return customerService.searchCustomer(keyword);
    }
    @PostMapping("/customer")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO saveCustomer(@RequestBody @Valid CustomerDTO customerDTO){
        return customerService.save(customerDTO);
    }

    @PutMapping("/customer/{id}")
    public CustomerDTO updateustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO){
        return customerService.updateCustomer(id, customerDTO);
    }

    @DeleteMapping("/customer/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCustomer(@PathVariable Long id){
         customerService.deleteCustomer(id);
    }
}
