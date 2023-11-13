package eu.greyson.sample.data.dto.account.converters;

import eu.greyson.sample.data.dto.account.AccountDTO;
import eu.greyson.sample.data.entity.Account;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AccountToDtoConverter implements Function<Account, AccountDTO> {

    private final ModelMapper modelMapper;

    @Autowired
    public AccountToDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AccountDTO apply(Account account) {
        AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);
        if (account.getUser() != null) {
            accountDTO.setUserId(account.getUser().getId());
        }
        return accountDTO;
    }
}
