package work.lclpnet.ryuo.module;

import com.google.common.collect.ImmutableBiMap;

public class RyuoModulesMock extends RyuoModules {

    @Override
    public void collectModules() {
        modules = ImmutableBiMap.of(RyuoTestModule.ID, new RyuoTestModule());
    }
}
