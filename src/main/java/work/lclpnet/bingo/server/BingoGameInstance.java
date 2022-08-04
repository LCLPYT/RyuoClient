package work.lclpnet.bingo.server;

import work.lclpnet.ryuo.server.game.GameInit;
import work.lclpnet.ryuo.server.game.GameInstance;
import work.lclpnet.ryuo.util.Ticks;

public class BingoGameInstance extends GameInstance<BingoGame> {

    public BingoGameInstance(BingoGame game, GameInit init) {
        super(game, init);
        lobby().setDuration(Ticks.seconds(60));
    }


}
