package eu.greyson.sample.data.dto;

import eu.greyson.sample.data.dto.account.AccountDTO;
import eu.greyson.sample.data.dto.account.converters.AccountToDtoConverter;
import eu.greyson.sample.data.dto.account.converters.DtoToAccountConverter;
import eu.greyson.sample.data.entity.Account;
import eu.greyson.sample.data.entity.User;
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
class AccountToDtoConverterTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountToDtoConverter accountToDtoConverter;

    @Autowired
    private DtoToAccountConverter dtoToAccountConverter;

    @Test
    void testAccountToDtoAndDtoToAccountConversion() {
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


        AccountDTO accountDTO = accountToDtoConverter.apply(newAccount);
        assertThat(accountDTO.getUserId()).isEqualTo(savedUser.getId());
        assertThat(accountDTO.getIban()).isEqualTo(newAccount.getIban());
        assertThat(accountDTO.getName()).isEqualTo(newAccount.getName());
        assertThat(accountDTO.getBalance()).isEqualByComparingTo(newAccount.getBalance());
        assertThat(accountDTO.getCurrency()).isEqualTo(newAccount.getCurrency());


        Account convertedAccount = dtoToAccountConverter.apply(accountDTO);
        assertThat(convertedAccount.getUser()).isEqualTo(savedUser);
        assertThat(convertedAccount.getIban()).isEqualTo(accountDTO.getIban());
        assertThat(convertedAccount.getName()).isEqualTo(accountDTO.getName());
        assertThat(convertedAccount.getBalance()).isEqualByComparingTo(accountDTO.getBalance());
        assertThat(convertedAccount.getCurrency()).isEqualTo(accountDTO.getCurrency());
    }
}

