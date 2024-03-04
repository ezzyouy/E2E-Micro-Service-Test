package ma.brahim.customerservice;

import ma.brahim.customerservice.entities.Customer;
import ma.brahim.customerservice.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

	@Bean
	@Profile("!test")
	CommandLineRunner start(CustomerRepository customerRepository){
		return args -> {
			customerRepository.save(Customer.builder().firstName("Med").lastName("Moha").email("med@med.com").build());
			customerRepository.save(Customer.builder().firstName("Aed").lastName("Aoha").email("aed@aed.com").build());
			customerRepository.save(Customer.builder().firstName("Mer").lastName("Mona").email("mer@mona.com").build());
		};
	}
}
