package work.lclpnet.ryuo;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import work.lclpnet.ryuo.command.RyuoCommands;
import work.lclpnet.ryuo.module.RyuoModules;
import work.lclpnet.ryuo.networking.RyuoNetworking;

public class Ryuo implements ModInitializer {

    public static final String MOD_ID = "ryuo";
    public static final String SERVER_MOD_NAME = "Ryuo";

    @Override
    public void onInitialize() {
        new RyuoModules().collectModules();
        RyuoModules.register();

        RyuoNetworking.registerPackets();
        RyuoNetworking.registerServerPacketHandlers();

        RyuoCommands.register();
    }

    public static Identifier identifier(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static Identifier identifier(String format, Object... substitutes) {
        return identifier(String.format(format, substitutes));
    }
}
