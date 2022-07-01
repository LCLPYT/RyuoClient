package work.lclpnet.test;

import net.minecraft.util.Identifier;
import org.junit.jupiter.api.Test;
import work.lclpnet.ryuo.Ryuo;

import static org.junit.jupiter.api.Assertions.*;

public class RyuoTest {

    @Test
    void testIdentifier() {
        var id = Ryuo.identifier("test");
        assertEquals(new Identifier(Ryuo.MOD_ID, "test"), id);

        id = Ryuo.identifier("test_%s", 5);
        assertEquals(new Identifier(Ryuo.MOD_ID, "test_5"), id);
    }

    @Test
    void testModuleEncapsulation() {
        final var ryuo = new RyuoMock();
        ryuo.onInitialize();

        assertFalse(Ryuo.getModules().isEmpty());

        assertTrue(Ryuo.getModule(RyuoTestModule.ID).isPresent());
        assertTrue(Ryuo.getModule(RyuoTestModule.ID, RyuoTestModule.class).isPresent());
    }
}
