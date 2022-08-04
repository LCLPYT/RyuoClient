package work.lclpnet.ryuo.module;

import net.minecraft.util.Identifier;
import work.lclpnet.ryuo.Ryuo;

public class RyuoTestModule extends AbstractRyuoModule {

    public static final Identifier ID = Ryuo.identifier("test");

    public RyuoTestModule() {
        super(ID);
    }

    @Override
    public void register() {
        System.out.println("Ryuo test module registered successfully.");
    }
}
