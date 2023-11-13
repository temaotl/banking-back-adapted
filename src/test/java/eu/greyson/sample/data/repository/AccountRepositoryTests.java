package eu.greyson.sample.data.repository;

import eu.greyson.sample.data.entity.Account;
import eu.greyson.sample.data.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AccountRepositoryTests {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAccount() {

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

        assertThat(savedAccount.getId()).isNotNull();
        assertThat(savedAccount.getName()).isEqualTo(newAccount.getName());
        assertThat(savedAccount.getIban()).isEqualTo(newAccount.getIban());
        assertThat(savedAccount.getBalance()).isEqualTo(newAccount.getBalance());
        assertThat(savedAccount.getCurrency()).isEqualTo(newAccount.getCurrency());
        assertThat(savedAccount.getUser()).isEqualTo(savedUser);
    }
}
