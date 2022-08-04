package work.lclpnet.ryuo.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TicksTest {

    @Test
    void nearestEnclosingMillis() {
        assertEquals(0L, Ticks.nearestEnclosingMillis(0L));

        assertEquals(1L, Ticks.nearestEnclosingMillis(5L));
        assertEquals(1L, Ticks.nearestEnclosingMillis(49L));
        assertEquals(1L, Ticks.nearestEnclosingMillis(50L));

        assertEquals(2L, Ticks.nearestEnclosingMillis(51L));

        assertEquals(20L, Ticks.nearestEnclosingMillis(999L));
        assertEquals(20L, Ticks.nearestEnclosingMillis(1000L));
        assertEquals(21L, Ticks.nearestEnclosingMillis(1001L));
    }

    @Test
    void seconds() {
        assertEquals(0, Ticks.seconds(0));
        assertEquals(20, Ticks.seconds(1));
        assertEquals(40, Ticks.seconds(2));
    }

    @Test
    void minutes() {
        assertEquals(0, Ticks.minutes(0));
        assertEquals(1200, Ticks.minutes(1));
        assertEquals(2400, Ticks.minutes(2));
    }
}