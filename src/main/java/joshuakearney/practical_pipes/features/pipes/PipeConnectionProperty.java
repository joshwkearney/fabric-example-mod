package joshuakearney.practical_pipes.features.pipes;

import net.minecraft.state.property.Property;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class PipeConnectionProperty extends Property<PipeConnection> {
    private static final Collection<PipeConnection> values = Arrays.asList(PipeConnection.values());

    protected PipeConnectionProperty(String name) {
        super(name, PipeConnection.class);
    }

    @Override
    public Collection<PipeConnection> getValues() { return values; }

    @Override
    public String name(PipeConnection value) { return value.getName(); }

    @Override
    public Optional<PipeConnection> parse(String name) {
        for (var value : values) {
            if (name.equals(value.getName())) {
                return Optional.of(value);
            }
        }

        return Optional.empty();
    }
}