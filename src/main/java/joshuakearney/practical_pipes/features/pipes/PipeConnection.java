package joshuakearney.practical_pipes.features.pipes;

public enum PipeConnection {
    None("none"), Pipe("pipe"), External("external");

    private final String name;

    PipeConnection(String name) {
        this.name = name;
    }

    public String getName() { return this.name; }
}