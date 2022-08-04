package work.lclpnet.ryuo;

import net.minecraft.util.Identifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RyuoTest {

    @Test
    void testIdentifier() {
        var id = Ryuo.identifier("test");
        assertEquals(new Identifier(Ryuo.MOD_ID, "test"), id);

        id = Ryuo.identifier("test_%s", 5);
        assertEquals(new Identifier(Ryuo.MOD_ID, "test_5"), id);
    }
}
