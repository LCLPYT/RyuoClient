package work.lclpnet.ryuo.module;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RyuoModulesTest {

    @Test
    void testModuleEncapsulation() {
        new RyuoModulesMock().collectModules();
        RyuoModules.register();

        assertFalse(RyuoModules.all().isEmpty());

        assertTrue(RyuoModules.get(RyuoTestModule.ID).isPresent());
        assertTrue(RyuoModules.get(RyuoTestModule.ID, RyuoTestModule.class).isPresent());
    }
}
