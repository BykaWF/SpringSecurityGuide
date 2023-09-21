package Guide.SecuritySpring.contoller;

import Guide.SecuritySpring.dto.CreateCustomerRequest;
import Guide.SecuritySpring.model.Customer;
import Guide.SecuritySpring.repository.CustomerRepository;
import Guide.SecuritySpring.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("management/api/")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<Customer> createCustomer(@RequestBody CreateCustomerRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());

        return ResponseEntity.ok(customerService.createCustomer(request.toCustomer()));
    }

    @GetMapping("/getCustomers")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER_TRAINEE')")
    public List<Customer> getAllUsers() {
        return customerService.getCustomers();
    }

}
