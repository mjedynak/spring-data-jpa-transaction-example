package pl.mjedynak.repository;

import org.springframework.data.repository.CrudRepository;
import pl.mjedynak.model.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {
}
