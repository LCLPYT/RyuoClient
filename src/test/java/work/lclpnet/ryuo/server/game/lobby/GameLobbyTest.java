package work.lclpnet.ryuo.server.game.lobby;

import org.junit.jupiter.api.Test;
import work.lclpnet.ryuo.util.Ticker;
import work.lclpnet.ryuo.util.Ticks;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class GameLobbyTest {

    @Test
    void setDuration() {
        var lobby = new GameLobby(new Ticker());

        final long ticks = Ticks.minutes(2);
        lobby.setDuration(ticks);

        assertEquals(ticks, lobby.getDuration());
    }

    @Test
    void scheduleInvalidTick() {
        final var lobby = new GameLobby(new Ticker());
        final var duration = Ticks.seconds(30);
        lobby.setDuration(duration);

        final Runnable dummy = () -> {};

        assertThrows(IllegalArgumentException.class, () -> lobby.atTick(-1L, dummy));
        assertThrows(IllegalArgumentException.class, () -> lobby.atTick(duration, dummy));
        assertThrows(IllegalArgumentException.class, () -> lobby.atTick(duration + Ticks.seconds(5), dummy));
    }

    @Test
    void atTick() {
        final Ticker ticker = new Ticker();

        final var lobby = new GameLobby(ticker);
        final var duration = Ticks.seconds(30);
        lobby.setDuration(duration);

        final boolean[] called = new boolean[4];
        Arrays.fill(called, false);

        lobby.atTick(0, () -> called[0] = true);
        lobby.atTick(0, () -> called[1] = true);
        lobby.atTick(duration / 2L, () -> called[2] = true);
        lobby.atTick(duration - 1L, () -> called[3] = true);

        // simulate tick
        for (int t = 0; t < duration; t++)
            ticker.tick();

        lobby.reset();

        assertArrayEquals(new boolean[] {true, true, true, true}, called);
    }

    @Test
    void setDurationRemoveInvalidActions() {
        final Ticker ticker = new Ticker();

        final var lobby = new GameLobby(ticker);
        final var duration = Ticks.seconds(30);
        lobby.setDuration(duration);

        final boolean[] called = new boolean[4];
        Arrays.fill(called, false);

        lobby.atTick(0, () -> called[0] = true);
        lobby.atTick(0, () -> called[1] = true);
        lobby.atTick(duration / 2L, () -> called[2] = true);
        lobby.atTick(duration - 1L, () -> called[3] = true);

        // last action at duration - 1 should be dropped
        lobby.setDuration(duration - 1);

        // simulate tick
        for (int t = 0; t < duration; t++)
            ticker.tick();

        lobby.reset();

        assertArrayEquals(new boolean[] {true, true, true, false}, called);
    }

    @Test
    void removeAtTick() {
        final Ticker ticker = new Ticker();

        final var lobby = new GameLobby(ticker);
        final var duration = Ticks.seconds(30);
        lobby.setDuration(duration);

        final boolean[] called = new boolean[4];
        Arrays.fill(called, false);

        lobby.atTick(0, () -> called[0] = true);
        lobby.atTick(0, () -> called[1] = true);
        final Runnable middleTask = () -> called[2] = true;
        lobby.atTick(duration / 2L, middleTask);
        lobby.atTick(duration - 1L, () -> called[3] = true);

        lobby.removeAllAtTick(0);
        lobby.removeAtTick(duration / 2L, middleTask);

        // simulate tick
        for (int t = 0; t < duration; t++)
            ticker.tick();

        lobby.reset();

        assertArrayEquals(new boolean[] {false, false, false, true}, called);
    }

    @Test
    void reset() {
        final Ticker ticker = new Ticker();

        final var lobby = new GameLobby(ticker);
        final var duration = Ticks.seconds(30);
        lobby.setDuration(duration);

        AtomicBoolean called = new AtomicBoolean(false);
        lobby.atTick(0, () -> called.set(true));

        // detach ticker from lobby
        lobby.reset();

        // simulate tick
        for (int t = 0; t < duration; t++)
            ticker.tick();

        assertFalse(called.get());
    }
}