package work.lclpnet.ryuo.server;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import work.lclpnet.ryuo.Ryuo;
import work.lclpnet.ryuo.hook.RyuoDataReadyHook;
import work.lclpnet.ryuo.networking.ModuleUpdateS2CPacket;

public class RyuoServer implements DedicatedServerModInitializer {

    private static MinecraftServer server = null;

    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            RyuoServer.server = server;
            new ModuleUpdateS2CPacket(Ryuo.getModules()).sendToAll(server);
        });

        sendModuleInitialStatus();
    }

    private static void sendModuleInitialStatus() {
        RyuoDataReadyHook.EVENT.register(RyuoServer::sendModuleStatus);
    }

    public static void sendModuleStatus(ServerPlayerEntity player) {
        new ModuleUpdateS2CPacket(Ryuo.getModules()).sendTo(player);
    }

    public static MinecraftServer getServer() {
        return server;
    }
}
