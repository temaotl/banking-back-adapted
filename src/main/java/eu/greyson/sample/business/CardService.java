package eu.greyson.sample.business;

import eu.greyson.sample.data.dto.card.CardBlockDTO;
import eu.greyson.sample.data.dto.card.CardDTO;
import eu.greyson.sample.data.dto.card.CardReadDTO;
import eu.greyson.sample.data.dto.card.converters.CardToDtoConverter;
import eu.greyson.sample.data.dto.card.converters.CardToReadDtoConverter;
import eu.greyson.sample.data.dto.card.converters.DtoToCardConverter;
import eu.greyson.sample.data.entity.Card;
import eu.greyson.sample.data.repository.CardRepository;
import org.springframework.stereotype.Service;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
/**
 * Service class for card management. Extends the generic AbstractCrudService class
 * to provide operations specifically for Card entities.
 */
@Service
public class CardService extends  AbstractCrudService<CardDTO,Long, Card, CardRepository>  {

    private final CardToReadDtoConverter toReadEntityConverter;

    private static final String CARD_NOT_FOUND_MESSAGE = "Card not found with id ";

    protected CardService(CardToReadDtoConverter toReadEntityConverter,
                          CardRepository repository,
                          DtoToCardConverter toEntityConverter,
                         CardToDtoConverter toDtoConverter) {
        super(repository, toEntityConverter, toDtoConverter);
        this.toReadEntityConverter = toReadEntityConverter;
    }

    @Override
    @Transactional
    public void update(CardDTO cardDTO, Long id) {
        Card existingCard = repository.findById(id)
                .orElseThrow(() -> new RuntimeException(CARD_NOT_FOUND_MESSAGE + id));

        Card updatedCard = toEntityConverter.apply(cardDTO);
        updatedCard.setId(existingCard.getId());
        repository.save(updatedCard);
    }

    /**
     * Retrieves all card entities associated with a given account ID and returns them as DTOs.
     * @param accountId the key of the account whose cards are to be retrieved.
     * @return A list of CardReadDTOs for the specified  accountId.
     */
    public List<CardReadDTO> findAllByAccountId(Long accountId) {
        List<Card> cards = repository.findByAccountId(accountId);
        return cards.stream()
                .map(toReadEntityConverter)
                .toList();
    }
    /**
     * Retrieves a specific card by its ID and associated account ID.
     * @param cardId    ID of the card to be retrieved.
     * @param accountId ID of the account associated with the card.
     * @return The CardReadDTO of the specified card or throws EntityNotFoundException if the card is not found.
     */
    public  CardReadDTO findByAccountIdAndCardId(Long cardId, Long accountId)
    {
        Optional<Card> card = repository.findByIdAndAccountId(cardId,accountId);
        if (card.isEmpty()) {
            throw new EntityNotFoundException(CARD_NOT_FOUND_MESSAGE + cardId + " for account " + accountId);
        }
        return  toReadEntityConverter.apply(card.get());
    }
    /**
     *  Update (block or unblock) card status
     * @param accountId    ID of the account associated with the card.
     * @param cardId       ID of the card to update.
     * @param cardBlockDTO DTO containing the new status of the card.
     * @return The updated CardReadDTO of the card.
     */
    @Transactional
    public CardReadDTO updateCardStatus(Long accountId, Long cardId, CardBlockDTO cardBlockDTO) {
        Card card = repository.findByIdAndAccountId(cardId, accountId)
                .orElseThrow(() -> new EntityNotFoundException(CARD_NOT_FOUND_MESSAGE + cardId + " for account " + accountId));

        card.setBlocked(cardBlockDTO.getBlocked());
        card.setDateLocked(card.isBlocked() ? LocalDate.now() : null);

        repository.save(card);
        return toReadEntityConverter.apply(card);
    }
}
