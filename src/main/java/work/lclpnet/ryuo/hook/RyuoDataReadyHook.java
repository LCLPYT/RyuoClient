package work.lclpnet.ryuo.hook;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public interface RyuoDataReadyHook {

    Event<RyuoDataReadyHook> EVENT = EventFactory.createArrayBacked(RyuoDataReadyHook.class,
            (listeners) -> player -> {
                for (RyuoDataReadyHook listener : listeners)
                    listener.onDataReady(player);
            });

    void onDataReady(ServerPlayerEntity player);
}
