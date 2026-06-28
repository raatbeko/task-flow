package kg.core.base.search;

import com.querydsl.core.types.Order;
import org.springframework.data.domain.Sort;

public interface SortBy {
    Sort getSorting(Sort.Direction direction);

    default Order directionToOrder(Sort.Direction direction) {
        return switch (direction) {
            case ASC -> Order.ASC;
            case DESC -> Order.DESC;
        };
    }
}

