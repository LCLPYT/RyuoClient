package work.lclpnet.ryuo.server.game;

import work.lclpnet.ryuo.server.game.lobby.GameLobby;

public class GameInstance<T extends RyuoGame> implements IGameInstance {

    public final T game;
    private final GameLobby lobby;

    public GameInstance(T game, GameInit init) {
        this.game = game;
        lobby = createLobby(init);
    }

    protected GameLobby createLobby(GameInit init) {
        return new GameLobby(init.ticker());
    }

    @Override
    public void init() {
         // NO-OP
    }

    @Override
    public void reset() {
        lobby.reset();
    }

    @Override
    public GameLobby lobby() {
        return lobby;
    }
}
