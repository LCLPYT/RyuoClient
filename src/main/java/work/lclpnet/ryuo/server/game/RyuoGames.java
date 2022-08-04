package work.lclpnet.ryuo.server.game;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.util.Identifier;
import work.lclpnet.bingo.server.BingoGame;

import java.util.List;
import java.util.Optional;

public class RyuoGames {

    protected static ImmutableBiMap<Identifier, RyuoGame> games;

    public void collectGames() {
        // register new modules here
        var list = List.of(new BingoGame());

        final ImmutableBiMap.Builder<Identifier, RyuoGame> builder = ImmutableBiMap.builder();
        list.forEach(m -> builder.put(m.getId(), m));
        games = builder.build();
    }

    public static ImmutableSet<RyuoGame> all() {
        return games.values();
    }

    public static Optional<RyuoGame> get(Identifier id) {
        return Optional.ofNullable(games.get(id));
    }

    @SuppressWarnings("unchecked")
    public static <T extends RyuoGame> Optional<T> get(Identifier id, Class<T> module) {
        return get(id).filter(mod -> module.equals(mod.getClass())).map(m -> (T) m);
    }
}
