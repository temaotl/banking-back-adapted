package eu.greyson.sample.data.dto;


import eu.greyson.sample.data.dto.card.CardDTO;
import eu.greyson.sample.data.dto.card.converters.CardToDtoConverter;
import eu.greyson.sample.data.dto.card.converters.DtoToCardConverter;
import eu.greyson.sample.data.entity.Account;
import eu.greyson.sample.data.entity.Card;
import eu.greyson.sample.data.entity.User;
import eu.greyson.sample.data.repository.AccountRepository;
import eu.greyson.sample.data.repository.CardRepository;
import eu.greyson.sample.data.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CardConvertersTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardToDtoConverter cardToDtoConverter;

    @Autowired
    private DtoToCardConverter dtoToCardConverter;

    @Test
    void testCardToDtoAndDtoToCardConversion() {

        User newUser = new User();
        newUser.setFirstName("Kirin");
        newUser.setLastName("Dorian");
        newUser.setBirthDate(LocalDate.of(1990, 5, 12));


        User savedUser = userRepository.save(newUser);


        Account newAccount = new Account();
        newAccount.setName("Savings Account");
        newAccount.setIban("CZ123456789");
        newAccount.setBalance(new BigDecimal("1000.00"));
        newAccount.setCurrency("CZK");
        newAccount.setUser(savedUser);


        Account savedAccount = accountRepository.save(newAccount);


        Card card = new Card();
        card.setName("Personal Card");
        card.setBlocked(false);
        card.setAccount(savedAccount);
        Card savedCard = cardRepository.save(card);


        CardDTO cardDTO = cardToDtoConverter.apply(savedCard);


        assertThat(cardDTO.getId()).isEqualTo(savedCard.getId());
        assertThat(cardDTO.getName()).isEqualTo(savedCard.getName());
        assertThat(cardDTO.getBlocked()).isEqualTo(savedCard.isBlocked());
        assertThat(cardDTO.getDateLocked()).isEqualTo(savedCard.getDateLocked());
        assertThat(cardDTO.getAccountId()).isEqualTo(savedAccount.getId());


        Card convertedCard = dtoToCardConverter.apply(cardDTO);


        assertThat(convertedCard.getId()).isEqualTo(cardDTO.getId());
        assertThat(convertedCard.getName()).isEqualTo(cardDTO.getName());
        assertThat(convertedCard.isBlocked()).isEqualTo(cardDTO.getBlocked());
        assertThat(convertedCard.getDateLocked()).isEqualTo(cardDTO.getDateLocked());
        assertThat(convertedCard.getAccount().getId()).isEqualTo(cardDTO.getAccountId());
    }
}

