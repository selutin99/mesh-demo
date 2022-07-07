package group.mesh.demo.service.security;

import group.mesh.demo.domain.dao.User;
import group.mesh.demo.domain.repository.EmailDataRepository;
import group.mesh.demo.domain.repository.PhoneDataRepository;
import group.mesh.demo.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final PhoneDataRepository phoneDataRepository;
    private final EmailDataRepository emailDataRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userData) throws UsernameNotFoundException {
        User user = findUser(userData);
        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getId()),
                user.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority("user")));
    }

    private User findUser(String userData) {
        Long userId = phoneDataRepository.findUserIdByPhone(userData) != null
                ? phoneDataRepository.findUserIdByPhone(userData)
                : emailDataRepository.findUserIdByEmail(userData);
        if (userId != null) {
            return userRepository.findById(userId).get();
        }
        throw new UsernameNotFoundException("User not found");
    }
}