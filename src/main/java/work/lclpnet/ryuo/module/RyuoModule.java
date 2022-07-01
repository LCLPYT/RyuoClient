package work.lclpnet.ryuo.module;

import net.minecraft.util.Identifier;

public interface RyuoModule {

    Identifier getId();

    /**
     * Called on mod init, regardless of enabled.
     */
    void register();

    void setEnabled(boolean enabled);

    boolean isEnabled();
}
