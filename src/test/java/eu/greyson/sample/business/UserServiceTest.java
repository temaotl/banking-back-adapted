package eu.greyson.sample.business;

import static org.junit.jupiter.api.Assertions.*;


import eu.greyson.sample.data.dto.user.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setLastName("Shepard");
        userDTO.setBirthDate(LocalDate.of(1980, 1, 1));

    }

    @Test
     void testCreateReadUpdateDelete() {
        // Create
        UserDTO createdUser = userService.create(userDTO);
        assertNotNull(createdUser.getId(), "User should be successfully created with an ID");
        assertEquals("John", createdUser.getFirstName(), "The first name of the created user should match");

        // Read
        UserDTO foundUser = userService.readById(createdUser.getId())
                .orElseThrow(() -> new AssertionError("User should be found by ID"));
        assertEquals("John", foundUser.getFirstName(), "The first name of the found user should match");

        // Update
        foundUser.setFirstName("Jane");
        userService.update(foundUser, foundUser.getId());

        UserDTO updatedUser = userService.readById(foundUser.getId())
                .orElseThrow(() -> new AssertionError("Updated user should be found"));
        assertEquals("Jane", updatedUser.getFirstName(), "The first name of the updated user should be changed to 'Jane'");

        // Delete
        userService.deleteById(updatedUser.getId());
        assertTrue(userService.readById(updatedUser.getId()).isEmpty(), "User should be deleted");
    }
}

