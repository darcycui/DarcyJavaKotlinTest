package records;

public record RecordA(int id, String name) {
    public RecordA(int id, String name) {
        this.id = id + 1;
        this.name = name;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "RecordA{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
