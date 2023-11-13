package eu.greyson.sample.data.dto;

import eu.greyson.sample.data.dto.user.converters.UserToDtoConverter;
import eu.greyson.sample.data.dto.user.UserDTO;
import eu.greyson.sample.data.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserToDtoConverterIntegrationTest {

    @Autowired
    private UserToDtoConverter userToDtoConverter;

    @Test
    void testConvertUserToUserDTO() {

        User user = new User();
        user.setId(1L);
        user.setFirstName("Test");
        user.setLastName("Testovic");

        user.setBirthDate(LocalDate.of(1990, 1, 1));


        UserDTO userDTO = userToDtoConverter.apply(user);


        assertThat(userDTO.getId()).isEqualTo(user.getId());
        assertThat(userDTO.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userDTO.getLastName()).isEqualTo(user.getLastName());
        assertThat(userDTO.getBirthDate()).isEqualTo(user.getBirthDate());

    }
}
