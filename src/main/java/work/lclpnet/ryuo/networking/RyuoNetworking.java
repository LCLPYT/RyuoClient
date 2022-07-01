package work.lclpnet.ryuo.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.LogManager;
import work.lclpnet.mmocontent.networking.MCPacket;
import work.lclpnet.mmocontent.networking.MMOPacketRegistrar;
import work.lclpnet.mmocontent.util.Env;
import work.lclpnet.ryuo.server.RyuoServer;

import java.util.Objects;

public class RyuoNetworking {

    private static MMOPacketRegistrar registrar = null;

    public static void registerPackets() {
        if (registrar != null) return; // already registered

        registrar = new MMOPacketRegistrar(LogManager.getLogger());

        // register packets here
        registrar.register(ModuleUpdateS2CPacket.ID, new ModuleUpdateS2CPacket.Decoder());
    }

    @Environment(EnvType.CLIENT)
    public static void registerClientPacketHandlers() {
        registrar.registerClientPacketHandlers();
        registrar = null; // should be called last on client
    }

    public static void registerServerPacketHandlers() {
        registrar.registerServerPacketHandlers();
        if (!Env.isClient()) registrar = null; // not needed any further on a dedicated server
    }

    public static void sendToAll(MCPacket packet) {
        final var server = RyuoServer.getServer();
        Objects.requireNonNull(server, "Server is not initialized");

        packet.sendToAll(server);
    }
}
