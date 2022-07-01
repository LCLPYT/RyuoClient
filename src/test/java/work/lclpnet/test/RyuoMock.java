package work.lclpnet.test;

import com.google.common.collect.ImmutableBiMap;
import work.lclpnet.ryuo.Ryuo;

public class RyuoMock extends Ryuo {

    @Override
    public void onInitialize() {
        modules = ImmutableBiMap.of(RyuoTestModule.ID, new RyuoTestModule());
        registerModules();
    }
}
