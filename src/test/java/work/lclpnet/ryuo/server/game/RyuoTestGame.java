package work.lclpnet.ryuo.server.game;

import net.minecraft.util.Identifier;
import work.lclpnet.ryuo.Ryuo;

import java.util.Set;

public class RyuoTestGame extends AbstractRyuoGame {

    public static final Identifier ID = Ryuo.identifier("test");

    public RyuoTestGame() {
        super(ID, "Test Game", Set.of());
    }

    @Override
    public IGameInstance makeInstance(GameInit init) {
        return null;
    }
}
