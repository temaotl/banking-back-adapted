package eu.greyson.sample.data.dto.user.converters;


import eu.greyson.sample.data.entity.User;
import eu.greyson.sample.data.dto.user.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class DtoToUserConverter implements Function<UserDTO, User> {

    private final ModelMapper modelMapper;

    @Autowired
    public DtoToUserConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public User apply(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}

