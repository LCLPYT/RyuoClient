package work.lclpnet.ryuo.server.game;

import com.google.common.collect.ImmutableBiMap;

public class RyuoGamesMock extends RyuoGames {

    @Override
    public void collectGames() {
        games = ImmutableBiMap.of(RyuoTestGame.ID, new RyuoTestGame());
    }
}
