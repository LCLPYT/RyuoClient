package work.lclpnet.ryuo.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.server.command.ServerCommandSource;
import work.lclpnet.mmocontent.cmd.MMOArgumentTypes;

import static work.lclpnet.ryuo.Ryuo.identifier;

public class RyuoCommands {

    private static volatile boolean listenerRegistered = false;

    public static void register() {
        if (listenerRegistered) return;
        listenerRegistered = true;

        registerArgumentTypes();
        CommandRegistrationCallback.EVENT.register(RyuoCommands::register);
    }

    private static void registerArgumentTypes() {
        MMOArgumentTypes.register(identifier("game"), RyuoGameArgumentType.class, new ConstantArgumentSerializer<>(RyuoGameArgumentType::game));
    }

    private static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        // register new commands here
        new SetGameCommand().register(dispatcher);
    }
}
