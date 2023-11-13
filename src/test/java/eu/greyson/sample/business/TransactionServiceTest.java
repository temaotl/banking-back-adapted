package eu.greyson.sample.business;

import eu.greyson.sample.data.dto.transaction.TransactionDTO;
import eu.greyson.sample.data.entity.Account;
import eu.greyson.sample.data.entity.User;
import eu.greyson.sample.data.repository.AccountRepository;
import eu.greyson.sample.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;


    private User newUser;
    private TransactionDTO transactionDTO;

    @BeforeEach
    void setUp() {
        User newUser = new User();
        newUser.setFirstName("Kirin");
        newUser.setLastName("Dorian");
        newUser.setBirthDate(LocalDate.of(1990, 5, 12));
        User savedUser = userRepository.save(newUser);

        Account newAccount = new Account();
        newAccount.setName("Savings Account");
        newAccount.setIban("GB29 NWBK 6016 1331 9268 19");
        newAccount.setBalance(new BigDecimal("1000.00"));
        newAccount.setCurrency("CZK");
        newAccount.setUser(savedUser);
        Account savedAccount = accountRepository.save(newAccount);

        transactionDTO = new TransactionDTO();
        transactionDTO.setDateCreated(LocalDateTime.now());
        transactionDTO.setDateExecuted(LocalDateTime.now());
        transactionDTO.setDebtor("GB29 NWBK 6016 1331 9268 20");
        transactionDTO.setCreditor("GB29 NWBK 6016 1331 9268 19");
        transactionDTO.setAmount(new BigDecimal("100.00"));
        transactionDTO.setCurrency("CZK");
    }

    @Test
    void testCreateTransaction() {
        TransactionDTO createdTransaction = transactionService.create(transactionDTO);
        assertNotNull(createdTransaction.getId());
    }

    @Test
     void testReadTransaction() {
        TransactionDTO createdTransaction = transactionService.create(transactionDTO);
        assertTrue(transactionService.readById(createdTransaction.getId()).isPresent());
    }

    @Test
    void testUpdateTransaction() {
        TransactionDTO createdTransaction = transactionService.create(transactionDTO);
        createdTransaction.setAmount(new BigDecimal("1000.00"));
        transactionService.update(createdTransaction, createdTransaction.getId());

        TransactionDTO updatedTransaction = transactionService.readById(createdTransaction.getId()).orElse(null);
        assertNotNull(updatedTransaction);
        assertEquals(new BigDecimal("1000.00"), updatedTransaction.getAmount());
    }

    @Test
    void testDeleteTransaction() {
        TransactionDTO createdTransaction = transactionService.create(transactionDTO);
        transactionService.deleteById(createdTransaction.getId());
        assertFalse(transactionService.readById(createdTransaction.getId()).isPresent());
    }

    @Test
    void testReadAllTransactions() {
        transactionService.create(transactionDTO);
        assertFalse(transactionService.readAll().isEmpty());
    }
}
