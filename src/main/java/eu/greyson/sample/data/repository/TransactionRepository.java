package eu.greyson.sample.data.repository;

import eu.greyson.sample.data.entity.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction,Long> {
    List<Transaction> findByCreditorOrDebtor(String creditorIban, String debtorIban);
}
