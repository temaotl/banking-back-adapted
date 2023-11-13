package eu.greyson.sample.controller;


import eu.greyson.sample.business.TransactionService;
import eu.greyson.sample.data.dto.transaction.TransactionDTO;
import eu.greyson.sample.data.dto.transaction.converters.DtoToTransactionConverter;
import eu.greyson.sample.data.dto.transaction.converters.TransactionToDtoConverter;
import eu.greyson.sample.data.entity.Transaction;
import eu.greyson.sample.data.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/transaction")
public class TransactionController extends AbstractCrudController<Transaction, TransactionDTO,TransactionDTO, Long, TransactionRepository> {

    private  final TransactionService service;

    @Autowired
    protected TransactionController(TransactionService service,
                                    TransactionRepository repository,
                                    TransactionToDtoConverter toDtoConverter,
                                    DtoToTransactionConverter toEntityConverter) {
        super(repository, toDtoConverter, toDtoConverter, toEntityConverter);
        this.service = service;
    }

    @Override
    @PostMapping
    public ResponseEntity<TransactionDTO> create(@Valid @RequestBody TransactionDTO dto) {
        TransactionDTO createdCard = service.create(dto);
        return new ResponseEntity<>(createdCard, HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> update(@Valid @RequestBody TransactionDTO dto, @PathVariable  Long id) {
        try {
            service.update(dto, id);
            Transaction updated = toEntityConverter.apply(dto);
            return ResponseEntity.ok(toDtoConverter.apply(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
