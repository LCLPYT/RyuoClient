package work.lclpnet.ryuo.server;

import net.fabricmc.loader.api.FabricLoader;
import work.lclpnet.mmocontent.util.ConfigHelper;
import work.lclpnet.ryuo.Ryuo;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class RyuoServerConfig {

    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir()
            .resolve(Ryuo.MOD_ID).resolve("server").resolve("config.json");

    private static RyuoServerConfig config = null;

    /* actual configuration properties; all types supported by GSON are allowed */
    private final LobbyConfig lobby = new LobbyConfig();

    protected RyuoServerConfig() {}

    /* static getters and setters */
    public static String lobbyDownload() {
        return config.lobby.download;
    }

    // optional update callback method (useful for reacting to general config changes)
    public static void onChange() {
        // implement behaviour here
    }

    // IO logic boilerplate
    public static CompletableFuture<Void> load() {
        return ConfigHelper.load(CONFIG_PATH, RyuoServerConfig.class, RyuoServerConfig::new).thenAccept(conf -> {
            config = conf;
            onChange();
        });
    }

    public static CompletableFuture<Void> save() {
        return ConfigHelper.save(CONFIG_PATH, config);
    }

    private static class LobbyConfig {
        private final String download = "https://lclpnet.work/dl/lobby_1.18.2";
    }
}
