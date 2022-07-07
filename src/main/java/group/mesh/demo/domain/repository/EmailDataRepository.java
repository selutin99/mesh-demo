package group.mesh.demo.domain.repository;

import group.mesh.demo.domain.dao.EmailData;
import group.mesh.demo.domain.dao.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EmailDataRepository extends CrudRepository<EmailData, Long> {

    List<EmailData> findAllByUser(User user);

    EmailData findByEmail(String email);

    @Query(value = "SELECT coalesce(max(e.id), 0) + 1 FROM EmailData e")
    Long generateNewId();

    @Query(value = "SELECT e.user.id FROM EmailData e WHERE e.email=:email")
    Long findUserIdByEmail(String email);
}
