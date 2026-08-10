package kg.core.boardMember.model;

public enum BoardRole {
    OWNER(30),
    EDITOR(20),
    VIEWER(10);

    private final int id;

    BoardRole(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }
}
