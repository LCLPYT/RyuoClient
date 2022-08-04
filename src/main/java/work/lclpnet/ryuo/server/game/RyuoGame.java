package work.lclpnet.ryuo.server.game;

import net.minecraft.util.Identifier;
import work.lclpnet.ryuo.module.RyuoModule;

import java.util.Set;

public interface RyuoGame {

    Identifier getId();

    String getName();

    Set<RyuoModule> getModules();

    IGameInstance makeInstance(GameInit init);
}
