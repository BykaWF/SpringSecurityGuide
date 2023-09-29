package Guide.SecuritySpring.repository;

import Guide.SecuritySpring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    void deleteByUsername(String username);

    User findByUserId(Long userId);
    Optional<User> findUserByUsername(String username);
}
