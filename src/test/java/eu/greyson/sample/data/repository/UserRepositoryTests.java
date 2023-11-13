package eu.greyson.sample.data.repository;

import eu.greyson.sample.data.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
 class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveUser() {
        User newUser = new User();
        newUser.setFirstName("Kirin");
        newUser.setLastName("Dorian");
        newUser.setBirthDate(LocalDate.of(1990, 5, 12));


        User savedUser = userRepository.save(newUser);
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getFirstName()).isEqualTo(newUser.getFirstName());
        assertThat(savedUser.getLastName()).isEqualTo(newUser.getLastName());
        assertThat(savedUser.getBirthDate()).isEqualTo(newUser.getBirthDate());
    }
}
