package kg.core.base.search;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Objects;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, QuerydslPredicateExecutor<T> {

    default Page<T> findAll(Predicate predicate, BaseSearchRequest<? extends SortBy> pageRequest) {
        Sort sort = Objects.nonNull(pageRequest.getSortBy()) ? getSort(pageRequest) : Sort.unsorted();
        PageRequest page = PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), sort);

        return findAll(predicate, page);
    }

    default Page<T> findAll(BaseSearchRequest<? extends SortBy> pageRequest) {
        Sort sort = getSort(pageRequest);
        PageRequest page = PageRequest.of(pageRequest.getPage() - 1, pageRequest.getSize(), sort);
        return findAll(page);
    }

    private Sort getSort(BaseSearchRequest<? extends SortBy> pageRequest) {
        Sort.Direction sortDirection = pageRequest.getDirection();
        return pageRequest.getSortBy().getSorting(sortDirection);
    }
}
