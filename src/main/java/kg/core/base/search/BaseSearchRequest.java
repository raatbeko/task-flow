package kg.core.base.search;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseSearchRequest<S extends SortBy> {

    Integer page = 0;
    Integer size = 20;

    S sortBy;
    Sort.Direction direction = Sort.Direction.ASC;

    protected abstract S getDefaultSortBy();

    public S getSortBy() {
        return sortBy != null ? sortBy : getDefaultSortBy();
    }
}