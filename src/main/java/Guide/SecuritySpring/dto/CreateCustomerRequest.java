package Guide.SecuritySpring.dto;

import Guide.SecuritySpring.model.Address;
import Guide.SecuritySpring.model.Customer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCustomerRequest {
    private String username;
    private String email;

    private Address address;

    public Customer toCustomer() {

        Address address1 = Address.builder()
                .addressType(address.getAddressType())
                .city(address.getCity())
                .build();

        return Customer.builder()
                .username(this.username)
                .email(this.email)
                .address(address1)
                .build();
    }
}
