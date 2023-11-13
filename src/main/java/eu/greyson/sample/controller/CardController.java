package eu.greyson.sample.controller;



import eu.greyson.sample.business.CardService;
import eu.greyson.sample.data.dto.card.CardDTO;
import eu.greyson.sample.data.dto.card.CardReadDTO;
import eu.greyson.sample.data.dto.card.converters.CardToDtoConverter;
import eu.greyson.sample.data.dto.card.converters.CardToReadDtoConverter;
import eu.greyson.sample.data.dto.card.converters.DtoToCardConverter;
import eu.greyson.sample.data.entity.Card;
import eu.greyson.sample.data.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api/cards")

public class CardController extends AbstractCrudController<Card, CardDTO, CardReadDTO, Long, CardRepository> {

    private final CardService service;

    @Autowired
    protected CardController(CardService service,
                             CardRepository repository,
                             CardToDtoConverter toDtoConverter,
                             CardToReadDtoConverter toReadDtoConverter,
                             DtoToCardConverter toEntityConverter) {
        super(repository, toDtoConverter, toReadDtoConverter, toEntityConverter);
        this.service = service;
    }


    @Override
    @PostMapping
    public ResponseEntity<CardDTO> create(@RequestBody CardDTO dto) {
        CardDTO createdCard = service.create(dto);
        return new ResponseEntity<>(createdCard, HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<CardDTO> update(@RequestBody CardDTO dto, @PathVariable Long id) {
        try {
            service.update(dto, id);
            Card updatedCard = toEntityConverter.apply(dto);
            return ResponseEntity.ok(toDtoConverter.apply(updatedCard));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

