package work.lclpnet.bingo;

import net.minecraft.util.Identifier;
import work.lclpnet.ryuo.Ryuo;
import work.lclpnet.ryuo.module.AbstractRyuoModule;

public class BingoModule extends AbstractRyuoModule {

    public static final String MOD_ID = "bingo";
    public static final Identifier ID = Ryuo.identifier(MOD_ID);

    public BingoModule() {
        super(ID);
    }

    @Override
    public void register() {

    }

    public static Identifier identifier(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static Identifier identifier(String format, Object... substitutes) {
        return identifier(String.format(format, substitutes));
    }
}
