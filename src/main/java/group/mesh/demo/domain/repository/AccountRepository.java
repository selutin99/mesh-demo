package group.mesh.demo.domain.repository;

import group.mesh.demo.domain.dao.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
}
