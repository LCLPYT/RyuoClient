package work.lclpnet.ryuo.module;

import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.lclpnet.mmocontent.util.Env;
import work.lclpnet.ryuo.networking.ModuleUpdateS2CPacket;
import work.lclpnet.ryuo.server.RyuoServer;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractRyuoModule implements RyuoModule {

    private static final Logger logger = LogManager.getLogger();

    private final AtomicBoolean enabled = new AtomicBoolean(false);
    private final Identifier id;

    protected AbstractRyuoModule(Identifier id) {
        this.id = Objects.requireNonNull(id);
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public void setEnabled(boolean enabled) {
        final boolean wasEnabled = this.enabled.get();

        this.enabled.set(enabled);

        if (wasEnabled != enabled) {
            if (enabled) {
                onEnable();
            } else {
                onDisable();
            }

            if (!Env.isClient()) {
                new ModuleUpdateS2CPacket(this).sendToAll(RyuoServer.getServer());
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled.get();
    }

    protected void onEnable() {
        logger.info("Enabled module {}", id);
    }

    protected void onDisable() {
        logger.info("Disabled module {}", id);
    }
}
