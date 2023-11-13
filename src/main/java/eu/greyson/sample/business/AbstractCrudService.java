package eu.greyson.sample.business;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.StreamSupport;

/**
 *
 * Implements basic CRUD functionality
 * This class is generic and can be used with any entity type and repository.
 * @param <D> DTO
 * @param <K> KEY
 * @param <E> ENTITY
 * @param <R> REPOSITORY
 */
@Transactional(readOnly = true)
public abstract class AbstractCrudService<D, K, E, R extends CrudRepository<E, K>> {

    protected final R repository;
    protected final Function<D, E> toEntityConverter;
    protected final Function<E, D> toDtoConverter;

    /**
     * @param repository  data access to repository.
     * @param toEntityConverter Function to convert a DTO to an entity.
     * @param toDtoConverter Function to convert an entity to a DTO.
     */
    protected AbstractCrudService(R repository, Function<D, E> toEntityConverter, Function<E, D> toDtoConverter) {
        this.repository = repository;
        this.toEntityConverter = toEntityConverter;
        this.toDtoConverter = toDtoConverter;
    }
    /**
     * Creates a new entity in the database from the provided DTO.
     *
     * @param dto The DTO to create a new entity from.
     * @return The created entity represented as a DTO.
     */
    @Transactional
    public D create(D dto) {
        E entity = toEntityConverter.apply(dto);
        E savedEntity = repository.save(entity);
        return toDtoConverter.apply(savedEntity);
    }
    /**
     * Retrieves an entity by its ID and returns it as a DTO.
     *
     * @param id The key of the entity to retrieve.
     * @return DTO if found, or empty otherwise.
     */
    public Optional<D> readById(K id) {
        return repository.findById(id).map(toDtoConverter);
    }
    /**
     * Retrieves all entities and returns them as a list of DTOs.
     * @return A List of DTOs representing all entities.
     */
    public List<D> readAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(toDtoConverter)
                .toList();
    }

    /**
     * Abstract method to update an existing entity.
     * @param dto The DTO containing updated data.
     * @param id  The key of the entity to update.
     */
    @Transactional
    public abstract void update(D dto, K id);

    /**
     Deletes the entity with the specified ID.
     * @param id  key of the entity to delete
     */
    @Transactional
    public void deleteById(K id) {
        repository.deleteById(id);
    }
}

