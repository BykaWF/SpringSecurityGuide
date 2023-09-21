package Guide.SecuritySpring.repository;

import Guide.SecuritySpring.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    void deleteByUsername(String username);

}
