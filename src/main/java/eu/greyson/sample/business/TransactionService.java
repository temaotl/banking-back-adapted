package eu.greyson.sample.business;

import eu.greyson.sample.custom_exception.InsufficientFundsException;
import eu.greyson.sample.data.dto.transaction.TransactionDTO;
import eu.greyson.sample.data.entity.Account;
import eu.greyson.sample.data.entity.Transaction;
import eu.greyson.sample.data.repository.AccountRepository;
import eu.greyson.sample.data.repository.TransactionRepository;
import eu.greyson.sample.data.dto.transaction.converters.DtoToTransactionConverter;
import eu.greyson.sample.data.dto.transaction.converters.TransactionToDtoConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * Service class for transaction management. Extends the generic AbstractCrudService class
 * to provide operations specifically for Transaction entities.
 */
@Service
public class TransactionService extends AbstractCrudService<TransactionDTO, Long, Transaction, TransactionRepository> {

    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;

    @Autowired
    public TransactionService(TransactionRepository repository,
                              DtoToTransactionConverter toEntityConverter,
                              TransactionToDtoConverter toDtoConverter,
                              ModelMapper modelMapper,
                              AccountRepository accountRepository
                              ) {
        super(repository, toEntityConverter, toDtoConverter);
        this.modelMapper = modelMapper;
        this.accountRepository=accountRepository;
    }

    @Override
    @Transactional
    public void update(TransactionDTO dto, Long id) {
        Transaction existingTransaction = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id " + id));
        Transaction updatedTransaction = toEntityConverter.apply(dto);
        updatedTransaction.setId(existingTransaction.getId());
        modelMapper.map(updatedTransaction, existingTransaction);
        repository.save(existingTransaction);
    }

    /**
     * Retrieves all transactions associated with a given account ID.
     *
     * @param accountId The ID of the account whose transactions are to be retrieved.
     * @return A list of TransactionDTOs for the specified account ID.
     */
    public List<TransactionDTO> findAllByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id " + accountId));

        String accountIban = account.getIban();
        List<Transaction> transactions = repository.findByCreditorOrDebtor(accountIban, accountIban);
        return transactions.stream()
                .map(toDtoConverter)
                .toList();
    }
    /**
     * Overridden create method for creating a new transaction.
     * Validates account balances and updates them according to the transaction.
     * if all account are internal provide balance update of 2 account
     * @param transactionDTO DTO of the transaction to be created.
     * @return The created TransactionDTO.
     */
    @Override
    @Transactional
    public TransactionDTO create(TransactionDTO transactionDTO) {

        Account creditorAccount = accountRepository.findByIban(transactionDTO.getCreditor())
                .orElseThrow(() -> new EntityNotFoundException("Account with IBAN " + transactionDTO.getCreditor() + " not found"));


        if (creditorAccount.getBalance().compareTo(transactionDTO.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in account with IBAN: " + transactionDTO.getCreditor());
        }


        LocalDateTime now = LocalDateTime.now();
        transactionDTO.setDateCreated(now);
        transactionDTO.setDateExecuted(now.plusDays(1));


        Transaction transaction = toEntityConverter.apply(transactionDTO);
        transaction = repository.save(transaction);


        creditorAccount.setBalance(creditorAccount.getBalance().subtract(transactionDTO.getAmount()));
        accountRepository.save(creditorAccount);

        Optional<Account> debtorAccount = accountRepository.findByIban(transactionDTO.getDebtor());
        debtorAccount.ifPresent(account -> account.setBalance(account.getBalance().add(transactionDTO.getAmount())));


        return toDtoConverter.apply(transaction);
    }

}

