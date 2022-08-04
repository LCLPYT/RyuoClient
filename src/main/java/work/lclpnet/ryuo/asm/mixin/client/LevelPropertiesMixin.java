package work.lclpnet.ryuo.asm.mixin.client;

import com.mojang.serialization.Lifecycle;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelProperties.class)
public class LevelPropertiesMixin {

    @Shadow @Final private Lifecycle lifecycle;

    @Inject(
            method = "getLifecycle",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ryuo$getLifecycle(CallbackInfoReturnable<Lifecycle> cir) {
        // disable warning screen
        if (lifecycle == Lifecycle.experimental()) {
            cir.setReturnValue(Lifecycle.stable());
        }
    }
}
