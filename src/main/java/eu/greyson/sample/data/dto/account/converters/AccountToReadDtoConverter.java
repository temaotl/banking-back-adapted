package eu.greyson.sample.data.dto.account.converters;


import eu.greyson.sample.data.dto.account.AccountReadDTO;
import eu.greyson.sample.data.entity.Account;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.function.Function;

@Component
public class AccountToReadDtoConverter implements Function<Account, AccountReadDTO> {

    private final ModelMapper modelMapper;

    @Autowired
    public AccountToReadDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AccountReadDTO apply(Account account) {

        return modelMapper.map(account, AccountReadDTO.class);
    }
}
