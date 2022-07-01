package work.lclpnet.ryuo.client;

import net.fabricmc.api.ClientModInitializer;
import work.lclpnet.bingo.client.BingoClientModule;
import work.lclpnet.ryuo.client.module.RyuoClientModule;
import work.lclpnet.ryuo.networking.RyuoNetworking;

import java.util.List;

public class RyuoClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // init client modules
        List.of(
                // register new client modules here
                new BingoClientModule()

        ).forEach(RyuoClientModule::register);

        RyuoNetworking.registerPackets();
        RyuoNetworking.registerClientPacketHandlers();
    }
}
