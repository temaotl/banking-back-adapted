package eu.greyson.sample.data.dto;

import eu.greyson.sample.data.dto.user.converters.DtoToUserConverter;
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
class DtoToUserConverterIntegrationTest {

    @Autowired
    private DtoToUserConverter dtoToUserConverter;

    @Test
    void testConvertUserDtoToUser() {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("Test");
        userDTO.setLastName("Testovic");

        userDTO.setBirthDate(LocalDate.of(1990, 1, 1));


        User user = dtoToUserConverter.apply(userDTO);

        assertThat(user.getId()).isEqualTo(userDTO.getId());
        assertThat(user.getFirstName()).isEqualTo(userDTO.getFirstName());
        assertThat(user.getLastName()).isEqualTo(userDTO.getLastName());
        assertThat(user.getBirthDate()).isEqualTo(userDTO.getBirthDate());

    }
}

