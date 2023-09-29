package Guide.SecuritySpring.dto;

import Guide.SecuritySpring.model.Address;
import Guide.SecuritySpring.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {

    private String username;
    private String email;
    private String password;
    private Address address;
    private String roles;

    public User toUser(PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode(this.password);

        Address address1 = Address.builder()
                .addressType(address.getAddressType())
                .city(address.getCity())
                .build();

        return User.builder()
                .username(this.username)
                .email(this.email)
                .password(encodedPassword)
                .roles(this.roles)
                .address(address1)
                .build();
    }
}
