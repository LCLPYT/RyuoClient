package work.lclpnet.ryuo.module;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.lclpnet.bingo.BingoModule;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class RyuoModules {

    private static volatile boolean registered = false;
    protected static ImmutableBiMap<Identifier, RyuoModule> modules;
    private static final Logger logger = LogManager.getLogger();

    public void collectModules() {
        // register new modules here
        var list = List.of(new BingoModule());

        final ImmutableBiMap.Builder<Identifier, RyuoModule> builder = ImmutableBiMap.builder();
        list.forEach(m -> builder.put(m.getId(), m));
        modules = builder.build();
    }

    public static void register() {
        if (registered) return;
        registered = true;

        logger.info("Registering modules: {}", modules.values());
        modules.values().forEach(RyuoModule::register);
    }

    public static ImmutableSet<RyuoModule> all() {
        return modules.values();
    }

    public static Optional<RyuoModule> get(Identifier id) {
        return Optional.ofNullable(modules.get(id));
    }

    @SuppressWarnings("unchecked")
    public static <T extends RyuoModule> Optional<T> get(Identifier id, Class<T> module) {
        return get(id).filter(mod -> module.equals(mod.getClass())).map(m -> (T) m);
    }

    public static Set<RyuoModule> modules(Identifier... ids) {
        return Arrays.stream(ids)
                .map(RyuoModules::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toUnmodifiableSet());
    }
}
