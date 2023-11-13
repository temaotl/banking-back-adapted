package eu.greyson.sample.data.dto.card.converters;


import eu.greyson.sample.data.dto.card.CardDTO;
import eu.greyson.sample.data.entity.Card;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.function.Function;

@Component
public class CardToDtoConverter implements Function<Card, CardDTO> {

    private final ModelMapper modelMapper;

    @Autowired
    public CardToDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public CardDTO apply(Card card) {
        CardDTO cardDTO = modelMapper.map(card, CardDTO.class);
        if (card.getAccount() != null) {
            cardDTO.setAccountId(card.getAccount().getId());
        }
        return cardDTO;
    }
}
