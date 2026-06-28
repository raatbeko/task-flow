package kg.core.base.service.impl;

import kg.core.base.exception.NotFoundException;
import kg.core.base.service.CrudService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

public abstract class DefaultCrudService<T, ID> implements CrudService<T, ID> {

    protected final JpaRepository<T, ID> repository;

    protected DefaultCrudService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public T find(ID id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Запись с id: " + id + " не найден!"));
    }

    @Override
    @Transactional(readOnly = true)
    public T get(ID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getByIds(ID[] ids) {
        return repository.findAllById(Arrays.asList(ids));
    }
}

