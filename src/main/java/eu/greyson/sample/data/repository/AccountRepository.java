package eu.greyson.sample.data.repository;

import eu.greyson.sample.data.entity.Account;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    List<Account> findByUserId(Long userId);
    Optional<Account> findByIban(String iban);

    boolean existsByIban(String iban);
}