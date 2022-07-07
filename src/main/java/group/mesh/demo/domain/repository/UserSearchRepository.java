package group.mesh.demo.domain.repository;

import group.mesh.demo.domain.dao.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface UserSearchRepository extends PagingAndSortingRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.dateOfBirth >= :dateOfBirth")
    Page<User> findByDateOfBirth(LocalDate dateOfBirth, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.phoneData p WHERE p.phone = :phone")
    Page<User> findByPhone(String phone, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.emailData e WHERE e.email = :email")
    Page<User> findByEmail(String email, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.name LIKE CONCAT(:name, '%')")
    Page<User> findByName(String name, Pageable pageable);
}
