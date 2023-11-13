package eu.greyson.sample.business;

import eu.greyson.sample.data.dto.account.AccountDTO;
import eu.greyson.sample.data.dto.card.CardDTO;
import eu.greyson.sample.data.dto.user.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class CardServiceTest {

    @Autowired
    private CardService cardService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    private Long savedAccountId;
    private Long savedCardId;

    @BeforeEach
    void setUp() {

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setBirthDate(LocalDate.of(1980, 1, 1));
        UserDTO createdUser = userService.create(userDTO);
        assertNotNull(createdUser.getId());
        Long savedUserId = createdUser.getId();


        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setIban("DE89 3704 0044 0532 0130 00");
        accountDTO.setName("John Doe Account");
        accountDTO.setBalance(new BigDecimal("1000.00"));
        accountDTO.setCurrency("EUR");
        accountDTO.setUserId(savedUserId);
        AccountDTO createdAccount = accountService.create(accountDTO);
        savedAccountId = createdAccount.getId();


        CardDTO cardDTO = new CardDTO();
        cardDTO.setName("Personal Card");
        cardDTO.setBlocked(false);
        cardDTO.setAccountId(savedAccountId);
        CardDTO createdCardDTO = cardService.create(cardDTO);
        savedCardId = createdCardDTO.getId();
    }

    @Test
    void testCreateCard() {
        CardDTO newCardDTO = new CardDTO();
        newCardDTO.setName("New Card");
        newCardDTO.setBlocked(true);
        newCardDTO.setAccountId(savedAccountId);

        CardDTO createdCardDTO = cardService.create(newCardDTO);

        assertThat(createdCardDTO).isNotNull();
        assertThat(createdCardDTO.getName()).isEqualTo("New Card");
        assertThat(createdCardDTO.getBlocked()).isTrue();
        assertThat(createdCardDTO.getAccountId()).isEqualTo(savedAccountId);
    }

    @Test
    void testReadCard() {
        Optional<CardDTO> cardDTO = cardService.readById(savedCardId);
        assertThat(cardDTO).isPresent();
        assertThat(cardDTO.get().getId()).isEqualTo(savedCardId);
    }

    @Test
    void testUpdateCard() {
        Optional<CardDTO> cardDTO = cardService.readById(savedCardId);
        assertThat(cardDTO).isPresent();
        cardDTO.get().setName("Updated Card");
        cardDTO.get().setBlocked(true);

        cardService.update(cardDTO.get(), savedCardId);

        Optional<CardDTO> updatedCardDTO = cardService.readById(savedCardId);
        assertThat(updatedCardDTO).isPresent();
        assertThat(updatedCardDTO.get().getName()).isEqualTo("Updated Card");
        assertThat(updatedCardDTO.get().getBlocked()).isTrue();
    }


    @Test
    void testDeleteCard() {
        cardService.deleteById(savedCardId);
        Optional<CardDTO> deletedCardDTO = cardService.readById(savedCardId);
        assertThat(deletedCardDTO).isNotPresent();
    }
}
