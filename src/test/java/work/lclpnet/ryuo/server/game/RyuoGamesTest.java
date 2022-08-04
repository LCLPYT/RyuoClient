package work.lclpnet.ryuo.server.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RyuoGamesTest {

    @Test
    void testGameEncapsulation() {
        new RyuoGamesMock().collectGames();

        assertFalse(RyuoGames.all().isEmpty());

        assertTrue(RyuoGames.get(RyuoTestGame.ID).isPresent());
        assertTrue(RyuoGames.get(RyuoTestGame.ID, RyuoTestGame.class).isPresent());
    }
}
