package group.mesh.demo.service;

import group.mesh.demo.domain.dao.User;
import group.mesh.demo.domain.repository.UserSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserSearchService {

    private final UserSearchRepository userSearchRepository;

    @Cacheable(value = "birthdate_users", key = "#birthDate")
    public Page<User> findByBirthDate(LocalDate birthDate, Pageable pageable) {
        return userSearchRepository.findByDateOfBirth(birthDate, pageable);
    }

    @Cacheable(value = "phone_users", key = "#phone")
    public Page<User> findByPhone(String phone, Pageable pageable) {
        return userSearchRepository.findByPhone(phone, pageable);
    }

    @Cacheable(value = "email_users", key = "#email")
    public Page<User> findByEmail(String email, Pageable pageable) {
        return userSearchRepository.findByEmail(email, pageable);
    }

    @Cacheable(value = "name_users", key = "#name")
    public Page<User> findByName(String name, Pageable pageable) {
        return userSearchRepository.findByName(name, pageable);
    }
}
