package work.lclpnet.ryuo;

import com.google.common.collect.ImmutableBiMap;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import work.lclpnet.bingo.BingoModule;
import work.lclpnet.ryuo.module.RyuoModule;
import work.lclpnet.ryuo.networking.RyuoNetworking;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Ryuo implements ModInitializer {

    public static final String MOD_ID = "ryuo";
    public static final String SERVER_MOD_NAME = "Ryuo";

    protected static ImmutableBiMap<Identifier, RyuoModule> modules;

    @Override
    public void onInitialize() {
        // init modules
        final ImmutableBiMap.Builder<Identifier, RyuoModule> builder = ImmutableBiMap.builder();
        List.of(
                // register new modules here
                new BingoModule()

        ).forEach(m -> builder.put(m.getId(), m));
        modules = builder.build();

        registerModules();

        RyuoNetworking.registerPackets();
        RyuoNetworking.registerServerPacketHandlers();
    }

    protected void registerModules() {
        modules.values().forEach(RyuoModule::register);
    }

    public static Collection<RyuoModule> getModules() {
        return modules.values();
    }

    public static Optional<RyuoModule> getModule(Identifier id) {
        return Optional.ofNullable(modules.get(id));
    }

    @SuppressWarnings("unchecked")
    public static <T extends RyuoModule> Optional<T> getModule(Identifier id, Class<T> module) {
        return getModule(id).filter(mod -> module.equals(mod.getClass())).map(m -> (T) m);
    }

    public static Identifier identifier(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static Identifier identifier(String format, Object... substitutes) {
        return identifier(String.format(format, substitutes));
    }
}
