package group.mesh.demo.domain.repository;

import group.mesh.demo.domain.dao.PhoneData;
import group.mesh.demo.domain.dao.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneDataRepository extends CrudRepository<PhoneData, Long> {

    List<PhoneData> findAllByUser(User user);

    PhoneData findByPhone(String phone);

    @Query(value = "SELECT coalesce(max(p.id), 0) + 1 FROM PhoneData p")
    Long generateNewId();

    @Query(value = "SELECT p.user.id FROM PhoneData p WHERE p.phone=:phone")
    Long findUserIdByPhone(String phone);
}
