package eu.greyson.sample.controller;

import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.StreamSupport;

/**
 * Abstract base controller class for CRUD operations on a generic entity.
 *
 * @param <E> entity
 * @param <W> write DTO
 * @param <R> read DTO
 * @param <I> id type
 * @param <T> repository
 */
public abstract class AbstractCrudController<E, W, R, I, T extends CrudRepository<E, I>> {

    protected final T repository;
    protected final Function<E, W> toDtoConverter;
    protected final Function<W, E> toEntityConverter;

    protected final Function<E, R> toReadDtoConverter;

    /**
     * @param repository         The repository used for data access.
     * @param toDtoConverter     Function to convert entities to DTO used in all not get query's.
     * @param toReadDtoConverter Function to convert entities  to DTO use in all get query's.
     * @param toEntityConverter  Function to convert write DTOs to entities.
     */
    protected AbstractCrudController(T repository,
                                     Function<E, W> toDtoConverter,
                                     Function<E, R> toReadDtoConverter,
                                     Function<W, E> toEntityConverter) {
        this.repository = repository;
        this.toDtoConverter = toDtoConverter;
        this.toEntityConverter = toEntityConverter;
        this.toReadDtoConverter = toReadDtoConverter;
    }

    /**
     * @return A list of read DTOs representing all entities.
     */
    @GetMapping
    public List<R> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(toReadDtoConverter)
                .toList();
    }

    /**
     * @param id The ID of the entity to retrieve.
     * @return ResponseEntity containing the read DTO or not found status.
     */
    @GetMapping("/{id}")
    public ResponseEntity<R> getOne(@PathVariable I id) {
        return repository.findById(id)
                .map(toReadDtoConverter)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Abstract method for creating an entity from write DTO.
     *
     * @param dto  write DTO containing data for the new entity.
     * @return ResponseEntity containing the created write DTO or error status.
     */
    @PostMapping
    public abstract ResponseEntity<W> create(@RequestBody W dto);
    /**
     * Abstract method for updating an entity with data from a write DTO.
     *
     * @param dto  write DTO containing updated data for the entity.
     * @param id  The ID of the entity to update.
     * @return ResponseEntity containing the updated write DTO or error status.
     */
    @PutMapping("/{id}")
    public abstract ResponseEntity<W> update(@RequestBody W dto, @PathVariable I id);

    /**
     * @param id The ID of the entity to delete.
     * @return ResponseEntity indicating the result of the delete operation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable I id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

