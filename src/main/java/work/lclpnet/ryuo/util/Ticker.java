package work.lclpnet.ryuo.util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Ticker implements Tickable {

    private final Set<Tickable> tickables = new HashSet<>();

    @Override
    public void tick() {
        tickables.forEach(Tickable::tick);
    }

    public void bind(Tickable tickable) {
        Objects.requireNonNull(tickable);
        tickables.add(tickable);
    }

    public void unbind(Tickable tickable) {
        Objects.requireNonNull(tickable);
        tickables.remove(tickable);
    }
}
