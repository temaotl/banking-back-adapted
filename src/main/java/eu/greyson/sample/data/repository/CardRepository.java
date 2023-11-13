package eu.greyson.sample.data.repository;

import eu.greyson.sample.data.entity.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends CrudRepository<Card, Long> {
    List<Card> findByAccountId(Long accountId);
    Optional<Card> findByIdAndAccountId(Long cardId, Long accountId);
}