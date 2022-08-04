package work.lclpnet.ryuo.server.game;

import work.lclpnet.ryuo.server.game.lobby.GameLobby;

public interface IGameInstance {

    void init();

    void reset();

    GameLobby lobby();
}
