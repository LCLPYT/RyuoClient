package work.lclpnet.ryuo.asm.mixin.server;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.ryuo.Ryuo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(
            method = "getServerModName",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    public void getServerModName(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(Ryuo.SERVER_MOD_NAME);
    }
}
