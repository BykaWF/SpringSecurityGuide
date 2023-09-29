package Guide.SecuritySpring.service;

import Guide.SecuritySpring.model.SecurityUser;
import Guide.SecuritySpring.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private  final UserRepository userRepository;


    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return userRepository
               .findUserByUsername(username)
               .map(SecurityUser::new)
               .orElseThrow(() -> new UsernameNotFoundException("Username not found " + username));

    }
}
