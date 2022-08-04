package work.lclpnet.ryuo.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import work.lclpnet.mmocontent.cmd.AbstractCommand;

import static net.minecraft.server.command.CommandManager.argument;
import static work.lclpnet.ryuo.command.RyuoGameArgumentType.game;
import static work.lclpnet.ryuo.command.RyuoGameArgumentType.getGame;

public class SetGameCommand extends AbstractCommand {

    public SetGameCommand() {
        super("setgame");
        addAlias("sg");
    }

    @Override
    protected LiteralArgumentBuilder<ServerCommandSource> build(LiteralArgumentBuilder<ServerCommandSource> builder) {
        return builder
                .requires(cs -> cs.hasPermissionLevel(2))
                .then(argument("game", game())
                        .executes(SetGameCommand::setGame));
    }

    public static int setGame(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        final var game = getGame(ctx, "game");

        ctx.getSource().sendFeedback(new LiteralText("Set active game to %s.".formatted(game.getName())), true);
        // TODO make game active
        return 0;
    }
}
