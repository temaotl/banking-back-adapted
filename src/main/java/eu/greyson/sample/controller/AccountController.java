package eu.greyson.sample.controller;


import eu.greyson.sample.business.AccountService;
import eu.greyson.sample.business.CardService;
import eu.greyson.sample.business.TransactionService;
import eu.greyson.sample.data.dto.account.AccountDTO;
import eu.greyson.sample.data.dto.account.AccountReadDTO;
import eu.greyson.sample.data.dto.card.CardBlockDTO;
import eu.greyson.sample.data.dto.card.CardReadDTO;
import eu.greyson.sample.data.dto.transaction.TransactionCreationDTO;
import eu.greyson.sample.data.dto.transaction.TransactionDTO;
import eu.greyson.sample.data.dto.account.converters.AccountToDtoConverter;
import eu.greyson.sample.data.dto.account.converters.AccountToReadDtoConverter;
import eu.greyson.sample.data.dto.account.converters.DtoToAccountConverter;
import eu.greyson.sample.data.entity.Account;
import eu.greyson.sample.data.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.List;
/**
 * REST controller for managing accounts. It extends the generic AbstractCrudController to provide
 * operations specifically for Account entities and their associated data like transactions and cards.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController extends AbstractCrudController<Account, AccountDTO,AccountReadDTO, Long, AccountRepository> {

    private final AccountService service;
    private final TransactionService transactionService;
    private final CardService cardService;

    private final ModelMapper modelMapper;

    @Autowired
    protected AccountController(ModelMapper modelMapper,
                                AccountService service,
                                CardService cardService,
                                TransactionService transactionService,
                                AccountRepository repository,
                                AccountToDtoConverter toDtoConverter,
                                AccountToReadDtoConverter toReadDtoConverter,
                                DtoToAccountConverter toEntityConverter) {
        super(repository, toDtoConverter, toReadDtoConverter, toEntityConverter);
        this.service = service;
        this.transactionService = transactionService;
        this.modelMapper =modelMapper;
        this.cardService = cardService;
    }

    @Override
    @PostMapping
    public ResponseEntity<AccountDTO> create(@RequestBody AccountDTO dto) {

        try {
            AccountDTO createdAccount = service.create(dto);
            return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<AccountDTO> update(@Valid @RequestBody AccountDTO dto, @PathVariable Long id) {
        try {
            service.update(dto, id);
            Account updatedAccount = toEntityConverter.apply(dto);
            return ResponseEntity.ok(toDtoConverter.apply(updatedAccount));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Handles request to retrieve all accounts associated with a given user.
     *
     * @param userId The user ID for which accounts are to be retrieved.
     * @return ResponseEntity containing a list of AccountReadDTOs or not found status.
     */
    @GetMapping(params = "userId")
    public ResponseEntity<List<AccountReadDTO>> getAccountsByUserId(@RequestParam Long userId) {
        List<AccountReadDTO> accounts = service.findAllByUserId(userId);
        return ResponseEntity.ok(accounts);
    }

    /**
     * Handles request to retrieve all transactions associated with a given account.
     *
     * @param id The account ID for which transactions are to be retrieved.
     * @return ResponseEntity containing a list of TransactionDTOs.
     */
    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionDTO>> getAccountTransactions(@PathVariable Long id) {
        List<TransactionDTO> transactions = transactionService.findAllByAccountId(id);
        return ResponseEntity.ok(transactions);
    }

    /**
     * Handles request to create a new transaction for a specified account.
     *
     * @param id The account ID for which the transaction is to be created.
     * @param creationRequest The DTO containing the transaction creation data.
     * @return ResponseEntity containing the created TransactionDTO or not found status.
     */
    @PostMapping("/{id}/transactions")
    public ResponseEntity<TransactionDTO> createTransaction(@PathVariable Long id, @RequestBody TransactionCreationDTO creationRequest) {
        return service.readById(id).map(account -> {
            TransactionDTO transactionDTO = modelMapper.map(creationRequest, TransactionDTO.class);
            transactionDTO.setCreditor(account.getIban());
            transactionDTO.setCurrency(account.getCurrency());

            TransactionDTO createdTransaction = transactionService.create(transactionDTO);
            return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    /**
     * Handles  request to retrieve all cards associated with a given account ID.
     *
     * @param id The account ID for which cards are to be retrieved.
     * @return ResponseEntity containing a list of CardReadDTOs or not found status.
     */
    @GetMapping("/{id}/cards")
    public ResponseEntity<List<CardReadDTO>> getAccountCards(@PathVariable Long id) {
        if (service.readById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<CardReadDTO> cards = cardService.findAllByAccountId(id);
        return ResponseEntity.ok(cards);
    }

    /**
     * Handles request to retrieve a specific card by its ID and associated account ID.
     *
     * @param accountId The account ID associated with the card.
     * @param cardId    The card ID to be retrieved.
     * @return ResponseEntity containing the CardReadDTO or not found status.
     */
    @GetMapping("/{accountId}/cards/{cardId}")
    public ResponseEntity<CardReadDTO> getCard(@PathVariable Long accountId, @PathVariable Long cardId) {
        try {
            CardReadDTO card = cardService.findByAccountIdAndCardId(accountId, cardId);
            return ResponseEntity.ok(card);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Handles  request to update (block or unblock) card status.
     *
     * @param accountId    The account ID associated with the card.
     * @param cardId       The card ID to be updated.
     * @param cardBlockDTO DTO containing the new state of the card.
     * @return ResponseEntity containing the updated CardReadDTO or not found status.
     */
    @PutMapping("/{accountId}/cards/{cardId}")
    public  ResponseEntity<CardReadDTO> changeCardState(@PathVariable Long accountId,
                                                        @PathVariable Long cardId,
                                                        @RequestBody CardBlockDTO cardBlockDTO)
    {
        try {
            CardReadDTO card = cardService.updateCardStatus(accountId, cardId,cardBlockDTO);
            return ResponseEntity.ok(card);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}

