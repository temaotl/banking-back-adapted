package eu.greyson.sample.data.repository;


import eu.greyson.sample.data.entity.Account;
import eu.greyson.sample.data.entity.Card;
import eu.greyson.sample.data.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CardRepositoryTests {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void shouldSaveAndFindCard() {

        User newUser = new User();
        newUser.setFirstName("Kirin");
        newUser.setLastName("Dorian");
        newUser.setBirthDate(LocalDate.of(1990, 5, 12));

        Account newAccount = new Account();
        newAccount.setName("Savings Account");
        newAccount.setIban("CZ1234256789");
        newAccount.setBalance(new BigDecimal("1500.00"));
        newAccount.setCurrency("CZK");
        newAccount.setUser(newUser);


        Account savedAccount = accountRepository.save(newAccount);


        Card newCard = new Card();
        newCard.setName("Test Card");
        newCard.setBlocked(false);
        newCard.setAccount(savedAccount);


        Card savedCard = cardRepository.save(newCard);


        assertThat(savedCard.getId()).isNotNull();


        assertThat(savedCard.getName()).isEqualTo(newCard.getName());
        assertThat(savedCard.isBlocked()).isEqualTo(newCard.isBlocked());
        assertThat(savedCard.getAccount()).isEqualTo(savedAccount);

        Optional<Card> foundCard = cardRepository.findById(savedCard.getId());
        assertThat(foundCard).isPresent();
        assertThat(foundCard.get()).isEqualTo(savedCard);
    }
}

