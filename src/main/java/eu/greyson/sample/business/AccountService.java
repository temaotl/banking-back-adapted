package eu.greyson.sample.business;

import eu.greyson.sample.data.dto.account.AccountDTO;
import eu.greyson.sample.data.dto.account.AccountReadDTO;
import eu.greyson.sample.data.dto.account.converters.AccountToDtoConverter;
import eu.greyson.sample.data.dto.account.converters.AccountToReadDtoConverter;
import eu.greyson.sample.data.dto.account.converters.DtoToAccountConverter;
import eu.greyson.sample.data.entity.Account;
import eu.greyson.sample.data.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for account management. It extends the generic AbstractCrudService class
 * to provide CRUD operations specifically for Account entities.
 */
@Service
public class AccountService extends AbstractCrudService<AccountDTO, Long, Account, AccountRepository> {
    private final AccountToReadDtoConverter toReadEntityConverter;

    @Autowired
    public AccountService(AccountToReadDtoConverter accountToReadDtoConverter,AccountRepository repository,
                          DtoToAccountConverter dtoToAccountConverter,
                          AccountToDtoConverter accountToDtoConverter) {
        super(repository, dtoToAccountConverter, accountToDtoConverter);
        this.toReadEntityConverter = accountToReadDtoConverter;
    }

    @Override
    @Transactional
    public void update(AccountDTO accountDTO, Long id) {
        Account existingAccount = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id " + id));
        Account updatedAccount = toEntityConverter.apply(accountDTO);
        updatedAccount.setId(existingAccount.getId());
        repository.save(updatedAccount);
    }
    /**
     * Retrieves all account entities associated with a given user ID and returns them as DTOs.
     * @param userId The ID of the user.
     * @return A list of AccountReadDTOs for the specified on the userId param.
     */
    public List<AccountReadDTO> findAllByUserId(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(toReadEntityConverter)
                .toList();
    }

    @Override
    @Transactional
    public AccountDTO create(AccountDTO accountDTO) {
        if (repository.existsByIban(accountDTO.getIban())) {
            throw new IllegalStateException("Account with IBAN " + accountDTO.getIban() + " already exists");
        }
        return super.create(accountDTO);
    }

}
