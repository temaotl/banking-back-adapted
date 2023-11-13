package eu.greyson.sample.controller;

import eu.greyson.sample.business.UserService;
import eu.greyson.sample.data.dto.user.UserDTO;
import eu.greyson.sample.data.dto.user.converters.DtoToUserConverter;
import eu.greyson.sample.data.dto.user.converters.UserToDtoConverter;
import eu.greyson.sample.data.entity.User;
import eu.greyson.sample.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/user")
public class UserController extends AbstractCrudController<User, UserDTO,UserDTO, Long, UserRepository> {

    private final UserService userService;

    @Autowired
    protected UserController(
            UserService userService,
            UserRepository repository,
            UserToDtoConverter toDtoConverter,
            DtoToUserConverter toEntityConverter) {
        super(repository, toDtoConverter, toDtoConverter, toEntityConverter);
        this.userService = userService;
    }

    @Override
    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO dto) {
        UserDTO createdUser = userService.create(dto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO dto, @PathVariable Long id) {
        try {
            userService.update(dto, id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(dto);
    }

}

