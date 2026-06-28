package kg.core.user.search;

// 5. UserSort Enum with Sorting Logic

import com.querydsl.core.types.OrderSpecifier;
import kg.core.base.search.SortBy;
import kg.core.user.model.QUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QSort;

public enum UserSort implements SortBy {

    ID {
        @Override
        public Sort getSorting(Sort.Direction direction) {
            return new QSort(new OrderSpecifier<>(
                    directionToOrder(direction),
                    QUser.user.id
            ));
        }
    },
    EMAIL {
        @Override
        public Sort getSorting(Sort.Direction direction) {
            return new QSort(new OrderSpecifier<>(
                    directionToOrder(direction),
                    QUser.user.email
            ));
        }
    },
    USERNAME {
        @Override
        public Sort getSorting(Sort.Direction direction) {
            return new QSort(new OrderSpecifier<>(
                    directionToOrder(direction),
                    QUser.user.username
            ));
        }
    },
    PHONE_NUMBER {
        @Override
        public Sort getSorting(Sort.Direction direction) {
            return new QSort(new OrderSpecifier<>(
                    directionToOrder(direction),
                    QUser.user.phoneNumber
            ));
        }
    },
    CREATED_AT {
        @Override
        public Sort getSorting(Sort.Direction direction) {
            return new QSort(new OrderSpecifier<>(
                    directionToOrder(direction),
                    QUser.user.createdAt
            ));
        }
    };

    public static UserSort defaultSort() {
        return ID;
    }
}