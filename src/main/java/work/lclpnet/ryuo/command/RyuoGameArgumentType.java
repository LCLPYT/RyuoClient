package work.lclpnet.ryuo.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import work.lclpnet.bingo.server.BingoGame;
import work.lclpnet.mmocontent.cmd.MMOCommands;
import work.lclpnet.ryuo.server.game.RyuoGame;
import work.lclpnet.ryuo.server.game.RyuoGames;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class RyuoGameArgumentType implements ArgumentType<RyuoGame> {

    private static final DynamicCommandExceptionType UNKNOWN_GAME = MMOCommands.dynamicError("commands.setgame.game_not_found");
    private static final Collection<String> EXAMPLES = Arrays.asList(BingoGame.ID.toString(), BingoGame.ID.getPath());

    public static RyuoGameArgumentType game() {
        return new RyuoGameArgumentType();
    }

    public static RyuoGame getGame(CommandContext<ServerCommandSource> context, String name) throws CommandSyntaxException {
        return validate(context.getArgument(name, Identifier.class));
    }

    private static RyuoGame validate(Identifier id) throws CommandSyntaxException {
        return RyuoGames.get(id)
                .orElseThrow(() -> UNKNOWN_GAME.create(id));
    }

    @Override
    public RyuoGame parse(StringReader reader) throws CommandSyntaxException {
        return validate(Identifier.fromCommandInput(reader));
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        var candidates = RyuoGames.all().stream().map(RyuoGame::getId);
        return CommandSource.suggestIdentifiers(candidates, builder);
    }
}
