package Guide.SecuritySpring.service;


import Guide.SecuritySpring.model.User;

import Guide.SecuritySpring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    public void createUser(User customer) {
        userRepository.save(customer);
    }
    public User getUserById(Long userId) {
        return userRepository.findByUserId(userId);
    }
}
