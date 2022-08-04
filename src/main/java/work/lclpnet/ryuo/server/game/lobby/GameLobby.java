package work.lclpnet.ryuo.server.game.lobby;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import work.lclpnet.ryuo.util.Tickable;
import work.lclpnet.ryuo.util.Ticker;
import work.lclpnet.ryuo.util.Ticks;

import java.util.Objects;
import java.util.stream.Collectors;

public class GameLobby {

    private long duration = Ticks.seconds(30);
    protected final Multimap<Long, Runnable> scheduledTasks = HashMultimap.create();
    private final Ticker ticker;
    private final Tickable tickable;

    private long t = 0;

    public GameLobby(Ticker ticker) {
        this.ticker = ticker;

        // register tick event
        ticker.bind(tickable = () -> scheduledTasks.get(t++).forEach(Runnable::run));
    }

    public void reset() {
        ticker.unbind(tickable);
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(final long durationTicks) {
        this.duration = durationTicks;

        final var invalidTicks = scheduledTasks.keySet().stream()
                .filter(t -> t >= durationTicks)
                .collect(Collectors.toUnmodifiableSet());

        invalidTicks.forEach(scheduledTasks::removeAll);
    }

    public void atTick(long tick, Runnable action) {
        validateTick(tick);
        Objects.requireNonNull(action);
        scheduledTasks.put(tick, action);
    }

    public void removeAllAtTick(long tick) {
        scheduledTasks.removeAll(tick);
    }

    public void removeAtTick(long tick, Runnable action) {
        scheduledTasks.remove(tick, action);
    }

    protected void validateTick(long tick) {
        if (tick < 0 || tick >= duration)
            throw new IllegalArgumentException("Tick is out of scheduling range");
    }
}
