package work.lclpnet.ryuo.server;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.lclpnet.ryuo.hook.RyuoDataReadyHook;
import work.lclpnet.ryuo.module.RyuoModules;
import work.lclpnet.ryuo.networking.ModuleUpdateS2CPacket;
import work.lclpnet.ryuo.server.game.RyuoGames;

@Environment(EnvType.SERVER)
public class RyuoServer implements DedicatedServerModInitializer {

    private static final Logger logger = LogManager.getLogger();
    private static MinecraftServer server = null;

    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            RyuoServer.server = server;
            new ModuleUpdateS2CPacket(RyuoModules.all()).sendToAll(server);
        });

        sendModuleInitialStatus();

        new RyuoGames().collectGames();
        logger.info("Available games: {}", RyuoGames.all());

        logger.info("Loading server config...");
        RyuoServerConfig.load().thenRun(() -> logger.info("Server config successfully loaded."));
    }

    private static void sendModuleInitialStatus() {
        RyuoDataReadyHook.EVENT.register(RyuoServer::sendModuleStatus);
    }

    public static void sendModuleStatus(ServerPlayerEntity player) {
        new ModuleUpdateS2CPacket(RyuoModules.all()).sendTo(player);
    }

    public static MinecraftServer getServer() {
        return server;
    }
}
