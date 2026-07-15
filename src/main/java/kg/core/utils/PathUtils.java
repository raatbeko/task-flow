package kg.core.utils;

public final class PathUtils {

    private PathUtils() {
    }

    public static final String API = "/api";
    public static final String V1 = API + "/v1";

    public static final String AUTH = V1 + "/auth";
    public static final String AUTH_OAUTH_2 = AUTH + "/oauth2";
    public static final String USERS = V1 + "/users";

    public static final String ADMIN = V1 + "/admin";
    public static final String ADMIN_USERS = ADMIN + "/users";

    public static final String BOARD_COLUMN = V1 + "/board-columns";
    public static final String BOARDS = V1 + "/boards";

    public static final String PROJECT_MEMBER = V1 + "/project-members";

    public static final String TASK = V1 + "/task";
    public static final String BOARD_MEMBER = V1 + "/board-members";
}

