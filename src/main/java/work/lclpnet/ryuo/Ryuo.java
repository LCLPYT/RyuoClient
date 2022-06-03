package work.lclpnet.ryuo;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Ryuo implements ModInitializer {

    public static final String MOD_ID = "ryuo";

    @Override
    public void onInitialize() {
        // common init
    }

    public static Identifier identifier(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static Identifier identifier(String format, Object... substitutes) {
        return identifier(String.format(format, substitutes));
    }
}
