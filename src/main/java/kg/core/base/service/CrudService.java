package kg.core.base.service;

import java.util.List;

public interface CrudService<T, ID> {

    T save(T entity);

    T find(ID id);

    T get(ID id);

    List<T> findAll();

    List<T> getByIds(ID[] ids);

}
