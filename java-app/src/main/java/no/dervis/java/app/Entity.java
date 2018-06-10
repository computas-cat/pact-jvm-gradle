package no.dervis.java.app;

public class Entity {
    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "uri='" + uri + '\'' +
                '}';
    }
}
