package work.lclpnet.ryuo.client;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.lclpnet.bingo.client.BingoClientModule;
import work.lclpnet.ryuo.client.module.RyuoClientModule;
import work.lclpnet.ryuo.networking.RyuoNetworking;

import java.util.Set;

public class RyuoClient implements ClientModInitializer {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void onInitializeClient() {
        registerClientModules();

        RyuoNetworking.registerPackets();
        RyuoNetworking.registerClientPacketHandlers();
    }

    private void registerClientModules() {
        // init client modules
        var clientModules = Set.of(
                // register new client modules here
                new BingoClientModule()
        );

        logger.info("Registering {} client modules...", clientModules.size());
        clientModules.forEach(RyuoClientModule::register);
    }
}
