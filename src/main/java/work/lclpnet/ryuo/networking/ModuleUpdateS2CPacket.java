package work.lclpnet.ryuo.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.lclpnet.mmocontent.networking.IClientPacketHandler;
import work.lclpnet.mmocontent.networking.IPacketDecoder;
import work.lclpnet.mmocontent.networking.MCPacket;
import work.lclpnet.ryuo.Ryuo;
import work.lclpnet.ryuo.module.RyuoModule;
import work.lclpnet.ryuo.module.RyuoModules;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModuleUpdateS2CPacket extends MCPacket implements IClientPacketHandler {

    public static final Identifier ID = Ryuo.identifier("module_update");
    private static final Logger LOGGER = LogManager.getLogger();

    protected final Map<Identifier, Boolean> status;

    public ModuleUpdateS2CPacket(RyuoModule update) {
        this(Collections.singleton(update));
    }

    public ModuleUpdateS2CPacket(Iterable<RyuoModule> update) {
        this(serializeModules(update));
    }

    protected ModuleUpdateS2CPacket(Map<Identifier, Boolean> status) {
        super(ID);
        this.status = status;
    }

    @Override
    public void encodeTo(PacketByteBuf buffer) {
        buffer.writeInt(status.size());
        status.forEach((id, status) -> {
            buffer.writeIdentifier(id);
            buffer.writeBoolean(status);
        });
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void handleClient(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender sender) {
        status.forEach((id, status) -> RyuoModules.get(id)
                .ifPresentOrElse(m -> m.setEnabled(status), () -> LOGGER.warn("Unknown module {}", id)));
    }

    private static Map<Identifier, Boolean> serializeModules(Iterable<RyuoModule> modules) {
        Map<Identifier, Boolean> status = new HashMap<>();
        modules.forEach(m -> status.put(m.getId(), m.isEnabled()));
        return status;
    }

    public static class Decoder implements IPacketDecoder<ModuleUpdateS2CPacket> {

        @Override
        public ModuleUpdateS2CPacket decode(PacketByteBuf buffer) {
            final int size = buffer.readInt();
            final Map<Identifier, Boolean> status = new HashMap<>();

            for (int i = 0; i < size; i++) {
                final Identifier id = buffer.readIdentifier();
                final boolean enabled = buffer.readBoolean();

                status.put(id, enabled);
            }

            return new ModuleUpdateS2CPacket(status);
        }
    }
}
