package eu.greyson.sample.business;

import static org.junit.jupiter.api.Assertions.*;


import eu.greyson.sample.data.dto.account.AccountDTO;
import eu.greyson.sample.data.dto.user.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
@Transactional
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    private AccountDTO accountDTO;

    @BeforeEach
    void setUp() {

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setBirthDate(LocalDate.of(1980, 1, 1));
        UserDTO createdUser = userService.create(userDTO);


        assertNotNull(createdUser.getId(), "User should be successfully created with an ID");


        accountDTO = new AccountDTO();
        accountDTO.setIban("DE89 3704 0044 0532 0130 00");
        accountDTO.setName("John Doe Account");
        accountDTO.setBalance(new BigDecimal("1000.00"));
        accountDTO.setCurrency("EUR");
        accountDTO.setUserId(createdUser.getId());
    }

    @Test
    void testCreateReadUpdateDelete() {
        // Create
        AccountDTO createdAccount = accountService.create(accountDTO);
        assertNotNull(createdAccount.getId(), "Account should be successfully created with an ID");

        // Read
        AccountDTO foundAccount = accountService.readById(createdAccount.getId())
                .orElseThrow(() -> new AssertionError("Account should be found by ID"));

        // Update
        foundAccount.setName("Jane Doe Account");
        accountService.update(foundAccount, foundAccount.getId());

        AccountDTO updatedAccount = accountService.readById(foundAccount.getId())
                .orElseThrow(() -> new AssertionError("Updated account should be found"));
        assertEquals("Jane Doe Account", updatedAccount.getName(), "The name of the updated account should be changed to 'Jane Doe Account'");

        // Delete
        accountService.deleteById(updatedAccount.getId());
        assertTrue(accountService.readById(updatedAccount.getId()).isEmpty(), "Account should be deleted");
    }
}

