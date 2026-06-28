package kg.core.user.search;

import kg.core.base.search.BaseSearchRequest;
import kg.core.user.model.AuthProvider;
import kg.core.user.model.Role;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSearchRequest extends BaseSearchRequest<UserSort> {

    Long id;
    String email;
    String username;
    String phoneNumber;
    Role role;
    AuthProvider provider;

    @Override
    protected UserSort getDefaultSortBy() {
        return UserSort.ID;
    }
}