package no.dervis.java.app;

public class Entity {
    private String uri;

    private int id;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "uri='" + uri + '\'' +
                ", id=" + id +
                '}';
    }
}
