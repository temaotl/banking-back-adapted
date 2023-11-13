package eu.greyson.sample.data.dto.user.converters;

import eu.greyson.sample.data.entity.User;
import eu.greyson.sample.data.dto.user.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserToDtoConverter implements Function<User, UserDTO> {

    private final ModelMapper modelMapper;

    @Autowired
    public UserToDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public UserDTO apply(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
