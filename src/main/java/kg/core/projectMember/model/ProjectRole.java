package kg.core.projectMember.model;

public enum ProjectRole {
    OWNER(30),
    EDITOR(20),
    VIEWER(10);

    private final int id;

    ProjectRole(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }
}
