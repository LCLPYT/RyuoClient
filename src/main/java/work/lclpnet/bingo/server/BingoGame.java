package work.lclpnet.bingo.server;

import net.minecraft.util.Identifier;
import work.lclpnet.bingo.BingoModule;
import work.lclpnet.ryuo.Ryuo;
import work.lclpnet.ryuo.module.RyuoModules;
import work.lclpnet.ryuo.server.game.AbstractRyuoGame;
import work.lclpnet.ryuo.server.game.GameInit;
import work.lclpnet.ryuo.server.game.IGameInstance;

public class BingoGame extends AbstractRyuoGame {

    public static final Identifier ID = Ryuo.identifier("bingo");

    public BingoGame() {
        super(ID, "Bingo", RyuoModules.modules(BingoModule.ID));
    }

    @Override
    public IGameInstance makeInstance(GameInit init) {
        return new BingoGameInstance(this, init);
    }
}
